# Multi-stage build for Game of War

# Stage 1: Build
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/build/libs/gameOfWar-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 5000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:5000/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


