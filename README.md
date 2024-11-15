# Pet Care Management App

This Pet Care Management app is a mobile application developed in **Android Studio** to help pet owners find veterinarians, manage pet-related services, and enhance the overall user experience with location-based functionalities.

## Features

### 1. Sign Up
- The **Sign Up page** allows users to create an account by filling in their basic information, including:
  - **Name**
  - **Email**
  - **Password**
  - **Phone Number**
  - **User Role** (Veterinarian or Normal User)
- **All fields are mandatory** for the user to successfully sign up. Upon successful registration, users can log in to explore the app's features.

### 2. Login
- The **Login page** provides users with secure access to the app. Users enter their email and password to log in.
- Once logged in, users are taken to the main interface with access to all app functionalities.

### 3. Navigation Sidebar
- The app features a **sidebar menu** that allows easy navigation to various parts of the app. Users can:
  - **Home** - View the home screen with general information.
  - **Vet List** - Browse a list of available veterinarians and view details.
  - **Vet Map** - Find veterinarians on a map with real-time directions.
  - **Food** - Access information about pet food management.
  - **Service** - Explore additional pet care services.
  - **Animal** - Manage information about pets.

### 4. Explore the UI
- Each section of the app provides a tailored user interface for seamless interaction and navigation.
- **Veterinarian List and Map**: Browse veterinarians in a list format and click to view the route to the selected vet using Google Maps.
- **Interactive Map Display**: The route to veterinarians is displayed on a map with real-time directions, enhancing the accessibility of the service.

## Technologies Used

- **Android Studio**: Primary IDE for development.
- **Room Database**: For efficient local data storage.
- **Google Maps and Directions API**: To provide route-finding and location services.
- **Express.js**: For implementing authentication features.

## Getting Started

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/pet-care-management-app.git

## Setup Instructions

1. **Import the Project**: Open **Android Studio**, go to **File > Open**, and select the project directory to import it.

2. **Google API Setup**:
   - Enable **Google Maps** and **Google Directions API** in your **Google Cloud Console**.
   - Generate an **API key** and add it to the appropriate configuration files in the project.

3. **Run the App**:
   - Build and run the app in Android Studio to start exploring the Pet Care Management application.

## Screenshots

- **Sign Up Page**: Allows users to register by providing their basic information (all fields are required).
- **Login Page**: Provides secure login for all users.
- **Sidebar Navigation**: Enables users to access various parts of the app through an intuitive sidebar.
- **Vet List and Map**: Users can browse veterinarians and view directions to their location.

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

