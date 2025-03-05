docker build --rm -t trade-system  --platform linux/amd64 . 

docker run -p 8080:8080 -p 3000:3000 --platform linux/amd64 trade-system