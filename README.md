<h1 align="center">🗨️ Chatbox</h1>

<p align="center">
  <b>Chatbox</b> is a Java-based desktop application designed to <b>simplify the SCRUM process</b> inside an organization. <br />
  It helps teams streamline agile workflows, sprint planning, and communication.
</p>

---

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

- **`HelloApplication.java`**  
  Main entry point of the JavaFX application. Responsible for launching the app.

- **`module-info.java`**  
  Defines the Java module system structure — specifies exported packages and required modules.

---

### 📂 `src/main/resources`

- **`components/`**  
  Resource files (like FXMLs) for modular UI components.

- **`css/`**  
  Stylesheets used for theming and customizing UI with JavaFX CSS.

- **`icons/`**  
  Icons used across the app — for menus, buttons, and indicators.

- **`images/`**  
  Image assets like logos, backgrounds, illustrations, etc.

- **`lib/`**  
  Third-party `.jar` libraries manually included in the project.

- **`views/`**  
  FXML layout files for full screens and views (e.g., dashboard, sprint board, etc.).

---

## 🚀 Getting Started

To build and run the project locally:

```bash
git clone https://github.com/your-username/chatbox.git
cd chatbox
./mvnw clean install
./mvnw javafx:run
```
