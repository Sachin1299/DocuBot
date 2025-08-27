# 📄 DocuBot – AI-Powered Document Assistant  

[![Java](https://img.shields.io/badge/Java-17-red?logo=java)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-green?logo=springboot)](https://spring.io/projects/spring-boot)  
[![React](https://img.shields.io/badge/React-Frontend-blue?logo=react)](https://reactjs.org/)  
[![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)](https://www.mysql.com/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)  

---

DocuBot is a **ChatGPT-style document assistant** built with **React.js (frontend)** and **Spring Boot (backend)**.  
It allows users to **upload documents (PDF, DOCX, TXT)**, ask questions, and get **AI-generated contextual answers**.  

This project demonstrates **full-stack Java + React development**, including secure authentication, REST APIs, file handling, and a modern UI/UX.  

---

## ✨ Features  

- 📂 **File Upload Support** – Upload `.pdf`, `.docx`, or `.txt` files  
- 💬 **Document Q&A** – Ask contextual questions based on file contents  
- 🧾 **File Metadata Preview** – Display filename, size, and metadata  
- 🟢 **Secure Authentication**  
  - JWT authentication for local signup/login  
  - Google OAuth2 login for federated access  
  - Logout clears JWT and redirects to login  
- 🔒 **HTTPS Enabled** – Backend secured with self-signed certificates  
- 🎨 **Modern Chat UI**  
  - Single-page React interface (Bootstrap-based)  
  - Chat bubbles with typing indicators  
  - Dark mode toggle 🌙  
  - File chip preview with metadata  
- ⚡ **Session Memory (WIP)** – Maintain chat history per uploaded document  
- 📜 **Streaming Responses (Planned)** – Stream AI answers in real-time  

---

## 🏗️ Tech Stack  

### 🔹 Frontend  
- React.js (Hooks, Functional Components)  
- Bootstrap 5 + Custom CSS  
- Axios (API communication)  

### 🔹 Backend  
- Java 17  
- Spring Boot (REST APIs, Security, Data JPA)  
- JWT authentication  
- Google OAuth2 integration  
- MySQL database  

### 🔹 Infrastructure & Tools  
- Docker (containerization)  
- Maven (dependency management & build)  
- HTTPS with self-signed certificates  
- GitHub for version control  

---

## 📸 Screenshots  

- 🔑 Login page (JWT + Google OAuth2)
  <img width="1167" height="590" alt="image" src="https://github.com/user-attachments/assets/49946810-068e-44b4-b4e3-9c6685b685b5" />

- 📝 Signup page
  <img width="1157" height="618" alt="image" src="https://github.com/user-attachments/assets/ab3aa861-0af1-47bb-a329-0bc2219cda4d" />
  
- 💬 Chat UI with file upload + responses
  <img width="1358" height="364" alt="image" src="https://github.com/user-attachments/assets/0744b165-f5e2-413f-b39c-4a86dc5c28a3" />
  
  <img width="1343" height="625" alt="image" src="https://github.com/user-attachments/assets/fec9d00f-2e8e-4e9e-828e-f4ad9820d728" />

---

## ⚙️ Setup & Installation  

### 🔹 Backend (Spring Boot)  

```bash
git clone https://github.com/your-username/DocuBot.git
cd DocuBot/backend
```

#### Update application.properties:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/docubot
spring.datasource.username=root
spring.datasource.password=yourpassword

jwt.secret=your-secret-key
server.port=8080
server.ssl.enabled=true
```

#### Run backend:
```bash
mvn spring-boot:run
```

### 🔹 Frontend (React.js)
```bash
npm start
```

#### Create .env:
```env
Create .env:
```

#### run frontend
```bash
npm start
```

## 🔐 Authentication Flow
- Local Login (JWT)
- User signs up with email & password
- Password stored with BCrypt encryption
- Login returns JWT → stored in local storage
- Google Login (OAuth2)
- User logs in with Google (password = null in DB)
- Backend verifies OAuth2 token → issues JWT
- Logout
- JWT cleared from local storage
- Redirect to login


## 🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you’d like to change.


## 👤 Author
**Sachin Chourasiya**
