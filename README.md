# X Fold6 Cast Assistant

> vivo X Fold6 电脑模式有线外接屏分辨率切换工具

English: [README_EN.md](./README_EN.md)

本项目只是个人业余自用工具，借助 AI 辅助开发，优先适配我自己的 vivo X Fold6，开源分享，不保证长期维护。

## 介绍

vivo X Fold6 原生电脑模式下，HDMI 有线外接显示器会被系统压到 2K 渲染。本应用通过 Shizuku 以 shell 身份执行系统命令，把外接屏渲染分辨率切到 4K，或者还原回 2K。

外接屏物理分辨率 3840×2160 @ 60Hz，系统默认只给 2560×1440 渲染，本工具就是把被压掉的那部分补回来。

### 界面预览

**App 界面（手机端，中文版）**

![App 界面](./assets/01-app-ui.jpg)

**App 界面（英文版）**

![App 界面 英文版](./assets/04-app-ui-en.jpg)

**外接显示器电脑模式（2K / 4K 各一张）**

同一台显示器（LECOO M3221PL，3840×2160 @ 60Hz）、同一条 HDMI 线，只差按一下按钮。下面两张是外接屏自己输出的画面，尺寸即渲染分辨率，可直接对比桌面图标与文字的清晰度差异：

| 2K 默认（被系统覆盖压制） | 4K 超清（切换后） |
| --- | --- |
| ![外接屏 2K](./assets/02-pc-mode-2k.jpg) | ![外接屏 4K](./assets/03-pc-mode-4k.jpg) |
| 渲染 2560 × 1440（App 显示：状态 2K，被系统分辨率覆盖压制） | 渲染 3840 × 2160（App 显示：状态 4K，原生物理分辨率） |

### 内置使用帮助

4 步配置 Shizuku，全部在手机上完成，不需要电脑 ADB，入口在主界面「使用帮助」。

## 适配说明

- 测试设备：vivo X Fold6
- 测试系统：OriginOS 6（已实测通过）
- 仅支持有线 HDMI + vivo 电脑模式，无线投屏不生效
- 系统升级后可能失效，如：升级 OriginOS 7

## 功能

- 一键切换：4K 超清（3840×2160）/ 2K 默认（2560×1440）
- 主界面实时显示外接屏物理分辨率、当前渲染分辨率与状态
- 内置完整使用帮助（4 步配置 Shizuku，仅无线调试方式，不需要电脑 ADB）
- 重新插拔 HDMI 后系统会把分辨率压回 2K，点「4K 超清」即可恢复

## 使用步骤

1. 安装 Shizuku（应用商店或 shizuku.rikka.app）
2. 启动 Shizuku 服务：设置 → 开发者选项 → 打开「无线调试」→ 用配对码配对 → Shizuku 里「通过无线调试启动」
3. 授权本应用：点「4K 超清」或「2K 默认」，Shizuku 弹窗点「允许」（只需一次）
4. 确认生效：回到电脑模式观察外接屏是否变清晰，若仍是 2K，重新插拔 HDMI 后再点一次

重启手机后，用无线调试启动的服务会失效，需要重新执行第 2 步。

## 下载

APK 见本仓库 Releases 页（v1.0）。中文版、英文版是两个独立包，可以同时安装、互不影响，功能完全一致，只是界面语言不同：

| 版本 | 文件 | 包名 | 界面语言 |
| --- | --- | --- | --- |
| 中文版 | `XFold6_Cast_CN_v1.0.apk` | `com.test.virtual4k` | 中文 |
| 英文版 | `XFold6_Cast_EN_v1.0.apk` | `com.test.virtual4k.en` | English |

直链：

- 中文版：https://github.com/ccee2000/vivo-xfold6-cast-assistant/releases/download/v1.0/XFold6_Cast_CN_v1.0.apk
- 英文版：https://github.com/ccee2000/vivo-xfold6-cast-assistant/releases/download/v1.0/XFold6_Cast_EN_v1.0.apk

## 说明

本程序调用系统显示覆盖接口，非 vivo 官方功能，系统大版本升级后可能失效。仅用于个人学习研究。

## 作者

- 河西尼（酷安）
- 邮箱：ccee2000@qq.com
- GitHub：https://github.com/ccee2000

## License

MIT
