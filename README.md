# Grids and tiles
Java RESTful Web Service for creating and managing grids and tiles.

## 1. Install&Run

### Used technologies

Java 11

Gradle 6.1.1

JUnit 5.6.0

Spring Boot 2.2.4

H2 database 1.4.200

### Run as a JAR file

Build:

`gradlew clean build`

Run the application:

`java -jar build/libs/gridsandtiles-0.0.1-SNAPSHOT.jar`

## 1. Get a grid:

`curl -X GET http://localhost:8080/api/grids/1`

## 2. Create grid (Windows style):

`curl -X POST http://localhost:8080/api/grids/ -H "Content-Type: application/json" --data "{\"name\":\"test\",\"dimensions\":{\"width\":10,\"height\":5}}"`

## 3. Get a tile:

`curl -X GET http://localhost:8080/api/grids/1/api/tiles/1`

## 4. Create tile (Windows style):

`curl -X POST http://localhost:8080/api/grids/1/tiles -H "Content-Type: application/json" --data "{\"url\":\"http://example.com\",\"title\":\"test\",\"position\":{\"xposition\": 2,\"yposition\": 2}}"`

## 5. Delete a tile:

`curl -X DELETE http://localhost:8080/api/grids/1/tiles/1`

## 6. Edit a tile (Windows style):

`curl -X PUT http://localhost:8080/api/grids/1/tiles/1 -H "Content-Type: application/json" --data "{\"url\":\"http://example.com\",\"title\":\"test\"}"`

### Authors
Daniel Hajdu-Kis - dhajdukis
