# Docker Setup for ChickenApp

This repository contains Docker configuration for running the ChickenApp with its frontend, backend, and PostgreSQL database.

## Prerequisites

- Docker and Docker Compose installed on your machine
- Git (to clone the repository)

## Services

The Docker Compose setup includes the following services:

1. **Frontend** - Next.js application (port 80)
2. **Backend** - Spring Boot application (port 81)
3. **Database** - PostgreSQL (port 5432)
4. **PgAdmin** - Database management UI (port 5050) - only in development mode

## Database Configuration

The PostgreSQL database is configured with:
- Database name: `chicken_db`
- Username: `postgres`
- Password: `password`

This matches the configuration used in the standalone command:
```bash
docker run --name chicken-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=chicken_db -p 5432:5432 -d postgres
```

## Getting Started

### Production Mode

To run the application in production mode:

```bash
docker compose up -d
```

This will:
- Build and start the frontend on port 80
- Build and start the backend on port 81
- Start a PostgreSQL database on port 5432

### Development Mode

For development with hot-reload capabilities:

```bash
docker compose -f docker-compose.yml -f docker-compose.override.yml up -d
```

This includes:
- Frontend with hot reload
- Backend with volume mount for code changes
- PgAdmin for database management (http://localhost:5050)
  - Login: admin@example.com
  - Password: admin
  - Pre-configured connection to the PostgreSQL database

## Accessing the Application

- Frontend: http://localhost:80
- Backend API: http://localhost:81
- PgAdmin (dev only): http://localhost:5050

## Configuration

You can modify environment variables in the docker-compose files:

- Database credentials
- API URL
- Other service-specific settings

## Stopping the Services

To stop all services:

```bash
docker compose down
```

To stop and remove volumes:

```bash
docker compose down -v
``` 