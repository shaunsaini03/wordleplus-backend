# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN curl -s -o src/main/resources/words.txt \
    https://raw.githubusercontent.com/dolph/dictionary/master/enable1.txt

RUN mvn clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]