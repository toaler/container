# FAQ

1. Build keystore frist by `cd container-driver; ./create_keystore.sh` 
2. Run `mvn install` from parent `container` directory.
3. Start `container-example` webapp by running `run.sh start ../container-example-webapp/target/container-example-webapp-0.0.1-SNAPSHOT.war` 
4. Hit various endpoints.
- HTTP 1.1 without TLS - http://localhost:8888/applicationcontext
- HTTP 2 - https://localhost:8889/applicationcontext
- https://localhost:8889/v1/healthcheck/status 
