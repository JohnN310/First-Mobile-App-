# Spotify Wrapped Interactive App

This project is an interactive app that uses Spotify API to pull users' top music tracks and artists based on a given time frame and rank them. There is also functionality for playing mini games that are correlated to the Spotify Statistics. 

## Features

- **Spotify Authentication**: Users log in with their Spotify account to grant access to their listening data.
- **Top Tracks & Artists**: View your top tracks and artists for different time frames (All Time, 6 Months, 4 Weeks, Top 5/10/15).
- **Genre Analysis**: Analyze your top genres and get AI-generated insights.
- **Recommendations**: Get personalized music recommendations based on your top tracks and artists.
- **Mini Games & Quizzes**: Play drag-and-drop quizzes to guess your top tracks/artists order.
- **Seasonal & Special Day Themes**: The app changes background themes for holidays and special dates (e.g., New Year's, Valentine's Day, Halloween).
- **Save Wrapped**: Save your personalized Spotify Wrapped summary.
- **Dark Mode**: Toggle between light and dark themes.
- **Profile & Settings**: Manage your profile and app settings.

## Main Screens

- **HomePage**: Entry point for authentication and navigation.
- **SpotifyApiHelperActivitySongs**: Displays top tracks and related options.
- **SpotifyApiHelperActivityArtists**: Displays top artists and related options.
- **SpotifyApiHelperActivityRecommendations**: Shows recommended tracks/artists.
- **Quiz Activities**: Interactive quizzes for ranking tracks/artists.

## Usage

1. **Authenticate**: Log in with Spotify to fetch your data.
2. **Explore Stats**: Use the options menu to view top tracks/artists for different time frames.
3. **Play Games**: Try quizzes to test your memory of your top tracks/artists.
4. **Get Recommendations**: Discover new music based on your listening habits.
5. **Analyze Genres**: Get AI-powered genre analysis.
6. **Save & Share**: Save your Wrapped summary and share with friends.

## Technologies

- **Java** (Android)
- **Spotify Web API**
- **OkHttp** for network requests
- **RecyclerView** for interactive lists
- **Google Gemini API** for genre analysis (AI)
- **Custom UI** with seasonal backgrounds

## Project Structure

- `app/src/main/java/com/example/spotifytest/`: Main app logic and activities.
- `app/src/main/res/layout/`: XML layouts for screens.
- `app/src/main/res/drawable/`: Backgrounds and button images.

## Getting Started

1. Clone the repo.
2. Open in Android Studio.
3. Add your Spotify API credentials.
4. Build and run on an Android device.

---

Enjoy your personalized Spotify Wrapped experience with interactive features and games!