#!/bin/bash
docker run --name monolith-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=monolith -p 3306:3306 -d mysql:8.0
