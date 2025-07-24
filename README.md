# **Software Portal Web Application**

This README provides instructions to set up and run the Software Portal Web Application, which consists of an Angular frontend and a Spring Boot backend.

## **Prerequisites**

Before you begin, ensure you have the following software installed on your system:

1. **Node.js (Version 16\) and npm:**  
   * Download and install Node.js (which includes npm) from the official Node.js website. Ensure you install version 16\.  
2. **Angular CLI (Version 15):**  
   npm install \-g @angular/cli@15

   This command requires Node.js and npm to be installed first.  
3. **IntelliJ IDEA:**  
   * Download and install IntelliJ IDEA from the official JetBrains website. This IDE will be used for the backend development.  
4. **Java Development Kit (JDK 17):**  
   * Download and install JDK 17 from Oracle or OpenJDK.  
5. **MySQL Server:**  
   * Install MySQL Server. You can download it from the official MySQL website. Ensure the server is running and accessible.

## **Project Setup**

Follow these steps to clone the project repository and set up both the frontend and backend.

### **1\. Clone the Project Repository**

Open your terminal or command prompt and clone the project:

git clone \<your-repository-url\>  
cd \<your-project-directory\>

**Note:** Replace \<your-repository-url\> with the actual URL of your Git repository and \<your-project-directory\> with the name of the cloned directory.

### **2\. Frontend Setup**

Navigate into the frontend directory of your cloned project and install dependencies.

cd frontend-folder-name \# Replace with your actual frontend folder name (e.g., 'frontend' or 'client')

Once in the frontend directory, install the necessary Node.js packages:

npm install

After installation, you can run the Angular development server:

ng serve \--open

This command will compile the Angular application, start a development server (usually on http://localhost:4200), and automatically open it in your default web browser.

### **3\. Backend Setup**

The backend is a Spring Boot application that uses Maven for dependency management.

1. **Open Project in IntelliJ IDEA:**  
   * Navigate to the backend folder (backend-folder-name or similar) in your project directory.  
   * Open IntelliJ IDEA.  
   * Select "Open" and choose the pom.xml file located in your backend folder. IntelliJ will then import the project.  
2. **Configure Database Credentials:**  
   * Locate the application.properties file within your backend project. Its common path is src/main/resources/application.properties.  
   * Open this file and update the database connection details to match your MySQL server credentials:

   spring.datasource.url=jdbc:mysql://localhost:3306/databaseName?useSSL=false  
     spring.datasource.username=root  
     spring.datasource.password=

   * **Important:**  
     * Replace databaseName with the actual name of your MySQL database.  
     * If you do not use a password for your MySQL root user, keep spring.datasource.password= empty as shown above.  
     * Ensure your MySQL server is running. 
     
3. **Configure Payement Gate way Credentials:** 
   * **Important:**  
      * Locate the application.properties file within your backend project. Its common path is src/main/resources/application.properties.  
      * Open this file and put this property:

      stripe.secretKey=sk_test_51QnhUaD6rzc0y8QfoRiWcx1XNMYcQXoVhbn1ME8kvBxrc9GYuYot8tReOvs4aYmIdQS7KffM5vr5qyQNzmB2Qzcq00WMIrMmq7

4. **Run the Backend Application:**  
   * Once the database configurations are updated, you can run the Spring Boot application directly from IntelliJ IDEA. Look for the main application class (usually ending with Application.java) and run it.

## **Usage**

After successfully setting up both the frontend and backend:

* The frontend (Angular) will be accessible in your web browser, typically at http://localhost:4200.  
* The backend (Spring Boot) will be running and handling API requests, usually on a port like 8081\.

You should now be able to interact with your Software Portal Web Application.