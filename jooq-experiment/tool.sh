#!/bin/bash

command=$1

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
  FLYWAY_USER=user1 \
  FLYWAY_PASSWORD=password1 \
  FLYWAY_URL=jdbc:mysql://localhost:3308/db1 \
  ./gradlew flywayMigrate -i

elif [[ "${command}" == "delete-db-data" ]]; then
  docker-compose stop db
  sudo rm -rf .data

elif [[ "${command}" == "generate-jooq" ]]; then
  ./gradlew cleanGenerateDbJooqSchemaSource generateDbJooqSchemaSource

elif [[ "${command}" == "" ]]; then
  echo "No command specified"
else
  echo "Unknown command ${command}"
fi
