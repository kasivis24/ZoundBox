# Music App

## Overview
This music app provides a seamless and feature-rich experience, allowing users to manage playlists, play songs, search via cloud storage, and customize themes. Built with modern Android technologies, the app ensures smooth performance and a user-friendly interface.


## Screenshots


![IMG-20250315-WA0002](https://github.com/user-attachments/assets/ba7df487-c540-4edd-9af7-1cfc312640d9)
![IMG-20250315-WA0003](https://github.com/user-attachments/assets/d059ff04-e51e-4e61-b2e2-7a7cf41d049c)
![IMG-20250315-WA0011](https://github.com/user-attachments/assets/0b3f7749-8c82-4f05-896a-a9fb036166a8)
![IMG-20250315-WA0004](https://github.com/user-attachments/assets/a18402a5-8941-4c7c-b947-6a7fad4aa90d)
![IMG-20250315-WA0006](https://github.com/user-attachments/assets/90b89ed4-e327-45a0-a33b-b93e303be0ea)
![IMG-20250315-WA0007](https://github.com/user-attachments/assets/dabae672-be5c-4c75-a585-2b42b05c4d42)
![IMG-20250315-WA0008](https://github.com/user-attachments/assets/2ab8f2c4-03f8-4ebb-8ba4-74331dcaf7df)
![IMG-20250315-WA0009](https://github.com/user-attachments/assets/921f2142-65ed-4553-8344-dbdc1400a78d)
![IMG-20250315-WA0010](https://github.com/user-attachments/assets/86346c28-53a6-44a8-83f0-9c22ec787c20)
![IMG-20250315-WA0011](https://github.com/user-attachments/assets/0e5ec1a6-48c7-4a9d-9087-37446029891c)




## Features

### 🎵 Playlist Management
- The Playlist screen utilizes Room Database for efficient playlist management.
- Upon app restart, the Playlist page serves as the default landing screen, ensuring a smooth user experience.

### 🎶 Songs Library
- Retrieves all locally stored audio files and extracts metadata using the MediaStore class.
- Requests necessary permissions before displaying songs.
- Songs are formatted and displayed as a structured list.
- Users can add songs to custom playlists for better organization.

### ⭐ Favorites
- The Favorites screen retrieves songs from the Favorites table, allowing users to manage their favorite tracks effortlessly.
- Users can add or remove songs from favorites directly within the screen.
- The bottom mini-player and player page provide seamless access to favorite songs.

### 🎛️ Music Player
- The Player Page enables users to:
  - Play/Pause songs
  - Shuffle playlists
  - Mark songs as favorites
  - Seek through the song's timeline
- Powered by ExoPlayer (Media3) for:
  - Seamless playback of songs, playlists, and favorites
  - Advanced playback controls and media handling
  - Efficient resource management for smooth performance

### 🔍 Cloud-Based Search & Admin Panel
- The search functionality retrieves songs stored in Firebase Cloud Storage.
- Developed an Admin Panel (webpage) to upload songs to Firebase.
- When a song is uploaded, its metadata is stored in Firebase Realtime Database (JSON format).
- Utilizes Retrofit and Gson to fetch and parse song data efficiently in an MVVM ViewModel architecture.

#### Technologies Used:
- **Firebase Cloud Storage** – For storing songs
- **Firebase Realtime Database** – For metadata storage
- **Admin Panel (Webpage)** – For song uploads
- **Retrofit & Gson** – For API integration and data retrieval
- **MVVM Architecture** – For structured data handling
- **Room Database** – Managed globally via the Application class
- **Google Voice Recognition** – For voice-based song search

### 🎤 Voice Search
- Integrated Google Voice Recognition for hands-free song search.
- Implemented logic to process voice commands effectively.

### 🎨 Theme Switching
- Users can seamlessly switch between light and dark mode for a personalized experience.
- The theme switch toggle is integrated into the Playlist screen.
- A bottom sheet dialog appears when the switch is pressed, allowing theme selection.
- The selected theme is persistently stored to maintain user preferences.
- The UI dynamically adapts to the selected theme, ensuring a smooth transition.

## Technologies Used
- **Jetpack Compose** – For UI development
- **Room Database** – For local storage
- **MVVM Architecture** – For structured data flow
- **ExoPlayer (Media3)** – For smooth audio playback
- **Firebase (Cloud Storage & Realtime Database)** – For cloud song storage and metadata
- **Retrofit & Gson** – For API communication
- **Google Voice Recognition** – For voice-based search


## Installation

### Clone the repository:
```sh
git clone https://github.com/kasivis24/ZoundBox.git
```

### Open in Android Studio:
- Sync Gradle and build the project.
- Run the app on an emulator or physical device.

## APK Download
[Download APK](app-debug.apk)

## Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request.

## License
This project is licensed under the MIT License.

