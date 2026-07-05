# Order & Payment System Microservice

A professional Spring Boot multi-module microservice application designed to handle order orchestration and payment processing.

## 🚀 Features
- **Microservices Architecture**: Decoupled services for Orders and Payments.
- **Maven Multi-Module**: Shared dependencies and clean project separation.
- **RESTful APIs**: Standardized endpoints for inter-service communication.
- **Data Persistence**: Integration with Spring Data JPA.

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot 3.x**
- **Spring Cloud**
- **Maven**
- **H2/PostgreSQL**
- **Lombok**

## 📂 Project Structure
- `order-service`: Manages customer orders and inventory checks.
- `payment-service`: Processes transactions and updates payment status.
- `common-library`: Shared DTOs and Utility classes.

## ⚙️ How to Run
1. **Build all modules:**
   ```bash
   mvn clean install
   ```
2. **Run Order Service:**
   ```bash
   cd order-service && mvn spring-boot:run
   ```
3. **Run Payment Service:**
   ```bash
   cd payment-service && mvn spring-boot:run
   ```

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
**Developed by [Priyajit](https://github.com/Priyajit100)**
