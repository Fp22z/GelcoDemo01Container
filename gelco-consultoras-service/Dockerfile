FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-consultoras-service/pom.xml gelco-consultoras-service/pom.xml
COPY gelco-consultoras-service/src gelco-consultoras-service/src
RUN mvn -pl gelco-consultoras-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-consultoras-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]