FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY target/identity-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java","-Duser.timezone=Asia/Kolkata","-jar","app.jar"]