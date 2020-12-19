FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 9097
ADD target/analytics-services-0.0.1-SNAPSHOT.jar analytics-services.jar
CMD ["java","-jar","analytics-services.jar"]