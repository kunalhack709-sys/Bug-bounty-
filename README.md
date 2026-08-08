# Bug Bounty Companion 🛡️

**Bug Bounty Companion** is an AI-powered mobile security research methodology assistant and Termux CLI reference guide built for Android using Kotlin and Jetpack Compose.

---

## 🌟 Features

- 📖 **Termux & Mobile Pentesting Reference Guide**: Quick access to essential terminal commands, tools, and methodologies (Nmap, Metasploit, SQLMap, Wireshark, Burp Suite, ADB, Objection, Frida, etc.).
- 🤖 **AI Assistant (Gemini Powered)**: Interactive security assistant to answer vulnerability questions, analyze payload ideas, and guide security assessments.
- 💾 **Local Notes & Findings Storage**: Room Database persistence to store bug bounty notes, terminal command snippets, and target vulnerability logs locally on your device.
- 🎨 **Modern Material 3 UI**: Clean, high-contrast UI with dark mode support, smooth transitions, and intuitive screen navigation.

---

## 🏗️ Technical Stack

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture principles
- **Asynchronous State**: Kotlin Coroutines & `StateFlow`
- **Database**: Room Database with KSP
- **Build Tool**: Gradle (Kotlin DSL `.gradle.kts`) with Version Catalog (`libs.versions.toml`)
- **CI/CD**: GitHub Actions (`.github/workflows/android.yml`)

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (Ladybug / Jellyfish or newer recommended)
- **JDK 21**
- **Android SDK** (API Level 35 compileSdk, API Level 24+ minSdk)

### Building the App

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/bug-bounty-companion.git
   cd bug-bounty-companion
   ```

2. **Build Debug APK using Gradle Wrapper**:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
   The generated APK file will be located at:
   `app/build/outputs/apk/debug/app-debug.apk`

3. **Run Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

## 🤖 GitHub Actions Workflow

This repository includes a pre-configured CI pipeline in `.github/workflows/android.yml`.

Every push or pull request to `main` or `master` branches will automatically:
1. Set up **JDK 21** and **Gradle setup action**.
2. Run unit tests (`./gradlew testDebugUnitTest`).
3. Build the debug APK (`./gradlew assembleDebug`).
4. Upload `app-debug.apk` as a downloadable GitHub build artifact.

---

## 📄 License

This project is intended for educational purposes and authorized security research only. Always obtain permission before testing targets.
