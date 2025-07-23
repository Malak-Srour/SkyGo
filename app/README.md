# SkyGo ✈️

A comprehensive flight booking mobile application built with Java and Firebase for Android. SkyGo provides users with an intuitive platform to search, book, and manage flight reservations while offering administrators complete control over flight management and user oversight.

## 📱 App Overview

SkyGo is a full-featured flight booking application that allows users to search for flights, select seats, and complete reservations with ease. The app includes both user and administrative interfaces, making it a complete solution for flight booking management.

## ✨ Key Features

### 👤 User Features
- **User Authentication** - Secure signup and login system
- **Flight Search** - Search flights by origin, destination, passenger count, and class
- **Passenger Management** - Support for adults and children with different pricing
- **Class Selection** - Choose between Economy, Business, and First Class
- **Real-time Seat Selection** - Interactive seat map with availability status
- **Visual Seat Status**:
    - 🔴 **Red** - Unavailable/Taken seats
    - ⚪ **Gray** - Available seats
    - 🟢 **Green** - Selected seats
- **Booking Confirmation** - Complete flight details and ticket generation
- **Ticket Management** - Screenshot and email sharing functionality
- **Reservation History** - View and manage personal bookings

### 👨‍💼 Admin Features
- **Admin Authentication** - Secure admin login with specific credentials
- **Flight Management** - Add, edit, and delete flights
- **User Management** - View and manage registered users
- **Reservation Oversight** - Monitor all user reservations
- **System Administration** - Complete control over app data

## 🏗️ Project Structure

