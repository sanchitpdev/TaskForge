# 📌 TaskForge – Secure Task Management Backend System

## 🚀 Overview

**TaskForge** is a secure and scalable **Task Management Backend System** built using **Spring Boot**. It follows a clean **layered architecture** and implements **JWT-based authentication and authorization** to ensure secure access to APIs.

This project is designed to simulate **real-world backend development practices**, including role-based access control, structured API design, and robust exception handling.

---

## 🧠 Key Features

- 🔐 JWT-based Authentication & Authorization  
- 👥 Role-Based Access Control (Admin / User)  
- 🧱 Layered Architecture (Controller → Service → Repository)  
- ⚠️ Global Exception Handling with consistent API responses  
- 📦 DTO-based request and response handling  
- 🧪 Fully tested APIs using Postman  

---

## 🏗️ Architecture

```
Client (Postman)
        ↓
JWT Authentication Filter (Spring Security)
        ↓
Controller Layer
        ↓
Service Layer
        ↓
Repository Layer (Spring Data JPA)
        ↓
Database (PostgreSQL)
```

---

## 🧩 Modules

### 🔐 Authentication Module
- User Login
- JWT Token Generation
- Secure API access using token

### 👤 User Module
- View profile
- Update profile

### 📋 Task Module
- Create tasks
- Assign tasks
- Update task status
- Track tasks

### 🛠 Admin Module
- Manage users
- Change roles

---

## 🧪 API Testing (Postman)

All APIs are tested using **Postman**:

- ✅ Authentication (Login & Token validation)
- ✅ Role-based access (Admin/User endpoints)
- ✅ CRUD operations (Users & Tasks)
- ✅ Security checks (401, 403 responses)
- ✅ Complete test suite execution  

✔ All test cases passed successfully.

---

## 🛠 Tech Stack

- **Backend:** Java, Spring Boot  
- **Security:** Spring Security, JWT  
- **Database:** PostgreSQL  
- **Build Tool:** Maven  
- **Testing:** Postman  
- **Version Control:** Git & GitHub  

---

## 📂 Project Structure

```
TaskForge
│
├── TaskForge-Backend
│   ├── controller
│   ├── service
│   ├── repository
│   ├── config
│   ├── security
│   ├── dto
│   ├── entity
│   └── exception
│
├── Postman
│   └── taskforge-api-collection.json
│
└── README.md
```

---

## ⚙️ Setup & Run Locally

### 1️⃣ Clone the repository

```bash
git clone https://github.com/your-username/taskforge.git
cd taskforge
```

---

### 2️⃣ Configure Database

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskforge
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### 3️⃣ Run the application

```bash
mvn spring-boot:run
```

---

### 4️⃣ Access APIs

```
http://localhost:8080/api
```

---

## 🔐 Authentication Flow

1. User logs in via `/api/auth/login`
2. JWT token is generated
3. Token is sent in request headers:

```
Authorization: Bearer <token>
```

4. Secured endpoints validate the token before access

---

## 📌 Sample API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /api/auth/login | User login |
| GET    | /api/users/profile | Get user profile |
| POST   | /api/tasks | Create task |
| GET    | /api/tasks | Get tasks |
| PUT    | /api/tasks/{id} | Update task |
| GET    | /api/admin/users | Admin-only access |

---

## ⚠️ Exception Handling

- Centralized using `GlobalExceptionHandler`
- Returns consistent JSON error responses
- Handles:
  - 400 Bad Request
  - 401 Unauthorized
  - 403 Forbidden
  - 404 Not Found
  - 500 Internal Server Error

---

## 🎯 Learning Outcomes

- Backend Development with Spring Boot  
- REST API Design & Development  
- JWT Authentication & Security  
- Clean Architecture & Code Structure  
- API Testing using Postman  

---

## 📌 Future Improvements

- Add frontend (React / Angular)  
- Add Docker support  
- Add unit & integration tests  
- Deploy on cloud  

---

## 🤝 Contributing

Feel free to fork this repository and contribute.

---

## 📬 Contact

For feedback or suggestions, feel free to connect.
