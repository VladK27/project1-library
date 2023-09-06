FROM amazoncorretto:20.0.2-alpine
LABEL authors="vlad"

WORKDIR /app

COPY ./target/project-library-1.0.0.jar ./app.jar

CMD [ "java", "-jar", "app.jar" ]