FROM java:8
VOLUME /tmp
EXPOSE 8006
COPY ./payment-service/target/payment-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch payment-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT exec java $JAVA_OPTS -jar payment-service-0.0.1-SNAPSHOT.jar