FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-discovery/pom.xml gelco-discovery/pom.xml
COPY gelco-discovery/src gelco-discovery/src
RUN mvn -pl gelco-discovery -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-discovery/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]