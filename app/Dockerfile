FROM eclipse-temurin:25
RUN addgroup --system spring
RUN adduser --system testjdbi
RUN adduser testjdbi spring
USER testjdbi:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
