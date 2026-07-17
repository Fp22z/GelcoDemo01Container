FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-catalogo-service/pom.xml gelco-catalogo-service/pom.xml
COPY gelco-catalogo-service/src gelco-catalogo-service/src
RUN mvn -pl gelco-catalogo-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-catalogo-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]