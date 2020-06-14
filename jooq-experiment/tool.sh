#!/bin/bash

command=$1

LOCAL_MYSQL_USER=user1
LOCAL_MYSQL_PASSWORD=password1
LOCAL_MYSQL_DB=db1
LOCAL_MYSQL_URL=jdbc:mysql://localhost:3308/${LOCAL_MYSQL_DB}

if [[ "${command}" == "start-db" ]]; then
  docker-compose stop db
  docker-compose up --build --no-start db
  docker-compose start db
  dbContainerId=$(docker-compose ps -q db)

  attempts=60
  succeeded=0
  while [[ "${attempts}" -gt "0" ]];
  do
    echo "Waiting: ${attempts}..."
    if docker logs --since 2s ${dbContainerId} 2>&1 | grep --quiet "port: 3306  MySQL Community Server - GPL."; then
      succeeded=1
      break
    fi
    sleep 1;
    let attempts=attempts-1;
  done

  if [[ "${succeeded}" == "1" ]]; then
    echo "Succeeded!"
  else
    echo "Failed!"
  fi

elif [[ "${command}" == "stop-db" ]]; then
  docker-compose stop db

elif [[ "${command}" == "migrate-db" ]]; then
  FLYWAY_USER=${LOCAL_MYSQL_USER} \
  FLYWAY_PASSWORD=${LOCAL_MYSQL_PASSWORD} \
  FLYWAY_URL=${LOCAL_MYSQL_URL} \
  ./gradlew flywayMigrate -i

elif [[ "${command}" == "delete-db-data" ]]; then
  docker-compose stop db
  sudo rm -rf .data

elif [[ "${command}" == "generate-jooq" ]]; then
  DB_USER=${LOCAL_MYSQL_USER} \
  DB_PASSWORD=${LOCAL_MYSQL_PASSWORD} \
  DB_URL=${LOCAL_MYSQL_URL} \
  DB_NAME=${LOCAL_MYSQL_DB} \
  ./gradlew cleanGenerateDbJooqSchemaSource generateDbJooqSchemaSource

elif [[ "${command}" == "" ]]; then
  echo "No command specified"
else
  echo "Unknown command ${command}"
fi
