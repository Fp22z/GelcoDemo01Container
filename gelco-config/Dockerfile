FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-config/pom.xml gelco-config/pom.xml
COPY gelco-config/src gelco-config/src
RUN mvn -pl gelco-config -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-config/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]