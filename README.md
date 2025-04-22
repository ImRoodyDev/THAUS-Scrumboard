<h1 align="center">🗨️ THAUS Scrumboard Managment Application</h1>

<p align="center">
  <b>Scrumboard Managment</b> is a Java-based desktop application designed to <b>simplify the SCRUM process</b> inside an organization. <br />
  It helps teams streamline agile workflows, sprint planning, and communication.
</p>

<div align="center">
  
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
  ![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
  ![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
  
</div>

---

## 🎬 Demo Video

<div align="center">
    <!-- Option 1: GitHub-hosted video (if your video is in your repo) -->
  ![Demo Video](path/to/your/video.mp4)
  
  <!-- Option 2: Link with thumbnail to YouTube -->
  [![Chatbox Demo](https://img.youtube.com/vi/YOUR_VIDEO_ID/0.jpg)](https://www.youtube.com/watch?v=YOUR_VIDEO_ID)
  
  <!-- Option 3: Animated GIF -->
  <!-- ![Demo](path/to/demo.gif) -->
 </div>

## 📁 Project Structure

This project follows a standard Maven project layout. Here's a breakdown of the key folders and their purposes:

### 📂 `src/main/java/com.thaus.chatbox`

- **`animation/`**  
  Contains JavaFX animations and effects to improve the visual interaction of the application.

- **`base/`**  
  Core or abstract classes shared throughout the app — useful for common behaviors and logic.

- **`classes/`**  
  Domain-related Java classes — likely includes models or plain Java objects (POJOs).

- **`components/`**  
  Contains modular, reusable Java components used across various UI parts of the application.

- **`controllers/`**  
  MVC controller classes — handles events, interactions, and communication between views and logic.

- **`utils/`**  
  Utility and helper classes — might include constants, formatters, and general-purpose methods.

- **`App.js`**  
  Main entry point of the JavaFX application. Responsible for launching the app.

- **`module-info.java`**  
  Defines the Java module system structure — specifies exported packages and required modules.

### 📂 `src/main/resources`

- **`components/`**  
  Resource files (like FXMLs) for modular UI components.

- **`css/`**  
  Stylesheets used for theming and customizing UI with JavaFX CSS.

- **`fonts/`**  
  Custom fonts used within the application.

- **`images/`**  
  Image assets like logos, backgrounds, illustrations, etc.

- **`lib/`**  
  Third-party `.jar` libraries manually included in the project.

- **`components/views/`**  
  FXML layout files for full screens and views (e.g., dashboard, sprint board, etc.).

## 🌟 Features

- **Agile Workflow Management**: Easily manage sprints, user stories, and tasks
- **Team Communication**: Built-in chat for team discussions
- **Sprint Planning**: Tools for planning and tracking sprints
- **User-Friendly Interface**: Clean, intuitive JavaFX-based UI
- **Cross-Platform**: Works on Windows, macOS, and Linux

## 🚀 Getting Started

To build and run the project locally:

```bash
git clone https://github.com/your-username/chatbox.git
cd chatbox
./mvnw clean install
./mvnw javafx:run
```

## 🔗 Related Repositories

This project works in conjunction with a backend server:

- [**Chatbox Server**](https://github.com/ImRoodyDev/THAUS-Scrumboard-Server.git) - Backend server component that handles data persistence, authentication, and real-time communication.

## 🔧 Troubleshooting

### Resolving JFoenix Compatibility Issues: Module Access Restrictions

If you encounter errors related to module access restrictions and JFoenix compatibility, you can resolve this by adding specific JVM arguments to your runtime configuration.

#### Solution: Adding VM Arguments

Add the following JVM arguments to your runtime configuration:

```bash
--add-opens java.base/java.lang.reflect=com.jfoenix
--add-opens javafx.controls/javafx.scene.control=com.jfoenix
```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <sub>Built with ❤️ by your team name</sub>
</div>
