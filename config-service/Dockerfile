FROM java:8
VOLUME /tmp
EXPOSE 8011
COPY ./config-service/target/config-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch config-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT exec java $JAVA_OPTS -jar config-service-0.0.1-SNAPSHOT.jar