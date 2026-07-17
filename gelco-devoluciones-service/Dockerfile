FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-devoluciones-service/pom.xml gelco-devoluciones-service/pom.xml
COPY gelco-devoluciones-service/src gelco-devoluciones-service/src
RUN mvn -pl gelco-devoluciones-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-devoluciones-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]