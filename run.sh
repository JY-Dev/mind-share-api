#!/bin/bash

echo "BUILD mind-share-db IMAGE"
docker build -t mind-share-db ./db/mysql

echo "BUILD mind-share-api IMAGE"
docker build -f api/mind-share-api/Dockerfile -t mind-share-api .

docker-compose up