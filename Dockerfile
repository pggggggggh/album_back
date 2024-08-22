FROM eclipse-temurin:17-jdk-alpine

ARG DATABASE_URL
ARG JWT_SECRET

ENV DATABASE_URL=${DATABASE_URL}
ENV JWT_SECRET=${JWT_SECRET}

ADD ./build/libs/*.jar app.jar

# 쉘 스크립트를 사용하여 환경 변수 출력 및 애플리케이션 실행
RUN echo '#!/bin/sh' > /start.sh && \
    echo 'echo "DATABASE_URL=$DATABASE_URL"' >> /start.sh && \
    echo 'echo "JWT_SECRET=$JWT_SECRET"' >> /start.sh && \
    echo 'exec java -jar app.jar' >> /start.sh && \
    chmod +x /start.sh

ENTRYPOINT ["/start.sh"]