FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-gateway/pom.xml gelco-gateway/pom.xml
COPY gelco-gateway/src gelco-gateway/src
RUN mvn -pl gelco-gateway -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-gateway/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]