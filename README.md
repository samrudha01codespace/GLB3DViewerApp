# 🎨 GLB 3D Viewer App

<div align="center">
  
  ![Android](https://img.shields.io/badge/Platform-Android-green.svg)
  ![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
  ![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)
  ![API](https://img.shields.io/badge/Min%20API-33-orange.svg)
  ![License](https://img.shields.io/badge/License-MIT-yellow.svg)
  
  **A modern Android application for viewing and managing GLB/GLTF 3D models with authentication and role-based access control.**

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Screenshots](#-screenshots)
- [Getting Started](#-getting-started)
- [Installation](#-installation)
- [Project Structure](#-project-structure)
- [Dependencies](#-dependencies)
- [Usage](#-usage)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## 🌟 Overview

GLB 3D Viewer App is a powerful Android application built with Jetpack Compose and Google Filament that allows users to visualize, manage, and interact with 3D models in GLB/GLTF format. The app features a robust authentication system with role-based access control, making it perfect for teams collaborating on 3D content.

---

## ✨ Features

### 🔐 **Authentication System**
- User registration and login
- Role-based access control (Admin/User)
- Secure password storage with encryption
- Persistent authentication state

### 🎭 **3D Model Viewing**
- High-quality GLB/GLTF model rendering using Google Filament
- Interactive 3D viewport with touch controls
- Adjustable lighting intensity
- Real-time model manipulation (rotation, zoom, pan)
- Smooth animations and transitions

### 📦 **Model Management**
- Add new 3D models from device storage
- Grid view of all models with thumbnails
- Edit model information
- Delete models (with role-based permissions)
- Local database storage for model metadata

### 🎨 **Modern UI/UX**
- Material Design 3 (Material You)
- Beautiful glassmorphism effects using Haze library
- Dark/Light theme support
- Responsive layouts
- Bottom sheet controls
- Smooth animations

### 🔒 **Security**
- Encrypted data storage
- Role-based access control
- Secure user authentication

---

## 🛠️ Tech Stack

### **Core Technologies**
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Minimum SDK:** API 33 (Android 13)
- **Target SDK:** API 36
- **Build System:** Gradle (Kotlin DSL)

### **Key Libraries**

#### 3D Rendering
- **Google Filament** (v1.68.2) - High-performance 3D rendering engine
- **GLTF I/O** - GLB/GLTF model loading and parsing
- **Filament Utils** - Utility functions for Filament

#### UI & Design
- **Jetpack Compose** - Modern declarative UI
- **Material 3** - Material Design components
- **Haze** (v0.7.2) - Glassmorphism blur effects
- **Coil** (v2.5.0) - Image loading library

#### Architecture & Data
- **Room Database** (v2.8.4) - Local data persistence
- **Kotlin Coroutines** - Asynchronous programming
- **DataStore** - Key-value storage
- **ViewModel** - UI state management
- **Lifecycle Components** - Lifecycle-aware components

#### Tools
- **KSP** (Kotlin Symbol Processing) - Annotation processing

---

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern with clean architecture principles:

```
┌─────────────────────────────────────────┐
│              UI Layer                    │
│   (Jetpack Compose + Material 3)        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         ViewModel Layer                  │
│   (State Management + Business Logic)   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        Repository Layer                  │
│      (Data Source Abstraction)          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│          Data Layer                      │
│  (Room Database + File System + 3D)     │
└─────────────────────────────────────────┘
```

### **Key Components**

- **MainActivity**: Entry point and composition root
- **MainViewModel**: Manages app state and business logic
- **GLBModelViewer**: Handles 3D model rendering with Filament
- **AppRepo**: Repository for data operations
- **Database Layer**: Room database with DAO interfaces
- **UI Components**: Reusable Compose screens and components

---

## 📸 Screenshots

> Add your app screenshots here to showcase the UI and features

```
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   Login     │  │   3D View   │  │   Gallery   │
│   Screen    │  │   Screen    │  │   Screen    │
└─────────────┘  └─────────────┘  └─────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio** (Ladybug | 2024.2.1 or later)
- **JDK 11** or higher
- **Android SDK** with API 33+
- **Git**

### System Requirements

- **OS:** Windows, macOS, or Linux
- **RAM:** 8GB minimum (16GB recommended)
- **Storage:** 4GB free space

---

## 📥 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/GLB3DViewerApp.git
cd GLB3DViewerApp
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select **"Open an Existing Project"**
3. Navigate to the cloned repository
4. Wait for Gradle sync to complete

### 3. Configure the Project

Update `local.properties` with your Android SDK path:

```properties
sdk.dir=/path/to/your/android/sdk
```

### 4. Build the Project

```bash
./gradlew build
```

Or use Android Studio's **Build > Make Project** (Ctrl+F9)

### 5. Run the App

- Connect an Android device or start an emulator (API 33+)
- Click the **Run** button (Shift+F10) or run:

```bash
./gradlew installDebug
```

---

## 📁 Project Structure

```
GLB3DViewerApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/samrudha/glb3dviewerapp/
│   │   │   │   ├── Database/
│   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   ├── DAO.kt
│   │   │   │   │   └── Entity.kt
│   │   │   │   ├── Design/
│   │   │   │   │   ├── Login.kt
│   │   │   │   │   ├── MainUI.kt
│   │   │   │   │   └── UI_Constant.kt
│   │   │   │   ├── MainViewModel/
│   │   │   │   │   ├── AuthState.kt
│   │   │   │   │   ├── CryptoManager.kt
│   │   │   │   │   ├── MainViewModel.kt
│   │   │   │   │   └── Roles.kt
│   │   │   │   ├── ui/theme/
│   │   │   │   │   └── Theme files
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── mipmap/
│   │   │   │   ├── values/
│   │   │   │   └── xml/
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

---

## 📦 Dependencies

### Core Android Libraries
```kotlin
androidx.core:core-ktx
androidx.lifecycle:lifecycle-runtime-ktx
androidx.activity:activity-compose
```

### Jetpack Compose
```kotlin
androidx.compose:compose-bom
androidx.compose.ui:ui
androidx.compose.ui:ui-graphics
androidx.compose.ui:ui-tooling-preview
androidx.compose.material3:material3
androidx.compose.material:material-icons-extended
```

### 3D Rendering (Filament)
```kotlin
com.google.android.filament:filament-android:1.68.2
com.google.android.filament:gltfio-android:1.68.2
com.google.android.filament:filament-utils-android:1.68.2
```

### Database (Room)
```kotlin
androidx.room:room-runtime:2.8.4
androidx.room:room-compiler:2.8.4 // KSP
```

### UI Effects & Image Loading
```kotlin
dev.chrisbanes.haze:haze:0.7.2
dev.chrisbanes.haze:haze-materials:0.7.2
io.coil-kt:coil-compose:2.5.0
```

### Data Storage
```kotlin
androidx.datastore:datastore-core
```

---

## 💡 Usage

### First Time Setup

1. **Launch the App**
2. **Register an Account**
   - Choose your role (User/Admin)
   - Enter email and password
   - Click "Register"

3. **Login**
   - Enter your credentials
   - Access the main dashboard

### Adding 3D Models

1. Click the **"Add Model"** button
2. Select a GLB/GLTF file from your device
3. The model will be added to your library with a thumbnail

### Viewing Models

1. Tap on any model thumbnail in the gallery
2. The 3D viewer will open
3. Use touch gestures to interact:
   - **Rotate:** Drag with one finger
   - **Zoom:** Pinch gesture
   - **Pan:** Drag with two fingers
4. Adjust lighting using the slider

### Managing Models

- **Edit:** Click the edit icon to modify model details
- **Delete:** Admin users can delete models (requires admin role)

### User Roles

- **User:** Can view and add models
- **Admin:** Full access including delete permissions

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

### How to Contribute

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features

### Reporting Issues

Found a bug or have a suggestion? Please open an issue with:
- Clear description
- Steps to reproduce (for bugs)
- Expected vs actual behavior
- Screenshots (if applicable)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 Samrudha

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 👨‍💻 Author

**Samrudha**

- GitHub: [@samrudha01codespace](https://github.com/samrudha01codespace)
- LinkedIn: [Samrudha Kshirsagar](https://linkedin.com/in/samrudha-kshirsagar)
- Email: samrudhakshirsagar@gmail.com

---

## 🙏 Acknowledgments

- **Google Filament Team** - For the amazing 3D rendering engine
- **Android Jetpack Team** - For Compose and modern Android libraries
- **Chris Banes** - For the Haze library
- **Open Source Community** - For inspiration and resources

---

## 🗺️ Roadmap

### Planned Features

- [ ] Cloud storage integration
- [ ] Model animation playback
- [ ] AR (Augmented Reality) mode
- [ ] Model sharing between users
- [ ] Export rendered images
- [ ] Material editor
- [ ] Multiple light sources
- [ ] Model statistics and info
- [ ] Search and filter functionality
- [ ] Batch model import

---

## 📊 Project Stats

- **Language:** Kotlin
- **Build Tool:** Gradle
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM
- **Min Android Version:** Android 13 (API 33)

---

## 🐛 Known Issues

- None reported yet

---

## 📝 Changelog

### Version 1.0 (Current)
- Initial release
- Basic 3D model viewing
- User authentication
- Role-based access control
- Model management (add, edit, delete)
- Local database storage

---

## 💬 Support

If you have any questions or need help, feel free to:

1. Open an issue on GitHub
2. Contact via email
3. Join our community discussions

---

## ⭐ Star History

If you find this project useful, please consider giving it a star! ⭐

---

<div align="center">

**Made with ❤️ and Kotlin**

[⬆ Back to Top](#-glb-3d-viewer-app)

</div>

