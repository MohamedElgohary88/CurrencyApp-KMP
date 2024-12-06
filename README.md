# Currency Application 🌍💱  

This is a Kotlin Multiplatform project targeting Android and iOS.

https://www.linkedin.com/posts/mohamed-elgohary8_composemultiplatform-kotlin-crossplatformdevelopment-activity-7270734502451105792-CcNC?utm_source=share&utm_medium=member_desktop

## Project Structure 🏗  

* `/composeApp`  
  This directory contains code shared across your Compose Multiplatform applications.  
  It includes the following subfolders:  
  - `commonMain`: Contains code that is common for all platforms.  
  - `androidMain` and `iosMain`: Contain platform-specific Kotlin code.  
    For example, platform APIs like CoreCrypto for iOS should be placed in `iosMain`.  

* `/iosApp`  
  This directory contains the iOS application entry point.  
  Even if you're sharing UI with Compose Multiplatform, you still need this entry point for the iOS app.  
  You can also add SwiftUI or other iOS-specific code in this folder.

* `/androidApp`  
  This directory contains the Android application entry point.  
  Compose Multiplatform integrates seamlessly with Android, and this is where Android-specific configuration resides.

## Features ✨  

- Cross-Platform UI: Shared UI using Compose Multiplatform.  
- Real-Time Data: Fetches currency exchange rates from an API.  
- Local Caching: Uses Realm for offline data storage.  
- Automatic Updates: Updates cached data when it becomes outdated.  
- Animations: Smooth UI transitions built with Compose.  

## Getting Started 🚀  

### Clone the Repository  
```bash
git clone 
cd currency-application
