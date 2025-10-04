# 🎮 NAYP - A Personal Game Catalog CLI

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apache-maven)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite)
![RAWG API](https://img.shields.io/badge/RAWG_API-000000?style=for-the-badge)

## 📖 About The Project

GameLog is a robust console application (CLI) developed in Java that functions as a "Pokédex" for your video game collection. The application allows you to search for any existing game using the public RAWG API, save its details to a local SQLite database, and manage your personal collection with different statuses.

This project was built from scratch with a focus on software architecture best practices, including a layered structure, separation of concerns, and a robust data persistence layer.

## ✨ Features

* **Game Search:** Search a catalog of over 500,000 games in real-time using the RAWG API.
* **Personal Catalog:** Save games to your local collection, which persists in an SQLite database.
* **Status Management:** Assign a status to each saved game (e.g., "Want to Play", "Playing", "Completed", etc.).
* **Full CRUD Functionality:** Complete features to Create, Read, Update (the status), and Delete games from your collection.
* **Filter by Status:** Easily view your collection by filtering for a specific status.
* **Polished Console Interface:** A clean and robust user experience with clear menus and input error handling.

## 🛠️ Tech Stack

* **Language:** Java 17+
* **Dependency Manager:** Apache Maven
* **Database:** SQLite with JDBC driver
* **HTTP Client:** OkHttp
* **JSON Parsing:** Google Gson
* **Secrets Management:** Dotenv-java

## 🚀 Getting Started

Follow the steps below to run the project locally.

### Prerequisites

You will need to have the following software installed on your machine:
* JDK (Java Development Kit) - Version 17 or higher.
* Apache Maven - Version 3.8 or higher.

### Setup

1.  **Clone the repo:**
    ```sh
    git clone [https://github.com/MatheusFrazatto/NAYP---Game_Log_CLI.git](https://github.com/MatheusFrazatto/NAYP---Game_Log_CLI.git)
    ```
2.  **Navigate to the project folder:**
    ```sh
    cd NAYP---Game_Log_CLI
    ```
3.  **Create the environment file:**
    * In the root of the project, create a file named `.env`.
    * Inside this file, add your RAWG API key in the following format:
        ```
        RAWG_API_KEY="YOUR_API_KEY_HERE"
        ```

### Running the Application

1.  **Compile the project:**
    Maven will download all the necessary dependencies and compile the code.
    ```sh
    mvn compile
    ```
2.  **Run the application:**
    ```sh
    mvn exec:java -Dexec.mainClass="br.com.matheus.nayp.Main"
    ```

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.
