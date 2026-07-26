# online-tiffin-service

A production-oriented **Online Tiffin Service Backend** developed using **Java Spring Boot** following a clean layered architecture and RESTful API design. The system connects **Customers**, **Kitchen Owners**, and **Administrators** on a single platform where customers can discover kitchens, order homemade meals, and track their orders.

This project demonstrates industry-standard backend development practices including DTO mapping, validation, exception handling, JPA entity relationships, repository pattern, service layer abstraction, and REST API development. :contentReference[oaicite:0]{index=0}

---

# 🚀 Project Status

**Current Progress:** ~70% Backend Completed

### Completed Modules

- ✅ User Management
- ✅ Kitchen Management
- ✅ Menu Management
- ✅ Order Management APIs
- ✅ Dashboard & Reporting APIs
- ✅ Exception Handling
- ✅ Validation
- ✅ DTO Mapping
- ✅ Layered Architecture

The backend currently provides a solid foundation and demonstrates real-world Spring Boot application development. :contentReference[oaicite:1]{index=1}

---

# 📌 Features

## Customer

- Register/Login (Basic)
- Browse Kitchens
- Search Kitchens by City
- View Available Menu Items
- Place Orders
- View Order History
- Cancel Orders

---

## Kitchen Owner

- Register Kitchen
- Update Kitchen Details
- Delete Kitchen
- Manage Menu Items
- Enable/Disable Menu Availability
- View Orders
- Update Order Status

---

## Administrator

- View All Kitchens
- Block/Unblock Kitchens
- View All Orders
- Dashboard Statistics
- Revenue Reports
- Admin Commission Reports

---

# 🏗️ Technology Stack

## Backend

- Java 17+
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- REST APIs

## Database

- MySQL

## Build Tool

- Maven

## Documentation

- Swagger / OpenAPI

## Utilities

- Lombok
- ModelMapper
- Bean Validation

## Version Control

- Git
- GitHub

### Planned Technologies

- React.js
- JWT Authentication
- Cloudinary
- Spring Mail

:contentReference[oaicite:2]{index=2}

---

# 📂 Project Structure

```
src
│
├── controller
├── service
│     ├── impl
│
├── repository
├── entity
├── dto
├── exception
├── config
├── enums
└── OnlineTiffinServiceApplication
```

The project follows the standard Spring Boot layered architecture.

```
Client
   │
Controller
   │
Service
   │
Repository
   │
MySQL Database
```

DTOs are used between Controller and Service layers to separate API contracts from persistence models. Validation and global exception handling ensure consistent responses. :contentReference[oaicite:3]{index=3}

---

# 🗃️ Database Design

Main Entities

- User
- Kitchen
- MenuItem
- Order
- OrderItem

### Relationships

```
User
 │
 ├──────────────┐
 │              │
Customer     Kitchen Owner
                  │
                  │ 1
                  │
                  ▼
             Kitchen
                  │
                  │ 1
                  │
                  ▼
             MenuItem

Customer
    │
    │ 1
    ▼
 Order
    │
    │ 1
    ▼
OrderItem
    │
    ▼
 MenuItem
```

Enums used:

- Role
- Food Category
- Order Status
- Payment Status
- Payment Method

:contentReference[oaicite:4]{index=4}

---

# 📡 REST APIs

## Kitchen APIs

- Register Kitchen
- Update Kitchen
- Delete Kitchen
- Get Kitchen By ID
- Get All Kitchens
- Search Kitchen By City
- Get Active Kitchens
- Block Kitchen
- Unblock Kitchen

---

## Menu APIs

- Add Menu Item
- Update Menu Item
- Delete Menu Item
- Toggle Availability
- Get Menu By Kitchen
- Get Available Menu Items

---

## Order APIs

- Place Order
- Cancel Order
- Get Customer Orders
- Get Kitchen Orders
- Update Order Status

---

## Dashboard APIs

- Total Orders
- Delivered Orders
- Pending Orders
- Cancelled Orders
- Total Revenue
- Total Admin Commission
- Kitchen Earnings

---

# ✅ Best Practices Used

- Layered Architecture
- RESTful API Design
- DTO Pattern
- Repository Pattern
- Service Abstraction
- Global Exception Handling
- Bean Validation
- ModelMapper
- JPA Entity Relationships
- Clean Package Structure
- ResponseEntity Usage
- Custom Queries (JPQL)

---

# 🧪 Future Enhancements

The project roadmap includes several improvements that will make it closer to a production-ready application.

### 🔐 Authentication & Authorization

- JWT Authentication
- Role-Based Authorization
- Secure Endpoints
- Password Encryption

---

### ☁️ Cloudinary Integration

- Upload Menu Images
- Store Images in Cloud
- Image URL Management

---

### 💻 React Frontend

Develop a complete frontend application including:

- Customer Dashboard
- Kitchen Dashboard
- Admin Dashboard
- Responsive UI
- Authentication Screens
- Order Tracking

---

### 📧 Email Notifications

Using Spring Mail:

- Order Confirmation
- Order Cancellation
- Order Delivered
- Kitchen Approval
- Password Reset

---

### 📄 Pagination, Sorting & Filtering

Implement:

- Pagination
- Sorting
- Advanced Search
- Dynamic Filters

---

### ✅ Unit Testing

Testing using:

- JUnit 5
- Mockito

Coverage:

- Service Layer
- Repository (Mocked)
- Controllers
- Exception Handling

---

### 🚀 Deployment

Deploy using:

- Docker
- AWS / Render / Railway
- MySQL Cloud Database
- CI/CD Pipeline
- GitHub Actions

These enhancements align with the project's documented roadmap and implementation order. :contentReference[oaicite:5]{index=5}

---

# 🎯 Learning Outcomes

This project helped in understanding:

- Spring Boot Architecture
- REST API Development
- Spring Data JPA
- Hibernate Mapping
- DTO Pattern
- Bean Validation
- Exception Handling
- Repository Pattern
- JPQL Queries
- Entity Relationships
- Maven
- Git & GitHub

---

# 📈 Current Assessment

### Current Backend Completion

**~70%**

### Current Project Rating

⭐ **9.2 / 10**

### Expected Rating After Completion

⭐ **9.8 – 10 / 10**

Major strengths include:

- Clean layered architecture
- Proper DTO usage
- Validation
- Exception handling
- Well-defined entity relationships
- Scalable backend design

Remaining focus areas include:

- JWT Security
- React Frontend
- Cloudinary Integration
- Email Notifications
- Unit Testing
- Deployment

:contentReference[oaicite:6]{index=6}

---

# 👨‍💻 Author

**Ganesh Bhutekar**

CDAC PG-DAC Student

Java Full Stack Developer

GitHub: *(https://github.com/GaneshBhutekar)*


---

# ⭐ Future Vision

The ultimate goal of this project is to build a **complete production-ready Online Tiffin Service Platform** with secure authentication, responsive React frontend, cloud-based image storage, automated email notifications, comprehensive testing, and cloud deployment. Once completed, it will demonstrate the skills expected of an entry-level Java Backend/Full Stack Developer and serve as a strong portfolio project for placements and interviews. :contentReference[oaicite:7]{index=7}
This README is suitable for a GitHub repository and presents your project in a professional, recruiter-friendly format while reflecting the current implementation and documented future roadmap.


