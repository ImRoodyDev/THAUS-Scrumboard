<div align="center" display="flex">
<img src="https://github.com/user-attachments/assets/57e208d8-08a5-479e-8b75-1153c2b14759" alt="Image" height="100"/>
<img src="https://github.com/user-attachments/assets/1891173a-1321-43ce-8e00-affdf127cdb3" alt="Image" width="400"/>
</div>

<h1 align="center">THAUS Scrumboard Managment Application</h1>

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
<video width="500" height="300" controls  src="https://github.com/user-attachments/assets/2a9c5c6c-edd3-4dd2-9516-ebccbc016100" > </video>

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

- [**Chatbox Server**](https://github.com/ImRoodyDev/THAUS-Scrumboard-Server.git) - Backend server component that
  handles data persistence, authentication, and real-time communication.

## 🔧 Troubleshooting

### Resolving JFoenix Compatibility Issues: Module Access Restrictions

If you encounter errors related to module access restrictions and JFoenix compatibility, you can resolve this by adding
specific JVM arguments to your runtime configuration.

#### Solution: Adding VM Arguments

Add the following JVM arguments to your runtime configuration:

```bash
--add-opens java.base/java.lang.reflect=com.jfoenix
--add-opens javafx.controls/javafx.scene.control=com.jfoenix
```

## 📝 License

This project is licensed under the Creative Commons Attribution-NonCommercial 4.0 International License - see
the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <sub>Built with ❤️ by your team name</sub>
</div>
