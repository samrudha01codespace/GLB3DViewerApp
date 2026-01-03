# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned Features
- Cloud storage integration for models
- AR (Augmented Reality) viewing mode
- Model animation playback
- Share models between users
- Export rendered images/screenshots
- Material and texture editor
- Multiple light source support
- Model statistics and metadata viewer
- Search and filter in model library
- Batch import functionality

## [1.0.0] - 2026-01-04

### Added
- Initial release of GLB 3D Viewer App
- 3D model rendering using Google Filament
- GLB/GLTF file format support
- User authentication system with login/register
- Role-based access control (Admin/User)
- Encrypted password storage
- Local database with Room for data persistence
- Model management features:
  - Add models from device storage
  - View models in interactive 3D viewport
  - Edit model information
  - Delete models (admin only)
- Model library with grid view
- Thumbnail generation for models
- Interactive 3D controls:
  - Rotate with one finger drag
  - Zoom with pinch gesture
  - Pan with two finger drag
- Adjustable lighting intensity with slider
- Material Design 3 (Material You) UI
- Glassmorphism effects using Haze library
- Bottom sheet for model controls
- Persistent user authentication state
- Edge-to-edge display support
- Smooth animations and transitions
- Jetpack Compose UI throughout
- MVVM architecture implementation
- Kotlin Coroutines for async operations
- Lifecycle-aware components

### Technical Details
- **Minimum SDK**: Android 13 (API 33)
- **Target SDK**: Android 14 (API 36)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **3D Engine**: Google Filament 1.68.2
- **Database**: Room 2.8.4
- **Architecture**: MVVM with Clean Architecture principles

### Dependencies
- androidx.core:core-ktx
- androidx.lifecycle:lifecycle-runtime-ktx
- androidx.activity:activity-compose
- androidx.compose.bom (Material 3)
- com.google.android.filament:filament-android:1.68.2
- com.google.android.filament:gltfio-android:1.68.2
- com.google.android.filament:filament-utils-android:1.68.2
- dev.chrisbanes.haze:haze:0.7.2
- androidx.room:room-runtime:2.8.4
- io.coil-kt:coil-compose:2.5.0

### Security
- Encrypted user credentials storage
- Role-based access control implementation
- Secure authentication flow

## [0.1.0] - Development Phase

### Development Milestones
- Project initialization
- Setup Gradle build configuration
- Implemented basic project structure
- Created database schema
- Developed authentication system
- Integrated Google Filament
- Built UI components with Compose
- Implemented 3D viewer functionality
- Added model management features
- Tested on multiple devices

---

## Version History

### Version Numbering

We use Semantic Versioning: MAJOR.MINOR.PATCH

- **MAJOR**: Incompatible API changes
- **MINOR**: New functionality (backwards compatible)
- **PATCH**: Bug fixes (backwards compatible)

### Release Types

- **[Unreleased]**: Changes in development
- **[Major Release]**: Significant new features
- **[Minor Release]**: Feature additions
- **[Patch Release]**: Bug fixes and improvements

---

## How to Read This Changelog

### Change Categories

- **Added**: New features
- **Changed**: Changes in existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Removed features
- **Fixed**: Bug fixes
- **Security**: Security updates

---

## Future Roadmap

### Version 1.1.0 (Planned)
- Model search functionality
- Filter models by name/date
- Model import improvements
- Performance optimizations
- Bug fixes from user feedback

### Version 1.2.0 (Planned)
- Cloud storage integration
- User model sharing
- Model categories/tags
- Enhanced thumbnail generation

### Version 2.0.0 (Planned)
- AR viewing mode
- Animation playback
- Material editor
- Advanced lighting controls
- Model export features

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute to this project.

## Support

For questions and support:
- Open an issue on GitHub
- Check existing issues and discussions
- Read the documentation

---

**Note**: This changelog is manually maintained. For a complete list of changes, see the [commit history](https://github.com/yourusername/GLB3DViewerApp/commits/main).

[Unreleased]: https://github.com/yourusername/GLB3DViewerApp/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/yourusername/GLB3DViewerApp/releases/tag/v1.0.0
[0.1.0]: https://github.com/yourusername/GLB3DViewerApp/releases/tag/v0.1.0

