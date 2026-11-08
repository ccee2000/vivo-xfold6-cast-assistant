package com.test.virtual4k

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rikka.shizuku.Shizuku

/**
 * 外接屏分辨率检查 + 一键切换 4K / 2K
 *
 * 背景（真机验证过）：
 * vivo 电脑模式接管 HDMI 外接屏后，会给它叠加一个「分辨率覆盖」，
 * 把物理 3840x2160 压成 2560x1440 渲染。清除这个覆盖就回到原生 4K。
 * 覆盖属于系统级设置，普通 App 无权限，因此通过 Shizuku 以 shell 身份执行：
 *   wm size reset -d <外屏ID>       → 恢复物理分辨率（4K）
 *   wm size 2560x1440 -d <外屏ID>   → 压回 2K（注意：尺寸必须写在 -d 前面，
 *                                     写成 "wm size -d ID 2560x1440" 会被当成查询，静默无效）
 * displayId 每次插拔都会变，运行时动态识别，不写死。
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "4KToggle"
        private const val REQ_SHIZUKU = 3001

        /** 4K → 2K 的缩放比：3840/1.5=2560，2160/1.5=1440，与 vivo 电脑模式一致 */
        private const val DOWNSCALE_RATIO = 1.5
    }

    private lateinit var displayManager: DisplayManager
    private lateinit var infoView: TextView
    private lateinit var btn4k: Button
    private lateinit var btn2k: Button
    private lateinit var sub4k: TextView
    private lateinit var sub2k: TextView
    private var busy = false

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = refresh("显示器接入")
        override fun onDisplayRemoved(displayId: Int) = refresh("显示器断开")
        override fun onDisplayChanged(displayId: Int) = refresh("显示参数变化")
    }

    private val permissionListener =
        Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
            if (requestCode == REQ_SHIZUKU) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    toast("Shizuku 权限已授予，再点一次按钮")
                } else {
                    toast("Shizuku 权限被拒绝")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoView = findViewById(R.id.tvInfo)
        btn4k = findViewById(R.id.btn4k)
        btn2k = findViewById(R.id.btn2k)
        sub4k = findViewById(R.id.tvSub4k)
        sub2k = findViewById(R.id.tvSub2k)
        displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager

        Shizuku.addRequestPermissionResultListener(permissionListener)
        displayManager.registerDisplayListener(displayListener, null)

        btn4k.setOnClickListener { switchTo(force4k = true) }
        btn2k.setOnClickListener { switchTo(force4k = false) }
        findViewById<TextView>(R.id.tvHelp).setOnClickListener { showHelp() }
        findViewById<TextView>(R.id.tvGithub).setOnClickListener {
            openUrl("https://github.com/ccee2000")
        }
        applyWindowInsets()
        refresh(null)
    }

    /** 版权区里的 GitHub 链接，点了用系统浏览器打开 */
    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (t: Throwable) {
            Log.w(TAG, "打开链接失败: ${t.message}")
            toast("没有找到可用的浏览器")
        }
    }

    /** Android 15 起强制 edge-to-edge，不处理 insets 标题会被状态栏压住 */
    private fun applyWindowInsets() {
        val root = findViewById<View>(R.id.rootScroll)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        ViewCompat.requestApplyInsets(root)
    }

    /** 帮助页：Shizuku 安装 → 启动服务 → 授权 → 验证，四步走 */
    private fun showHelp() {
        val content = layoutInflater.inflate(R.layout.dialog_help, null, false)
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.help_title)
            .setView(content)
            .setPositiveButton(R.string.help_close, null)
            .create()
        // 弹窗默认会把焦点给可滚动内容，自动滚到中段；强制回到顶部
        dialog.setOnShowListener { content.post { (content as ScrollView).scrollTo(0, 0) } }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        refresh(null)
    }

    override fun onDestroy() {
        try {
            displayManager.unregisterDisplayListener(displayListener)
        } catch (t: Throwable) {
            Log.w(TAG, "反注册显示监听失败: ${t.message}")
        }
        try {
            Shizuku.removeRequestPermissionResultListener(permissionListener)
        } catch (t: Throwable) {
            Log.w(TAG, "移除权限回调失败: ${t.message}")
        }
        super.onDestroy()
    }

    // ---------------- 状态刷新 ----------------

    private fun refresh(reason: String?) {
        if (reason != null) Log.i(TAG, "刷新原因: $reason")

        val display = findExternalDisplay()
        if (display == null) {
            infoView.text = "未检测到外接屏\n\n请接上 HDMI 后再查看"
            btn4k.isEnabled = false
            btn2k.isEnabled = false
            return
        }

        val mode = display.mode
        val real = realSizeOf(display)
        val isNative = real[0] == mode.physicalWidth && real[1] == mode.physicalHeight
        val down2k = downscaledSize(mode)

        val stateText = if (isNative) "4K（原生物理分辨率）" else "2K（被系统分辨率覆盖压制）"

        infoView.text = buildString {
            append("外接屏：").append(display.name).append("（displayId=").append(display.displayId).append("）\n")
            append("物理分辨率：").append(mode.physicalWidth).append(" × ").append(mode.physicalHeight)
            append(" @ ").append(mode.refreshRate.toInt()).append(" Hz\n")
            append("当前渲染：").append(real[0]).append(" × ").append(real[1]).append("\n")
            append("状态：").append(stateText)
        }

        btn4k.isEnabled = !busy
        btn2k.isEnabled = !busy
        // 当前所处的分辨率按钮变灰，一眼看出现在在哪一档
        btn4k.alpha = if (isNative) 0.45f else 1f
        btn2k.alpha = if (isNative) 1f else 0.45f
        // 按钮标题固定（4K 超清 / 2K 默认），分辨率放到按钮下方的小字里
        sub4k.text = "${mode.physicalWidth} × ${mode.physicalHeight}"
        sub2k.text = "${down2k[0]} × ${down2k[1]}"
    }

    // ---------------- 切换 ----------------

    /**
     * 两个按钮各管一个明确目标，不依赖当前状态做猜测：
     * force4k = true  → wm size reset -d <id>      清掉覆盖，回原生 4K
     * force4k = false → wm size 2560x1440 -d <id>  压成 2K
     */
    private fun switchTo(force4k: Boolean) {
        val display = findExternalDisplay()
        if (display == null) {
            toast("没有检测到外接屏")
            return
        }

        if (Shizuku.isPreV11() || !Shizuku.pingBinder()) {
            toast("Shizuku 未运行，请先启动 Shizuku")
            return
        }
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(REQ_SHIZUKU)
            toast("请允许 Shizuku 授权，授权后再点一次")
            return
        }

        val displayId = display.displayId
        val mode = display.mode
        val down2k = downscaledSize(mode)

        val command = if (force4k) {
            "wm size reset -d $displayId"
        } else {
            "wm size ${down2k[0]}x${down2k[1]} -d $displayId"
        }
        val actionName = if (force4k) getString(R.string.btn_4k) else getString(R.string.btn_2k)
        Log.i(TAG, "$actionName → displayId=$displayId, 命令: $command")

        busy = true
        btn4k.isEnabled = false
        btn2k.isEnabled = false

        Thread {
            val output = runShell(command)
            Log.i(TAG, "$actionName 执行结果: $output")
            runOnUiThread {
                busy = false
                refresh(null)
                toast("$actionName 已执行")
                // 系统生效有延迟，2 秒后再核对一次结果
                infoView.postDelayed({ refresh(null) }, 2000)
            }
        }.start()
    }

    private fun runShell(command: String): String {
        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            val stdout = process.inputStream.bufferedReader().use { it.readText() }.trim()
            val stderr = process.errorStream.bufferedReader().use { it.readText() }.trim()
            process.waitFor()
            stdout.ifBlank { stderr }
        } catch (t: Throwable) {
            Log.e(TAG, "shell[$command] 执行失败", t)
            "执行失败: ${t.message}"
        }
    }

    // ---------------- 工具 ----------------

    private fun findExternalDisplay(): Display? {
        var fallback: Display? = null
        for (display in displayManager.displays) {
            if (display.displayId == Display.DEFAULT_DISPLAY) continue
            val name = display.name.orEmpty()
            if (name.contains("HDMI", true) || name.contains("外接", true)) return display
            if (fallback == null) fallback = display
        }
        return fallback
    }

    private fun realSizeOf(display: Display): IntArray {
        val point = Point()
        try {
            display.getRealSize(point)
        } catch (t: Throwable) {
            Log.w(TAG, "getRealSize 失败: ${t.message}")
        }
        return intArrayOf(point.x, point.y)
    }

    private fun downscaledSize(mode: Display.Mode): IntArray {
        return intArrayOf(
            (mode.physicalWidth / DOWNSCALE_RATIO).toInt(),
            (mode.physicalHeight / DOWNSCALE_RATIO).toInt()
        )
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
