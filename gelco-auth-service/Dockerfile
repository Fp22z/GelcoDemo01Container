FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-auth-service/pom.xml gelco-auth-service/pom.xml
COPY gelco-auth-service/src gelco-auth-service/src
RUN mvn -pl gelco-auth-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-auth-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]