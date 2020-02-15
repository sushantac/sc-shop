FROM java:8
VOLUME /tmp
EXPOSE 8004
COPY ./target/cart-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch cart-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","cart-service-0.0.1-SNAPSHOT.jar"]