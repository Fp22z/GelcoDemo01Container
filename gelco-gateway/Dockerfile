FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY gelco-discovery/pom.xml gelco-discovery/pom.xml
COPY gelco-config/pom.xml gelco-config/pom.xml
COPY gelco-gateway/pom.xml gelco-gateway/pom.xml
COPY gelco-auth-service/pom.xml gelco-auth-service/pom.xml
COPY gelco-catalogo-service/pom.xml gelco-catalogo-service/pom.xml
COPY gelco-consultoras-service/pom.xml gelco-consultoras-service/pom.xml
COPY gelco-pedidos-service/pom.xml gelco-pedidos-service/pom.xml
COPY gelco-capacitacion-service/pom.xml gelco-capacitacion-service/pom.xml
COPY gelco-devoluciones-service/pom.xml gelco-devoluciones-service/pom.xml
COPY gelco-distribucion-service/pom.xml gelco-distribucion-service/pom.xml
COPY gelco-reportes-service/pom.xml gelco-reportes-service/pom.xml

COPY gelco-gateway/src gelco-gateway/src
RUN mvn -pl gelco-gateway -am package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/gelco-gateway/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]