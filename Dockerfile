# Используем базовый образ с Java 17
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл внутрь контейнера
COPY target/mtshack-0.0.1-SNAPSHOT.jar /app/mtshack.jar

# Определяем команду для запуска приложения
CMD ["java", "-jar", "mtshack.jar"]