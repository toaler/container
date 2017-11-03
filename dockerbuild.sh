sudo docker build -t container-discovery -f Dockerfile-container-discovery .
sudo docker run -it -p 80:8888 --name=container-discovery container-discovery
