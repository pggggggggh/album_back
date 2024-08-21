ARG DATABASE_URL
ARG JWT_SECRET

FROM eclipse-temurin:17-jdk-

ENV DATABASE_URL=${DATABASE_URL}
ENV JWT_SECRET=${JWT_SECRET}

ADD ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]