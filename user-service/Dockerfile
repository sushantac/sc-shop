FROM java:8
VOLUME /tmp
EXPOSE 8002
COPY ./user-service/target/user-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch user-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","user-service-0.0.1-SNAPSHOT.jar"]