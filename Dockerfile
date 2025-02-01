FROM amazoncorretto:21.0.6 AS build
RUN yum update -y && yum install -y tar gzip

WORKDIR /app

COPY . .
RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:21.0.6-alpine

WORKDIR /app

COPY --from=build /app/target/kitchensink-spring-boot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]