\`\`\`
app/
├── manifests/
│   └── AndroidManifest.xml
├── java/com/example/skygo/
│   ├── activities/
│   │   ├── AddFlightActivity.java
│   │   ├── AdminDashboardActivity.java
│   │   ├── AdminLoginActivity.java
│   │   ├── ConfirmationActivity.java
│   │   ├── EditFlightActivity.java
│   │   ├── FlightResultsActivity.java
│   │   ├── LoginActivity.java
│   │   ├── MainActivity.java
│   │   ├── ManageReservationsActivity.java
│   │   ├── ManageUsersActivity.java
│   │   ├── RoleSelectionActivity.java
│   │   ├── SeatSelectionActivity.java
│   │   └── SignupActivity.java
│   ├── adapters/
│   │   ├── FlightAdapter.java
│   │   ├── ReservationAdapter.java
│   │   ├── SeatAdapter.java
│   │   └── UserAdapter.java
│   └── models/
│       ├── Flight.java
│       ├── Reservation.java
│       ├── Seat.java
│       ├── Ticket.java
│       └── User.java
└── res/
├── drawable/
│   ├── background_sky_go.png
│   ├── button_background.xml
│   ├── button_outline_background.xml
│   ├── class_badge_background.xml
│   ├── counter_background.xml
│   ├── counter_button_background.xml
│   ├── edittext_background.xml
│   ├── ic_launcher_background.xml
│   ├── ic_launcher_foreground.xml
│   ├── search_criteria_background.xml
│   └── spinner_background.xml
└── layout/
├── activity_add_flight.xml
├── activity_admin_dashboard.xml
├── activity_admin_login.xml
├── activity_confirmation.xml
├── activity_edit_flight.xml
├── activity_flight_results.xml
├── activity_login.xml
├── activity_main.xml
├── activity_manage_reservations.xml
├── activity_manage_users.xml
├── activity_role_selection.xml
├── activity_seat_selection.xml
├── activity_signup.xml
├── item_flight.xml
├── item_reservation.xml
├── item_seat.xml
└── item_user.xml
\`\`\`

## 🛠️ Technology Stack

- **Platform**: Android
- **Language**: Java
- **Database**: Firebase Firestore
- **Authentication**: Firebase Authentication
- **IDE**: Android Studio
- **UI**: XML Layouts with Material Design principles

## 📋 Prerequisites

- **Android Studio** (Latest version recommended)
- **Java Development Kit (JDK)** 8 or higher
- **Android SDK** (API level 21 or higher)
- **Firebase Project** with Firestore and Authentication enabled
- **Google Services** configuration file (google-services.json)

## 🚀 Installation & Setup

### 1. Clone the Repository
\`\`\`bash
git clone https://github.com/yourusername/skygo.git
cd skygo
\`\`\`

### 2. Open in Android Studio
1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the cloned repository folder
4. Click "OK" to open the project

### 3. Firebase Configuration
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app to your Firebase project
3. Download the \`google-services.json\` file
4. Place it in the \`app/\` directory of your project
5. Enable **Firestore Database** and **Authentication** in Firebase Console

### 4. Build and Run
1. Connect an Android device or start an emulator
2. Click the "Run" button in Android Studio
3. The app will build and install on your device

## 📱 User Journey

### For Regular Users

1. **Registration/Login**
    - New users sign up with email and password
    - Existing users log in with credentials

2. **Flight Search**
    - Enter origin and destination cities
    - Select travel dates
    - Choose number of adult and child passengers
    - Select flight class (Economy/Business/First)

3. **Flight Selection**
    - Browse available flights matching criteria
    - View flight details (time, duration, price)
    - Select preferred flight

4. **Seat Selection**
    - View interactive seat map
    - Select seats for all passengers
    - Avoid taken (red) seats
    - Confirm available (gray) seats turn green when selected

5. **Booking Confirmation**
    - Review complete booking details
    - Enter passenger email information
    - Complete reservation

6. **Ticket Management**
    - Screenshot ticket for offline access
    - Share ticket via email with travel companions

### For Administrators

1. **Admin Login**
    - Access admin panel with special credentials
    - Navigate to admin dashboard

2. **Flight Management**
    - Add new flights with complete details
    - Edit existing flight information
    - Delete flights as needed

3. **User Management**
    - View all registered users
    - Remove spam or problematic accounts

4. **Reservation Monitoring**
    - View all user reservations
    - Monitor booking patterns and statistics

## 🎨 UI/UX Features

- **Modern Design** - Clean, intuitive interface with sky-themed branding
- **Responsive Layouts** - Optimized for various Android screen sizes
- **Visual Feedback** - Color-coded seat selection and status indicators
- **Custom Drawables** - Professional UI elements and backgrounds
- **Material Design** - Following Android design guidelines

## 🔐 Security Features

- **Firebase Authentication** - Secure user authentication system
- **Admin Access Control** - Restricted admin functionality
- **Data Validation** - Input validation and error handling
- **Secure Database** - Firebase Firestore security rules

## 📊 Data Models

### User
- User ID, Email, Password
- Personal information
- Booking history

### Flight
- Flight ID, Origin, Destination
- Departure/Arrival times
- Available seats, Pricing
- Aircraft information

### Reservation
- Reservation ID, User ID, Flight ID
- Passenger details
- Seat assignments, Total cost
- Booking timestamp

### Seat
- Seat number, Availability status
- Class type, Position

## 🔧 Configuration

### Firebase Setup
1. **Authentication**: Enable Email/Password authentication
2. **Firestore**: Set up collections for users, flights, reservations
3. **Security Rules**: Configure appropriate read/write permissions

### Admin Credentials
- Set up admin email and password in Firebase Authentication
- Configure admin role identification in the app

## 🐛 Troubleshooting

### Common Issues

**Build Errors**
- Ensure \`google-services.json\` is in the correct location
- Check Firebase dependencies in \`build.gradle\`
- Verify Android SDK and build tools are up to date

**Authentication Issues**
- Verify Firebase Authentication is enabled
- Check internet connectivity
- Ensure email format validation

**Database Connection**
- Confirm Firestore is enabled in Firebase Console
- Check security rules allow read/write operations
- Verify network permissions in AndroidManifest.xml

## 📈 Future Enhancements

- **Payment Integration** - Add payment gateway for real transactions
- **Push Notifications** - Flight updates and booking confirmations
- **Offline Mode** - Cache data for offline viewing
- **Multi-language Support** - Internationalization
- **Advanced Search** - Filters for airlines, stops, price ranges
- **User Profiles** - Enhanced user management and preferences

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (\`git checkout -b feature/AmazingFeature\`)
3. Commit your changes (\`git commit -m 'Add some AmazingFeature'\`)
4. Push to the branch (\`git push origin feature/AmazingFeature\`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

## 🙏 Acknowledgments

- Firebase for backend services
- Android development community
- Material Design guidelines
- Open source libraries used in the project

---

**SkyGo** - Making flight booking simple and efficient ✈️

*Built with ❤️ for travelers everywhere*
