# HowsApp 💬

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-brightgreen)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A modern WhatsApp clone built with Jetpack Compose that showcases advanced Android development techniques.

<img src="assets/screenshot.png" width="300" alt="HowsApp Screenshot">

## Features ✨

### 🚀 Current Implementation
- **Authentication**: Complete login/signup flow with validation
- **Permissions**: Contact access, location handling
- **Navigation**: Multi-level routing system
- **Chat**: Recent chats screen, conversation UI
- **Profile**: View/edit profile with secure logout
- **Backend**: Robust API integration with error handling

### 🔜 Planned Features
| Category          | Features Planned |
|-------------------|------------------|
| UI Enhancements   | Animated chat bubbles, theme customization |
| Messaging        | Real-time chat, read receipts, typing indicators |
| Media            | Image/video sharing, document support |
| Calls            | Voice/video calling, call history |
| Security         | Passkey auth, E2E encryption |

## Tech Stack 🛠️

**Frontend**:
- Jetpack Compose
- Kotlin Coroutines
- ViewModel
- DataStore
- Navigation Compose

**Backend**: [See Backend Repo](https://github.com/Jhaempyre/backend)
- Node.js + Express
- MongoDB
- WebSockets
- JWT Authentication

## Getting Started 🏁

### Prerequisites
- Android Studio (latest)
- JDK 17+
- Android SDK (API 34+)

### Installation
```bash
git clone https://github.com/Jhaempyre/HowsApp.git
cd HowsApp

# Set up backend
git clone https://github.com/Jhaempyre/backend.git
cd backend && npm install && npm start

# Port forwarding for real device
adb reverse tcp:5000 tcp:5000



graph TD
    A[UI Layer] --> B[ViewModel]
    B --> C[Repository]
    C --> D[Local Data]
    C --> E[Remote Data]
    D --> F[DataStore]
    E --> G[API Client]



Contributing 🤝
We welcome contributions! Please follow these steps:

Fork the repository

Create your feature branch (git checkout -b feature/amazing-feature)

Commit your changes (git commit -m 'Add some feature')

Push to the branch (git push origin feature/amazing-feature)

Open a Pull Request

First time contributors are welcome! Look for good first issue labeled tickets.



Connect With Us 🌐
Have questions or suggestions? Open an issue or reach out to the maintainers!




### Key Features of This README:
1. **Shields/Badges** - Visual indicators for key project info
2. **Feature Comparison** - Clear table showing current vs planned features
3. **Visual Hierarchy** - Proper section organization with emojis
4. **Code Blocks** - For installation instructions
5. **Mermaid Diagram** - Visual architecture representation
6. **Contributing Guide** - Clear steps for new contributors
7. **Mobile-Friendly** - Proper formatting for GitHub mobile

### Recommended Additions:
1. Add actual screenshots in an `/assets` folder
2. Include a demo video/GIF
3. Add API documentation link if available
4. Include build status badges when CI is set up

This format presents all your information in a professional, organized manner while maintaining readability on both desktop and mobile GitHub interfaces.
