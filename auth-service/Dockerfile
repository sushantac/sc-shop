FROM java:8
VOLUME /tmp
EXPOSE 8001
COPY ./auth-service/target/auth-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch auth-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT exec java $JAVA_OPTS -jar auth-service-0.0.1-SNAPSHOT.jar