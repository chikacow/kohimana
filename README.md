# Kohimana ☕️
**All-in-One Coffee Business Manager**  
*A scalable and secure management system for coffee shops*

---

## 📌 Overview
Kohimana is a comprehensive Coffee Shop Management System designed to streamline operations for businesses of all sizes. The system supports multiple user roles (admin, manager, staff, and customer) with secure, role-based access to all functionalities.

The backend is built using modern Java technologies with an emphasis on **security**, **scalability**, and **maintainability**.

---

## 🚀 Features
- **Role-based RESTful APIs** for complete CRUD operations
- **JWT authentication** with Spring Security
- Advanced data handling:
    - Pagination
    - Dynamic filtering
    - Search functionality
- Data consistency with:
    - Transactional processing
    - Custom validation annotations
- **Multithreaded background tasks** for system maintenance
- **DTO integration** for efficient client-server communication
- **MiniIO integration** for file storage management

---

## 🛠️ Technologies Used
**Languages & Frameworks:**
- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Validation

**Database:**
- MySQL

**Security:**
- JWT (JSON Web Token)

**File Management:**
- MiniIO

**Others:**
- Multithreading
- DTO design pattern
- Custom Validation Annotations

---

## ✅ Getting Started

### Prerequisites
- Java 17+
- Maven or Gradle
- MySQL database
- Docker (for MiniIO)
- Postman (for API testing)

### Clone the Repository
```bash
git clone https://github.com/shibeeu/kohimana.git
cd kohimana

```

Setup MySQL Database
```sql
CREATE DATABASE kohimana;
Update application.properties:
```

```
properties
spring.datasource.url=jdbc:mysql://localhost:3306/kohimana
spring.datasource.username=root
spring.datasource.password=your_password
```
Run MiniIO (Optional for Local Dev)
```bash
docker run -p 9000:9000 -p 9001:9001 \
-e "MINIO_ROOT_USER=minioadmin" \
-e "MINIO_ROOT_PASSWORD=minioadmin" \
quay.io/minio/minio server /data --console-address ":9001"
```
Update file storage config accordingly.
## 🔐 Security

- ✅ **Spring Security + JWT** for stateless authentication
- 🔐 Role-based endpoint security (`admin`, `manager`, `staff`, `customer`)
- 🔑 Password hashing using **BCrypt**

---

## 📎 Contributions

Pull Requests are welcome! 🎉

1. Fork the repository
2. Create your feature branch
3. Submit a pull request
