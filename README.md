# WATCH IT System

[![GitHub Repo](https://img.shields.io/badge/GitHub-Watch_it-blue?style=flat-square)](https://github.com/Fady2024/Watch_it)

WATCH IT is a robust media platform that allows users to explore movies and series, manage subscriptions, and enjoy interactive features. The system is built to ensure a seamless user experience with intuitive navigation, personalized content management, and advanced administrative capabilities.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [System Description](#system-description)
- [Pages](#pages)
  - [Login Page](#login-page)
  - [Sign-Up Page](#sign-up-page)
  - [Developer Page](#developer-page)
  - [Home Page](#home-page)
  - [Favorites Page](#favorites-page)
  - [Top Rated and Most Watched Pages](#top-rated-and-most-watched-pages)
  - [Payment Page](#payment-page)
  - [Subscription Page](#subscription-page)
  - [Account Page](#account-page)
  - [Details Page](#details-page)
  - [Movie Page](#movie-page)
  - [Sidebar](#sidebar)
  - [Video Player](#video-player)
- [Core Classes](#core-classes)
- [Installation](#installation)
- [Usage](#usage)
- [Team Members](#team-members)
- [Acknowledgments](#acknowledgments)
- [License](#license)

---

## Overview

WATCH IT is a multimedia platform designed to enhance user engagement with movies and series. The platform includes functionalities for exploring top-rated and trending shows, managing user accounts and subscriptions, and providing a user-friendly administrative interface.

---

## Key Features

- **Secure User Authentication**: Login and Sign-Up pages with validation and feedback.
- **Favorites Management**: Personalized lists of movies and series.
- **Dynamic Content Display**: Discover top-rated and trending content.
- **Subscription Management**: Flexible subscription plans with revenue tracking.
- **Interactive Video Playback**: Ad-free streaming with a robust commenting system.
- **Administrative Tools**: Content management with add, edit, and delete features.
- **Multi-language Support**: Switch seamlessly between languages.

---

## System Description

The WATCH IT system is composed of the following core functionalities:
1. **Login and Registration**: Secure user access.
2. **Content Browsing**: Explore movies, series, and personalized favorites.
3. **Subscriptions**: Manage plans and calculate revenue.
4. **Video Streaming**: Ad-free playback with comments.
5. **Administrative Management**: Edit content and track analytics.

---

## Pages

### Login Page
The gateway to the system, enabling secure user authentication.

**Key Features:**
- Password visibility toggle.
- Error handling with feedback.
- Direct redirection to the Home Page upon successful login.

---

### Sign-Up Page
An interactive page for new users to register and personalize their profiles.

**Key Features:**
- Password strength validation.
- Profile image upload with a default option.
- Clear validation messages for smooth registration.

---

### Developer Page
Showcases the development team and their roles in the project.

**Key Features:**
- Dynamic carousel of team members.
- Multi-language support for detailed task display.

---

### Home Page
A dynamic landing page displaying all available content.

**Key Features:**
- Lists recent and most-watched content.
- Admin functionalities for managing shows.

---

### Favorites Page
Allows users to manage their personalized favorite lists.

**Key Features:**
- Add, edit, or remove items seamlessly.
- Fetches user data from the backend for dynamic updates.

---

### Top Rated and Most Watched Pages
Discover the best and trending content.

**Top Rated Page**:
- Displays top-rated shows based on IMDb scores.
- Features an interactive grid layout.

**Most Watched Page**:
- Highlights shows with the highest view counts.

---

### Payment Page
Facilitates subscription payments.

**Key Features:**
- Input validation for card details.
- Displays selected subscription plan information.

---

### Subscription Page
Interactive subscription management with analytics.

**Key Features:**
- Plan selection and updates.
- Monthly revenue calculation.

---

### Account Page
A centralized page for managing user accounts.

**Key Features:**
- Update personal details.
- Profile photo upload.
- Password change with verification.

---

### Details Page
Provides information about directors and cast members.

**Key Features:**
- Displays associated shows for selected individuals.
- Navigation to main content pages.

---

### Movie Page
Comprehensive information about individual movies or shows.

**Key Features:**
- Display movie details, ratings, and descriptions.
- Interactive rating system with animated stars.

---

### Sidebar
A collapsible navigation menu.

**Key Features:**
- Quick access to key sections.
- Icons and text for both expanded and minimized states.

---

### Video Player
Stream videos and interact through comments.

**Key Features:**
- Ad-free video playback.
- Nested commenting system for discussions.

---

## Core Classes

- **Show**: Abstract base class for shared attributes.
- **Movie & Series**: Extended classes for movie and series-specific features.
- **Calculate_Rating**: Computes average ratings using IMDb scores and user feedback.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Fady2024/Watch_it.git
## Team Members

The WATCH IT system was developed by a dedicated team of students from **Ain Shams University**, Faculty of Computer and Information Science:

| **Name**                  | **Seat Number** | **Academic Level** | **Department** |
|---------------------------|-----------------|--------------------|----------------|
| **Fady Gerges Kodsy Al Sagheer**      | 2023170415      | 2                  | General        |
| **Mahmoud Ahmed Abdel Sadeq** | 5462023170   | 2                  | General        |
| **Salma Fawzy Ahmed** | 2023170262      | 2                  | General        |
| **Sara Emad Atta**        | 2023170254      | 2                  | General        |
| **Sandra Hany Samir Gabra**| 2023170256      | 2                  | General        |
| **Marwan Waleed Saleh**    | 2023170574      | 2                  | General        |

**Under Supervision:**
- **TA**: Hossam Sherif  
Faculty of Computer and Information Science, Ain Shams University (2023–2024).

---

## Acknowledgments

We would like to extend our heartfelt thanks to the following:

1. **Faculty of Computer and Information Science, Ain Shams University**  
   Thank you for providing us with the tools, resources, and support necessary to complete this project.

2. **Teaching Assistant: Hossam Sherif**  
   For their expert guidance, mentorship, and feedback throughout the development process.

3. **Team Members**  
   Thank you for your collaboration, dedication, and contributions to the success of the WATCH IT system.

Thank you to everyone who supported us in making this project a success!
