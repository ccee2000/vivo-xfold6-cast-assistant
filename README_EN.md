# X Fold6 Cast Assistant

A resolution switcher for wired external displays under vivo X Fold6 PC Mode.

> This is a personal hobby tool, built with AI assistance, mainly for my own vivo X Fold6. Shared as open source, no long-term maintenance guarantee.

## Introduction

In vivo X Fold6 PC Mode, an HDMI-connected external display is locked to a 2K render size by the system. This app uses Shizuku to run shell commands and switch the external display render size to 4K, or restore it back to 2K.

The panel itself is 3840×2160 @ 60Hz, but the system only renders 2560×1440 by default. This tool restores what gets cut.

### Screenshots

**App UI (English build)**

![App UI](./assets/04-app-ui-en.jpg)

**App UI (Chinese build)**

![App UI 中文版](./assets/01-app-ui.jpg)

**PC Mode on the external monitor (one 2K, one 4K)**

Same monitor (LECOO M3221PL, 3840×2160 @ 60Hz), same HDMI cable, one tap apart. These two images are the monitor's own output, so their pixel size is the render size — compare the sharpness of the desktop icons and text directly:

| 2K default (system override) | 4K Ultra (after switching) |
| --- | --- |
| ![External 2K](./assets/02-pc-mode-2k.jpg) | ![External 4K](./assets/03-pc-mode-4k.jpg) |
| Render 2560 × 1440 (app shows state 2K, overridden by system) | Render 3840 × 2160 (app shows state 4K, native) |

### Built-in guide

4 steps to set up Shizuku, all on the phone, no desktop ADB; entry point is "Help ›" on the main screen (「使用帮助 ›」 in the Chinese build).

## Compatibility

- Tested device: vivo X Fold6
- Tested OS: OriginOS 6
- Wired HDMI PC Mode only; wireless cast is not supported
- May stop working after system updates, e.g. upgrading to OriginOS 7

## Features

- One-tap switch: 4K Ultra (3840×2160) / Default 2K (2560×1440)
- Live status of the external display: physical resolution, current render size, state
- Built-in 4-step guide for Shizuku setup (wireless debugging only, no desktop ADB)
- After re-plugging HDMI the system falls back to 2K; tap "4K Ultra" to restore

## Usage

1. Install Shizuku (app store or shizuku.rikka.app)
2. Start the Shizuku service: Developer options → Wireless debugging → pair with code → "Start via wireless debugging" in Shizuku
3. Grant access: tap "4K Ultra" or "2K Default" and allow the Shizuku prompt (once only)
4. Verify: check whether the external display looks sharper; if it is still 2K, re-plug HDMI and tap again

The service started via wireless debugging stops after a reboot and has to be started again.

## Download

APKs are on the Releases page (v1.0). The Chinese and English builds are two separate packages that can be installed side by side; features are identical, only the UI language differs:

| Build | File | applicationId | UI language |
| --- | --- | --- | --- |
| Chinese | `XFold6_Cast_CN_v1.0.apk` | `com.test.virtual4k` | 中文 |
| English | `XFold6_Cast_EN_v1.0.apk` | `com.test.virtual4k.en` | English |

Direct links:

- Chinese: https://github.com/ccee2000/vivo-xfold6-cast-assistant/releases/download/v1.0/XFold6_Cast_CN_v1.0.apk
- English: https://github.com/ccee2000/vivo-xfold6-cast-assistant/releases/download/v1.0/XFold6_Cast_EN_v1.0.apk

## Notice

This app calls system display override APIs. It is not an official vivo feature and may break after major OS updates. For personal study and research use only.

## Author

- Hexini (Coolapk)
- Email: ccee2000@qq.com
- GitHub: https://github.com/ccee2000

## License

MIT
