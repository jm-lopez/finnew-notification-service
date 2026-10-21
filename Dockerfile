#STAGE 1 : BUILDING
FROM maven:3.10.0-amazoncorretto-25 AS Build
LABEL authors="BassDX"
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

#STAGE 2 : RUNTIME

FROM amazoncorretto:25
WORKDIR /app

#Copy the JAR file
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]