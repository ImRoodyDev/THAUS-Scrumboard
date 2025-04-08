<h1 align="center">🗨️ Chatbox</h1>

<p align="center">
  <b>Chatbox</b> is a Java-based desktop application designed to <b>simplify the SCRUM process</b> inside an organization. <br />
  It provides tools and an interface to streamline team communication, sprint tracking, and agile workflows.
</p>

---

## 📦 Project Structure

This project follows the standard Maven directory structure, which separates Java source code from resources.

<details>
  <summary><b>▶️ <code>src/main/java/com.thaus.chatbox</code></b></summary>

- 📁 <b>animation</b>  
  Contains UI animations to enhance the user experience, likely JavaFX-based effects.

- 📁 <b>base</b>  
  Abstract classes and foundational elements shared across the app.

- 📁 <b>classes</b>  
  General Java classes, often used for core business logic or data modeling.

- 📁 <b>components</b>  
  Reusable UI components and elements, such as buttons, panels, or dialogs.

- 📁 <b>controllers</b>  
  MVC controllers managing user interactions and bridging logic between views and models.

- 📁 <b>utils</b>  
  Utility classes — helpful functions, converters, or static methods used throughout the app.

- 🧩 <b>HelloApplication.java</b>  
  The main entry point of the application. Initializes and starts the JavaFX application.

- 📦 <b>module-info.java</b>  
  Java module system definition, declaring dependencies and package exports.
</details>

<details>
  <summary><b>▶️ <code>src/main/resources</code></b></summary>

- 📁 <b>components</b>  
  FXML files or config assets related to reusable UI pieces.

- 📁 <b>css</b>  
  Stylesheets that define the UI’s look and feel (themes, layouts, colors).

- 📁 <b>icons</b>  
  SVG or PNG icons used for UI elements like buttons, menus, and headers.

- 📁 <b>images</b>  
  Other image assets (e.g., logos, illustrations, etc.).

- 📁 <b>lib</b>  
  Third-party libraries (usually `.jar` files) that are bundled with the project.

- 📁 <b>views</b>  
  FXML files or other UI layout definitions used for various screens/views in the app.
</details>

---

## 🚀 Getting Started

To run the project locally:

```bash
git clone https://github.com/your-username/chatbox.git
cd chatbox
./mvnw clean install
./mvnw javafx:run
