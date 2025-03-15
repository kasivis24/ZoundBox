# ZoundBox

# Music App

## Overview
This music app provides a seamless and feature-rich experience, allowing users to manage playlists, play songs, search via cloud storage, and customize themes. Built with modern Android technologies, the app ensures smooth performance and a user-friendly interface.

## Features

### 🎵 Playlist Management
- The **Playlist** screen utilizes **Room Database** for efficient playlist management.
- Upon app restart, the **Playlist** page serves as the **default landing screen**, ensuring a smooth user experience.

### 🎶 Songs Library
- Retrieves all **locally stored audio files** and extracts metadata using the **MediaStore** class.
- Requests necessary permissions before displaying songs.
- Songs are formatted and displayed as a structured list.
- Users can **add songs to custom playlists** for better organization.

### ⭐ Favorites
- The **Favorites** screen retrieves songs from the **Favorites table**, allowing users to manage their favorite tracks effortlessly.
- Users can **add or remove** songs from favorites directly within the screen.
- The **bottom mini-player** and **player page** provide seamless access to favorite songs.

### 🎛️ Music Player
The **Player Page** enables users to:
- **Play/Pause** songs
- **Shuffle** playlists
- **Mark** songs as favorites
- **Seek** through the song's timeline

#### Powered by **ExoPlayer (Media3)** for:
- Seamless playback of songs, playlists, and favorites
- Advanced playback controls and media handling
- Efficient resource management for smooth performance

### 🔍 Cloud-Based Search & Admin Panel
- The **search functionality** retrieves songs stored in **Firebase Cloud Storage**.
- Developed an **Admin Panel (webpage)** to upload songs to Firebase.
- When a song is uploaded, its metadata is stored in **Firebase Realtime Database (JSON format)**.
- Utilizes **Retrofit and Gson** to fetch and parse song data efficiently in an **MVVM ViewModel** architecture.

#### Technologies Used:
- **Firebase Cloud Storage** – For storing songs
- **Firebase Realtime Database** – For metadata storage
- **Admin Panel (Webpage)** – For song uploads
- **Retrofit & Gson** – For API integration and data retrieval
- **MVVM Architecture** – For structured data handling
- **Room Database** – Managed globally via the **Application class**
- **Google Voice Recognition** – For voice-based song search

### 🎤 Voice Search
- Integrated **Google Voice Recognition** for hands-free song search.
- Implemented logic to process voice commands effectively.

### 🎨 Theme Switching
- Users can seamlessly **switch between light and dark mode** for a personalized experience.
- The **theme switch toggle** is integrated into the **Playlist screen**.
- A **bottom sheet** dialog appears when the switch is pressed, allowing theme selection.
- The selected theme is **persistently stored** to maintain user preferences.
- The **UI dynamically adapts** to the selected theme, ensuring a smooth transition.

## Technologies Used
- **Jetpack Compose** – For UI development
- **Room Database** – For local storage
- **MVVM Architecture** – For structured data flow
- **ExoPlayer (Media3)** – For smooth audio playback
- **Firebase (Cloud Storage & Realtime Database)** – For cloud song storage and metadata
- **Retrofit & Gson** – For API communication
- **Google Voice Recognition** – For voice-based search

## Screenshots
(Attach images related to each screen here)

## Installation
1. Clone the repository:
   ```bash
   git clone  https://github.com/kasivis24/ZoundBox.git
   ```
2. Open in **Android Studio**.
3. Sync Gradle and build the project.
4. Run the app on an emulator or physical device.

## Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request.

## License
This project is licensed under the **MIT License**.

---

Feel free to customize the repository name and add relevant images!

