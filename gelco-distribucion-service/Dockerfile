FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-distribucion-service/pom.xml gelco-distribucion-service/pom.xml
COPY gelco-distribucion-service/src gelco-distribucion-service/src
RUN mvn -pl gelco-distribucion-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-distribucion-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]