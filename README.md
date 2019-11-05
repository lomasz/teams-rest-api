# Team REST API
Create a Rest API that will have 2 methods:

- [x] the 1st one will return a list of teams. Each list will have a list of players. 
    The list will be paginated and sortable server side (sortable by team name, acronym and budget).
- [x] The 2nd one will permit to add a team with players, or not (all the other fields are required)

Technical environment:
- Spring
- Hibernate
- Database: H2 (Embedded)

### Example of data model:
```
Team:   [id, name, acronym, players, budget]
Player: [id, name, position]
```

---

### How to run
#### using docker-compose
```
./gradlew clean build
docker-compose up
```

#### using gradle wrapper
```
./gradlew clean bootRun
```

### How to use endpoints

### by swagger
```
http://localhost:8080/swagger-ui.html
```

### by curl

###### [POST] /api/teams
```
curl -X POST "http://localhost:8080/api/teams" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"name\": \"Olympique Gymnaste Club Nice\", \"acronym\": \"OGC\", \"budget\": 182005000, \"players\": [ { \"name\": \"Youcef Atal\", \"position\": \"RIGHT_FULLBACK\" } ]}"
```

###### [GET] /api/teams
```
curl -X GET "http://localhost:8080/api/teams
curl -X GET "http://localhost:8080/api/teams?page=0&size=5"
curl -X GET "http://localhost:8080/api/teams?page=1&size=2&sort=budget,asc"
```

###### [GET] /api/teams/{id}
```
curl -X GET "http://localhost:8080/api/teams/1"
```
