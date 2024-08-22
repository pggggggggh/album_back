FROM eclipse-temurin:17-jdk-alpine

ARG DATABASE_URL
ARG JWT_SECRET

ENV DATABASE_URL=${DATABASE_URL}
ENV JWT_SECRET=${JWT_SECRET}

ADD ./build/libs/*.jar app.jar

#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["sh", "-c", "echo DATABASE_URL=$(echo -n ${DATABASE_URL} | base64) && echo JWT_SECRET=$(echo -n ${JWT_SECRET} | base64) && java -jar app.jar"]