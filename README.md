# Online Reservation System

## Project Overview
The Online Reservation System is a Java-based desktop application that allows users to book and cancel train tickets through an interactive graphical interface.

## Features
- User login authentication
- Interactive graphical user interface using Java Swing
- Train reservation form
- Train details and journey information
- Automatic PNR generation
- Ticket booking confirmation
- Ticket cancellation using PNR
- SQLite database integration
- Input validation

## Technologies Used
- **Java:** Core application logic and functionality
- **Java Swing:** Graphical user interface
- **SQLite:** Database for storing user, train and reservation information
- **JDBC:** Connecting Java with SQLite
- **VS Code:** Development environment

## Project Structure
- `src/` – Java source files
- `lib/` – SQLite JDBC driver
- `database/` – SQLite database
- `screenshots/` – Application screenshots

## How to Run

### Requirements
- Java JDK 17 or above
- SQLite JDBC Driver

### Compile
```bash
javac -cp "lib/*" -d out src/*.java
```

### Run
**Windows PowerShell:**
```powershell
java -cp "out;lib/*" Main
```

## Default Login
- Username: `admin`
- Password: `admin123`

## Project Type
Oasis Infobyte Java Development Internship – Task 1

## Author
Tajeshvi Singh
