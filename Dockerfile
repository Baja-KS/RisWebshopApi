ARG MAVEN_VERSION=3.8
ARG JAVA_VERSION=17
FROM maven:$MAVEN_VERSION-openjdk-$JAVA_VERSION as build
WORKDIR /app
COPY . .
RUN mvn clean && mvn install -DskipTests

FROM openjdk:$JAVA_VERSION-alpine as runtime
RUN addgroup --system --gid 1001 spring && \
         adduser --system --uid 1001 spring && \
         mkdir -p /home/spring/app
WORKDIR /home/spring/app
COPY --chown=spring:spring --from=build /app/target/*.jar app.jar
USER spring
EXPOSE 8080
CMD ["java","-jar","app.jar"]

