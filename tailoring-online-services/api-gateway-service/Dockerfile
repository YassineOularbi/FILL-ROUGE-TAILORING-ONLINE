FROM openjdk:17
VOLUME /tmp
COPY target/*.jar  app.jar
ENTRYPOINT ["java","--enable-preview","-jar", "app.jar"]