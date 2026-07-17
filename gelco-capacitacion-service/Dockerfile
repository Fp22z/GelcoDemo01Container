FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-capacitacion-service/pom.xml gelco-capacitacion-service/pom.xml
COPY gelco-capacitacion-service/src gelco-capacitacion-service/src
RUN mvn -pl gelco-capacitacion-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-capacitacion-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]