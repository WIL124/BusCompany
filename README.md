# BusCompany
## Goal

The goal is to build a tool for monitoring audience activity of website. This service should provide website statistics to website administrator.

## Function requirements

### Main

1. Website statistics should contain:
* statistics by browsers;
* statistics by countries;
* statistics by day of week;
* statistics by time of day;
* statistics by website pages;
* statistics by users
2. Website statistics should be updated automatically every hour

## Technologies

* Language: Java
* Database: Postgres, Apache HBase
* Build system: Apache Maven
* Frameworks: Spring Boot, Spring Data, Hibernate
* Environment: Docker


## Start
### Build
    mvn clean package
### Run all modules modules
    docker-compose up
### Demo page:
    http://localhost:8080/
### Tracker hosted in:
    http://localhost:8081/
### You can also use api:
    http://localhost:8082/
### Admin Page:
    http://localhost:8084/

## Stop
### Stop Environment
    docker-compose stop
### Remove Data
    docker-compose down