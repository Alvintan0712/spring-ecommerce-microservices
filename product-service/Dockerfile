FROM eclipse-temurin:21.0.6_7-jdk as builder

WORKDIR /build

COPY pom.xml .
COPY product-service/ service/

RUN apt-get update && apt-get install -y tar gzip
RUN chmod +x service/mvnw
RUN cd service && ./mvnw clean package -Dmaven.test.skip=true

FROM amazoncorretto:21.0.6

COPY --from=builder /build/service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]