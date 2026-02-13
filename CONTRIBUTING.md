## Build Environments & Configuration

To build the project with a specific environment, pass the `app.env` property in your terminal:

* **Build for Development:**
  ```bash
  ./gradlew :sample:composeApp:assembleDebug -Papp.env=dev
   ```
  
* **Build for Testing:**
  ```bash
  ./gradlew :sample:composeApp:assembleDebug -Papp.env=test
   ```
  
* **Build for Production:**
  ```bash
  ./gradlew :sample:composeApp:assembleDebug -Papp.env=prod
   ```