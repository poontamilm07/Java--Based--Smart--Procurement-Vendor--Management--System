# Procurement & Vendor Management System
## Agile Documentation

---

## 1. Title
Procurement & Vendor Management System

---

## 2. Problem Statement
Manual procurement systems lack transparency, security, and workflow automation. Organizations face delays in approvals and poor vendor tracking.

---

## 3. Solution
We developed a secure, role-based procurement system with automated approval workflow and reporting functionality.

---

## 4. Technology Stack
Backend: Java, Spring Boot, Spring Security, JWT, Hibernate  
Database: MySQL  
Reporting: JasperReports  
Frontend: React  
Tools: IntelliJ, VS Code, Postman  

---

## 5. Project Architecture
Layered Architecture:

Controller → Service → Repository → Database

---

## 6. Database Design
Tables:
- users
- roles
- vendors
- requisitions
- requisition_items
- purchase_orders
- purchase_order_items

---

## 7. Authentication Flow (JWT)
1. User login
2. JWT token generated
3. Token sent in Authorization header
4. Request validated using JWT filter
5. Access granted based on role

---

## 8. Complete Workflow
Employee → Create Requisition  
Manager → Approve Requisition  
Procurement → Create Purchase Order  
Admin → Approve Purchase Order  

---

## 9. Role Responsibility
Admin – Manage system  
Manager – Approve requisitions  
Procurement – Create purchase orders  
Employee – Create requisitions  

---

## 10. Requisition Lifecycle
CREATED → PENDING → APPROVED → PURCHASE ORDER CREATED

---

## 11. API Structure
POST /auth/login  
POST /vendors/create  
POST /requisition/create  
POST /procurement/purchase/create  
PUT /procurement/purchase/approve/{id}  

---

## 12. Security Implementation
- JWT authentication
- Role-based authorization
- BCrypt password encryption
- Stateless session management

---

## 13. Error Handling
- Global exception handling
- Custom error messages
- Proper HTTP status codes

---

## 14. Deployment Flow
1. Configure MySQL
2. Run mvn clean install
3. Run mvn spring-boot:run
4. Access at localhost:8082

---

## 15. Key Features
- Vendor management
- Requisition workflow
- Purchase approval system
- PDF/Excel reports
- JWT security

---

## 16. Limitations
- No payment integration
- No email notification

---

## 17. Future Enhancements
- Email alerts
- Multi-level approval
- Dashboard analytics
- Cloud deployment

---

## 18. Conclusion
The system automates procurement lifecycle with security and structured workflow management.

---

## 19. Demo
Demonstration includes:
- Login
- Create vendor
- Create requisition
- Approve requisition
- Create purchase order
- Approve purchase order
- Generate report
