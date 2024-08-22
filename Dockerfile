FROM eclipse-temurin:17-jdk-alpine

ARG DATABASE_URL
ARG JWT_SECRET

ENV DATABASE_URL=${DATABASE_URL}
ENV JWT_SECRET=${JWT_SECRET}

ADD ./build/libs/*.jar app.jar

RUN echo "DATABASE_URL=${DATABASE_URL}" | base64
ENTRYPOINT ["java", "-jar", "app.jar"]