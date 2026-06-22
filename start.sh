#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "========================================="
echo "       Building IdeaSnap Application     "
echo "========================================="

# Build the Maven project locally to verify there are no compilation errors
mvn clean package -DskipTests

echo "========================================="
echo "       Starting IdeaSnap Docker Stack    "
echo "========================================="

# Build and start all services in detached mode
docker-compose down
docker-compose up --build -d

echo "========================================="
echo "       IdeaSnap Stack is Running!        "
echo "========================================="
echo "Spring Boot app:  http://localhost:28080"
echo "Swagger UI API:   http://localhost:28080/swagger-ui.html"
echo "PostgreSQL port:  5432"
echo "Redis port:       6379"
echo "========================================="
echo "Check logs using: docker logs -f ideasnap-app"
echo "========================================="
