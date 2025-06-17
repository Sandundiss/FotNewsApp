# FOT News App

![App Logo](app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)  
*A news application for Faculty of Technology, University of Colombo*

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Firebase Setup](#firebase-setup)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [License](#license)

## Features
- **User Authentication** (Signup/Login)
- **News Categories** (Sports, Academic, FOT Events)
- **Profile Management**
- **Developer Information Section**
- **Responsive UI** with Bottom Navigation

## Prerequisites
- Android Studio (Latest Version)
- JDK 17+
- Android SDK 33+
- Firebase Account

## Installation
1. Clone the repository:
   ```bash

   Create a new Firebase project at console.firebase.google.com

Add Android app with package name: com.example.fotnewsapp

Download google-services.json and place in:

text
/app/google-services.json
Enable these Firebase services:

Authentication (Email/Password)

Realtime Database

(Optional) Firebase Storage

Project Structure
text
/app
├── /src/main
│   ├── /java/com/example/fotnewsapp
│   │   ├── MainActivity.java         # Main screen with news categories
│   │   ├── LoginActivity.java        # User login
│   │   ├── SignupActivity.java       # User registration
│   │   ├── ProfileActivity.java      # User profile
│   │   └── fragments/                # News category fragments
│   └── /res
│       ├── /layout                   # All XML layouts
│       ├── /values                   # Strings, colors, styles
│       └── /drawable                 # Images and vectors

Firebase Database Structure
json
{
  "users": {
    "user1_uid": {
      "username": "john_doe",
      "email": "john@example.com"
    }
  },
  "news": {
    "sports": [
      {
        "title": "Inter-Faculty Championship",
        "date": "2023-07-15"
      }
    ]
  }
}
Dependencies
gradle
dependencies {
    implementation 'com.google.firebase:firebase-auth:22.1.2'
    implementation 'com.google.firebase:firebase-database:20.2.2'
    implementation 'androidx.fragment:fragment:1.5.5'
    implementation 'com.google.android.material:material:1.9.0'
}
Troubleshooting
"App keeps stopping": Check Logcat for specific errors

Firebase connection issues: Verify google-services.json is in correct location

Authentication failures: Ensure Email/Password provider is enabled in Firebase Console

License
text
MIT License
Copyright (c) 2023 [Sandun Dissanayake]
Contributors
Sandun Dissanayake

text

To use this README:
1. Save as `README.md` in your project root
2. Replace placeholder images with actual screenshots
3. Update the contributors section
4. Customize any sections as needed

Would you like me to add any additional sections like:
- Development roadmap
- API documentation
- Testing guidelines
- Deployment instructions?
