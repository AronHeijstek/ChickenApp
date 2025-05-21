# Chicken Financial Helper App

A Spring Boot application for managing financial transactions with a gamified virtual chicken feature.

## Features

- User registration and authentication with JWT
- Transaction management and insights
- Virtual chicken that levels up by completing challenges
- Financial challenges to improve spending habits
- Kafka integration for real-time transaction updates

## Technology Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security with JWT
- Spring Kafka
- PostgreSQL
- OpenAPI/Swagger for API documentation

## Setup Instructions

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- Kafka

### Database Setup

1. Create a PostgreSQL database named `chicken_db`:

```sql
CREATE DATABASE chicken_db;
```

2. Configure database connection in `application.yml` if necessary:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/chicken_db
    username: postgres
    password: password
```

### Build and Run

1. Clone the repository:

```bash
git clone https://github.com/yourusername/chicken-app.git
cd chicken-app
```

2. Build the application:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/api`.

## API Documentation

Once the application is running, you can access the API documentation at `http://localhost:8080/api/swagger-ui.html`.

### Main Endpoints

1. **Authentication**:
   - `POST /api/auth/register`: Register a new user
   - `POST /api/auth/login`: Login a user

2. **Transactions**:
   - `GET /api/transactions`: Get all transactions
   - `GET /api/spending-overview`: Get spending overview

3. **Chicken**:
   - `GET /api/chicken`: Get chicken status
   - `POST /api/chicken/feed`: Feed the chicken

4. **Challenges**:
   - `POST /api/challenges`: Create a new challenge
   - `GET /api/challenges`: Get all challenges
   - `GET /api/challenges/active`: Get active challenges
   - `PUT /api/challenges/{id}/status`: Update challenge status

## Example API Calls

### Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "password": "password123"}'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "password": "password123"}'
```

### Get Transactions (Authenticated)

```bash
curl -X GET http://localhost:8080/api/transactions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Feed the Chicken (Authenticated)

```bash
curl -X POST http://localhost:8080/api/chicken/feed \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Development

### Kafka Setup

The application connects to a Kafka server at `116.203.98.217:9092` and subscribes to a topic based on the user's username: `transactions.{username}`.

### TPP API Integration

The application integrates with a TPP API to fetch user details and transactions:
- User details: `https://api.spendstream.nl/v1/users/{username}`
- Transactions: `https://api.spendstream.nl/v1/users/{username}/transactions` 