FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-pedidos-service/pom.xml gelco-pedidos-service/pom.xml
COPY gelco-pedidos-service/src gelco-pedidos-service/src
RUN mvn -pl gelco-pedidos-service -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-pedidos-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]