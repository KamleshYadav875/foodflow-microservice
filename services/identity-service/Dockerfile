FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java","-Duser.timezone=Asia/Kolkata","-jar","app.jar"]