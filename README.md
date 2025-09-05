# Spotify Wrapped Interactive App

This project is an interactive app that uses Spotify API to pull users' top music tracks and artists based on a given time frame and rank them. There is also functionality for playing mini games that are correlated to the Spotify Statistics. 

## Features

- **Spotify Authentication**: Users log in with their Spotify account to grant access to their listening data.
- **Top Tracks & Artists**: View your top tracks and artists for different time frames (All Time, 6 Months, 4 Weeks, Top 5/10/15).
- **Mini Games & Quizzes**: Play drag-and-drop quizzes to guess your top tracks/artists order.
- **Seasonal & Special Day Themes**: The app changes background themes for holidays and special dates (e.g., New Year's, Valentine's Day, Halloween).
- **Save Wrapped**: Save your personalized Spotify Wrapped summary.
- **Profile & Settings**: Manage your profile and app settings.

## Main Screens

- **HomePage**: Entry point for authentication and navigation.
- **SpotifyApiHelperActivitySongs**: Displays top tracks and related options.
- **SpotifyApiHelperActivityArtists**: Displays top artists and related options.
- **SpotifyApiHelperActivityRecommendations**: Shows recommended artists.
- **Quiz Activities**: Interactive quizzes for ranking tracks/artists.

## Usage

1. **Authenticate**: Log in with Spotify to fetch your data.
2. **Explore Stats**: Use the options menu to view top tracks/artists for different time frames.
3. **Play Games**: Try quizzes to test your memory of your top tracks/artists.
4. **Save & Share**: Save your Wrapped summary and share with friends.

## Technologies

- **Java** (Android)
- **Spotify Web API**
- **OkHttp** for network requests
- **RecyclerView** for interactive lists
- **Custom UI** with seasonal backgrounds

## Project Structure

- `app/src/main/java/com/example/spotifytest/`: Main app logic and activities.
- `app/src/main/res/layout/`: XML layouts for screens.
- `app/src/main/res/drawable/`: Backgrounds and button images.

## Backend Service: FastAPI + SQLite

To support account management and user data persistence, this project includes a **FastAPI backend** with **SQLite**.

### Features
- **User Management**: Create, retrieve, delete users.  
- **Password Updates**: Update user passwords.  
- **Top Tracks Storage**: Save and retrieve users’ top tracks.  

### API Endpoints
- **POST** `/users/` → Create a new user.  
- **GET** `/users/{username}` → Retrieve a user’s details.  
- **PUT** `/users/{username}/password` → Update a user’s password.  
- **PUT** `/users/{username}/toptracks` → Save/update a user’s top tracks.  
- **GET** `/users/{username}/toptracks` → Retrieve a user’s top tracks.  
- **DELETE** `/users/{username}` → Delete a user.  

### Tech Stack
- **FastAPI** for building REST API endpoints.  
- **SQLite** for local database storage.  
- **Pydantic** for data validation.  

### File
- `fastapi_spotify_backend.py` → Contains all API logic, database setup, and helper functions.  

## Getting Started

1. Clone the repo.
2. Open in Android Studio.
3. Add your Spotify API credentials.
4. Build and run on an Android device.
5. Start the backend service: uvicorn fastapi_spotify_backend:app --reload
6. Use the backend to manage user accounts and top tracks.

---

Enjoy your personalized Spotify Wrapped experience with interactive features and games!
