# Turno Loan Origination System – Backend Assignment

A Spring Boot application simulating a loan origination system with multithreaded loan processing, agent hierarchy, and mocked notification service.

---

## Features

- Submit and track loan applications
- Auto-processing with random delay and rule-based approval
- Agent assignment for `UNDER_REVIEW` loans
- Agent-manager hierarchy
- Mock notification system (push + SMS)
- Analytics endpoints (loan status counts, top customers)
- Pagination and filtering for loan data

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 in-memory database
- JUnit + Mockito
- Maven

---

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.8+
- (Optional) Postman or cURL for API testing

---

###  Setup Instructions

1. **Clone the Repository**

git clone https://github.com/udaramrdc/Turno-Springboot-Assignment.git
cd turno-loan-origination
./mvnw spring-boot:run
Or via IDE: Run LosApplication.java
