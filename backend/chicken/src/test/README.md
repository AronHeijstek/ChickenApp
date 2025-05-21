# Chicken Financial Helper - Test Suite

This directory contains comprehensive tests for the Chicken Financial Helper application, with a particular focus on Kafka integration and user registration flows.

## Test Structure

The test suite is organized into the following categories:

### Integration Tests

Located in `java/com/app/chicken/integration/`:

- **UserAuthIntegrationTest**: Tests user registration and authentication endpoints
- **KafkaIntegrationTest**: End-to-end tests for Kafka message processing with real components
- **KafkaManagerIntegrationTest**: Tests for the KafkaManager component with dynamic topic subscription

### Kafka Tests

Located in `java/com/app/chicken/kafka/`:

- **KafkaSubscriptionTest**: Tests for Kafka topic subscription functionality
- **TransactionConsumerTest**: Tests for Kafka message handling and transaction processing

## Test Users

The tests use the following test users:

```json
[
    {
        "username": "acc_peter",
        "name": "peter",
        "iban": "NL11BUGL00000031321",
        "balance": 100000.00
    },
    {
        "accountId": "acc_tony",
        "name": "Tony Stark",
        "iban": "US22STARK00000099999",
        "balance": 10000000.00
    },
    {
        "accountId": "acc_jim",
        "name": "Jim Halpert",
        "iban": "US33DUNDER00000088888",
        "balance": 50000.00
    }
]
```

## Running the Tests

### Running All Tests

```
mvn test
```

### Running a Specific Test Class

```
mvn test -Dtest=KafkaIntegrationTest
```

### Running a Specific Test Method

```
mvn test -Dtest=KafkaIntegrationTest#testEndToEndFlow
```

## Test Environment

The tests use:

1. An embedded Kafka broker for Kafka-related tests
2. MockMvc for testing HTTP endpoints
3. Spring Boot test slices where appropriate
4. Mockito for mocking external dependencies

## Important Notes

- The Kafka tests create and delete topics as needed
- Each test method cleans up after itself
- The tests use `@DirtiesContext` to ensure a clean Spring context for each test class 