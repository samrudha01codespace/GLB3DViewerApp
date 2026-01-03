# Contributing to GLB 3D Viewer App

First off, thank you for considering contributing to GLB 3D Viewer App! 🎉

It's people like you that make GLB 3D Viewer App such a great tool. We welcome contributions from everyone and are grateful for even the smallest of fixes!

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Getting Started](#getting-started)
- [Development Process](#development-process)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Community](#community)

## 📜 Code of Conduct

This project and everyone participating in it is governed by our commitment to providing a welcoming and inclusive experience for everyone. By participating, you are expected to uphold these values:

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## 🤝 How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates. When creating a bug report, include as many details as possible:

**Bug Report Template:**
```markdown
**Describe the bug**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected behavior**
A clear and concise description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Device Information:**
 - Device: [e.g. Pixel 6]
 - Android Version: [e.g. Android 13]
 - App Version: [e.g. 1.0]

**Additional context**
Add any other context about the problem here.
```

### Suggesting Features

Feature suggestions are welcome! Please provide:

- **Clear description** of the feature
- **Use case**: Why is this feature needed?
- **Proposed implementation** (if you have ideas)
- **Alternatives considered**
- **Mockups or examples** (if applicable)

### Code Contributions

1. **Bug Fixes**: Always welcome!
2. **New Features**: Please open an issue first to discuss
3. **Documentation**: Improvements to README, code comments, etc.
4. **Tests**: Adding or improving test coverage
5. **Performance**: Optimization and improvements

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest version)
- JDK 11 or higher
- Git
- Basic knowledge of Kotlin and Jetpack Compose

### Setting Up Development Environment

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/GLB3DViewerApp.git
   cd GLB3DViewerApp
   ```

3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/ORIGINAL_OWNER/GLB3DViewerApp.git
   ```

4. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

5. **Build the project**:
   ```bash
   ./gradlew build
   ```

6. **Run tests**:
   ```bash
   ./gradlew test
   ```

## 💻 Development Process

### Branching Strategy

- `main` - Production-ready code
- `develop` - Development branch (base for features)
- `feature/*` - New features
- `bugfix/*` - Bug fixes
- `hotfix/*` - Urgent fixes for production

### Creating a New Branch

```bash
# Update your local repository
git checkout develop
git pull upstream develop

# Create a new feature branch
git checkout -b feature/your-feature-name
```

### Making Changes

1. **Write clean code** following Kotlin conventions
2. **Add tests** for new features
3. **Update documentation** if needed
4. **Test thoroughly** on multiple devices/API levels
5. **Commit regularly** with clear messages

## 📝 Coding Standards

### Kotlin Style Guide

Follow the [official Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

- Use 4 spaces for indentation
- Use camelCase for functions and variables
- Use PascalCase for classes
- Use meaningful names
- Keep functions small and focused
- Add KDoc comments for public APIs

### Example Code Style

```kotlin
/**
 * Loads a GLB model from the specified URI.
 *
 * @param uri The URI of the GLB file
 * @return ModelData containing the loaded model
 * @throws ModelLoadException if the model cannot be loaded
 */
suspend fun loadModel(uri: Uri): ModelData {
    return withContext(Dispatchers.IO) {
        // Implementation
    }
}
```

### Compose Guidelines

```kotlin
@Composable
fun ModelCard(
    model: ModelEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        // Composable content
    }
}
```

### Project Structure

Keep files organized:
```
com.samrudha.glb3dviewerapp/
├── Database/          # Database entities, DAOs
├── Design/            # UI components
├── MainViewModel/     # ViewModels and state
├── ui/theme/          # Theme and styling
└── MainActivity.kt    # Entry point
```

## 📋 Commit Guidelines

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, etc.)
- **refactor**: Code refactoring
- **test**: Adding or updating tests
- **chore**: Build process or auxiliary tool changes

### Examples

```bash
feat(viewer): add lighting intensity control

Added a slider to control the intensity of the 3D scene lighting.
Users can now adjust brightness for better model visibility.

Closes #42
```

```bash
fix(auth): resolve login crash on Android 13

Fixed a NullPointerException that occurred during login
on Android 13 devices due to uninitialized ViewModel.

Fixes #38
```

## 🔄 Pull Request Process

### Before Submitting

- [ ] Code follows the style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings
- [ ] Tests added/updated and passing
- [ ] Works on multiple devices/API levels

### Submitting a Pull Request

1. **Update your branch**:
   ```bash
   git checkout develop
   git pull upstream develop
   git checkout feature/your-feature
   git rebase develop
   ```

2. **Push to your fork**:
   ```bash
   git push origin feature/your-feature
   ```

3. **Create Pull Request** on GitHub:
   - Use a clear, descriptive title
   - Reference related issues
   - Describe changes made
   - Add screenshots/videos if UI changes
   - Request review from maintainers

### Pull Request Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Related Issues
Fixes #(issue number)

## Screenshots
(if applicable)

## Testing
- [ ] Tested on Android 13
- [ ] Tested on Android 14
- [ ] Unit tests pass
- [ ] Manual testing completed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Comments added
- [ ] Documentation updated
- [ ] No new warnings
- [ ] Tests added
```

### Review Process

- Maintainers will review your PR
- Address feedback promptly
- Make requested changes
- Once approved, your PR will be merged!

## 🏆 Recognition

Contributors will be:
- Added to the Contributors section
- Mentioned in release notes
- Given credit in the project

## 🎯 Areas We Need Help

Current priorities:
- [ ] UI/UX improvements
- [ ] Performance optimization
- [ ] Test coverage
- [ ] Documentation
- [ ] Bug fixes
- [ ] Accessibility features
- [ ] Internationalization

## 📚 Resources

### Learning Resources
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Google Filament Documentation](https://google.github.io/filament/)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)

### Tools
- [Android Studio](https://developer.android.com/studio)
- [Kotlin Playground](https://play.kotlinlang.org/)
- [Material Design Guidelines](https://m3.material.io/)

## 💬 Community

### Getting Help

- **GitHub Issues**: For bugs and feature requests
- **Discussions**: For questions and general discussion
- **Email**: [your.email@example.com]

### Stay Connected

- Star ⭐ the repository
- Watch for updates
- Follow on social media

## ❓ Questions?

Don't hesitate to ask! We're here to help:
- Open an issue with the `question` label
- Reach out via email
- Ask in discussions

---

Thank you for contributing! 🎉 Every contribution, no matter how small, is valued and appreciated!

**Happy Coding!** 💻✨

