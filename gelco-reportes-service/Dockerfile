FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-reportes-service/pom.xml gelco-reportes-service/pom.xml
COPY gelco-reportes-service/src gelco-reportes-service/src
RUN mvn -pl gelco-reportes-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-reportes-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]