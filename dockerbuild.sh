sudo docker build -t container-discovery -f Dockerfile-container-discovery .
sudo docker build -t container-example-webapp -f Dockerfile-container-example-webapp .
sudo docker run -it -p 80:8888 --name=container-discovery container-discovery
sudo docker run -it --name=container-example-webapp container-example-webapp
