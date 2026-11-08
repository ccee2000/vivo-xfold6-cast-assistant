# CHANGELOG

## v1.0

首次正式发布。

- 主标题：vivo X Fold6 外接屏分辨率切换，标注推荐 HDMI 有线连接
- 一键切换 4K 超清（3840×2160）/ 2K 默认（2560×1440）
- 主界面显示外接屏物理分辨率、当前渲染分辨率与状态
- 使用帮助：4 步配置流程，仅无线调试启动 Shizuku，删除电脑 ADB 相关步骤
- 常见问题精简，末尾提示改为「系统升级后可能失效，如：升级OriginOS7。」
- 版权信息：作者 河西尼（酷安），邮箱、GitHub，移除微信
- versionCode = 1，versionName = 1.0
- README 界面预览改为 App 界面 1 张 + 外接显示器电脑模式 2K / 4K 实拍各 1 张（assets/）

## v1.0 英文版（并入同一个 v1.0 Release）

- 新增英文版 APK：`XFold6_Cast_EN_v1.0.apk`，包名 `com.test.virtual4k.en`（`com.test.virtual4k` 为中文版），两个包可同时安装
- 工程加入 Gradle productFlavors：`cn` / `en`，各自 applicationId，资源按 flavor 覆盖
- 界面文案（含状态面板、Toast）全部迁入字符串资源，英文包使用 `app/src/en/res/values/strings.xml` 全量英译，界面不再有硬编码中文
- 状态面板改为格式串，英文版显示 `Display: xxx (displayId=xx)` / `State: 2K (overridden by system)`
- 英文版桌面名 `X Fold6 Cast`，README 中英双份都补充双包下载表与外接屏 2K/4K 对照

## 文档

- README 合并为中英双语单文件（README.md）：英文在顶部、中文在下方，删除独立 README_EN.md；仓库首页默认渲染英文

## 重大修正

- 修正功能说明：无线投屏同样支持切换到 4K，此前文档写的「仅支持有线 HDMI、无线不生效」是错的，README 中英文与此条同步更正

## v1.1

- 主界面文案精简，长句移入「使用帮助」，帮助页保留完整 4 步流程与常见问题
- 去掉「仅支持有线 HDMI、无线投屏无效」的错误表述：有线 HDMI 与无线投屏都支持，无线切 4K 已实测可用
- 界面里的「请接上 HDMI」「重新插拔 HDMI」等改为通用表述（连接 / 断开重连外接屏）
- 中英两包同步更新，版本号 1.1（versionCode 2），签名与 v1.0 相同，可直接覆盖安装
- 主界面删除重复的「X Fold6 · 电脑模式」一行，只保留上方「需 vivo 电脑模式」提示
- 布局里对应的 TextView 一并移除，「OriginOS 6 已验证」上间距由 2dp 调到 10dp
