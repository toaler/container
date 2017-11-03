FROM treasureboat/java

RUN mkdir -p /app/lib

RUN ls -lrt /app/lib/
COPY container-driver/target/container-driver-0.0.1-SNAPSHOT.jar /app/lib/
COPY container-driver/lib /app/lib/
COPY container-driver/run.sh /app
COPY container-discovery/target/container-discovery-0.0.1-SNAPSHOT.war /app
RUN ls -lrt /app 
RUN ls -lrt /app/lib/

WORKDIR /app

EXPOSE 8888

CMD ["./run.sh", "./container-discovery-0.0.1-SNAPSHOT.war"]
