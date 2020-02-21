FROM java:8
VOLUME /tmp
EXPOSE 8001
COPY ./api-gateway-service/target/api-gateway-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch api-gateway-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","api-gateway-service-0.0.1-SNAPSHOT.jar"]