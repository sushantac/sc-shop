FROM java:8
VOLUME /tmp
EXPOSE 8001
COPY ./target/config-service 0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch config-service 0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","config-service 0.0.1-SNAPSHOT.jar"]