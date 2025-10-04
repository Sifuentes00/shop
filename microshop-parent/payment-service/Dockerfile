# Используем образ с Java 17
FROM openjdk:17-jdk-slim

# Рабочая директория
WORKDIR /app

# Копируем jar
COPY target/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запуск сервиса
ENTRYPOINT ["java","-jar","app.jar"]
