# Jetpack Weather App

An Android weather app built with **Jetpack Compose**, **MVVM architecture**, **StateFlow**, and **Coroutines**.

Users can view current weather based on their location and manage a list of favorite locations with weather alerts.  
The app also includes various settings like language change, unit preferences, notification toggle, and selecting your location from a map.

---
## 📽️ Demo Video

🎥 Watch demo [here](https://drive.google.com/file/d/1-hNpz8ahLSGfazFppMPeoERhW1ccujZV/view?usp=sharing).

---
## Tech Stack

- **Jetpack Compose** – Modern Android UI toolkit
- **MVVM Architecture** – Separation of concerns
- **Kotlin Coroutines** – Asynchronous programming
- **StateFlow & SharedFlow** – Reactive state management
- **WorkManager** – Background weather alert scheduling
- **Room** – Local storage of favorite locations and alerts
- **Google Maps API** – Select your location from a map
- **Content Provider** – to expose weather alerts 
- **openweathermap API** 
---

## ✨ Features

-  **Current Location Weather**
-  **Favorite Locations List**
-  **Weather Alerts via Notifications**
-  **Add Weather Alerts to calendar**
-  **App Settings:**
  - Change language (English / Arabic)
  - Switch temperature (Celsius / Fahrenheit / Kelvin) & wind units
  - Enable/Disable weather alert notifications
  - Choose your current location from a map
    

---

## 📷 Screenshots

<img src="https://github.com/user-attachments/assets/31b3b760-123c-41e8-8d43-c0a06c4bcece" alt="screens" width="1000" height="1000" />

---

##  Testing

- **Unit tests** for repositories and ViewModels using `MockK` and `Turbine`
- **Coroutines test APIs** for testing `StateFlow` and suspend functions

## Setup Instructions
git clone https://github.com/MasameEh/WeatherForecast_App.git

- Open with Android Studio.

- Add your API key(s) in the appropriate local.properties or config files:

    - For weather API
    - For Google Maps
- Build & Run!


