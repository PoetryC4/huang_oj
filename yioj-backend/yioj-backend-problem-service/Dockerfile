# Docker 镜像构建
FROM openjdk:8-jdk-alpine

# Copy local code to the container image.
WORKDIR /app
ADD target/yioj-backend-problem-service-0.0.1-SNAPSHOT.jar .

# Build a release artifact.
EXPOSE 8004

# Run the web service on container startup.
ENTRYPOINT ["java","-jar","/app/yioj-backend-problem-service-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]