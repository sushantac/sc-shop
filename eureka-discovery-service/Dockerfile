FROM java:8
VOLUME /tmp
EXPOSE 8012
COPY ./eureka-discovery-service/target/eureka-discovery-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch eureka-discovery-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT exec java $JAVA_OPTS -jar eureka-discovery-service-0.0.1-SNAPSHOT.jar