# Team REST API
Create a Rest API that will have 2 methods:

- [x] the 1st one will return a list of teams. Each list will have a list of players. 
    The list will be paginated and sortable server side (sortable by team name, acronym and budget).
- [x] The 2nd one will permit to add a team with players, or not (all the other fields are required)

Technical environment:
- Spring
- Hibernate
- Database: H2 (Embedded)

## Example of data model
```
Team:   [id, name, acronym, players, budget]
Player: [id, name, position]
```

---

## How to run
#### using docker-compose
**REQUIRED: Docker running**

###### Starting
```
./gradlew clean build
docker-compose up --build -d
```

###### Closing
```
docker-compose down --remove-orphans
```

#### using gradle wrapper
```
./gradlew clean bootRun
```

## How to use endpoints

### by swagger
* `http://localhost:4326/v2/api-docs` - API Docs [JSON]
* `http://localhost:4326/swagger-ui.html` - Swagger UI
### by curl

###### [POST] /api/teams
```
curl -X POST "http://localhost:4326/api/teams" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"name\": \"Olympique Gymnaste Club Nice\", \"acronym\": \"OGC\", \"budget\": 182005000, \"players\": [ { \"name\": \"Youcef Atal\", \"position\": \"RIGHT_FULLBACK\" } ]}"
```

###### [GET] /api/teams
```
curl -X GET "http://localhost:4326/api/teams
curl -X GET "http://localhost:4326/api/teams?page=0&size=5"
curl -X GET "http://localhost:4326/api/teams?page=1&size=2&sort=budget,asc"
```

###### [GET] /api/teams/{id}
```
curl -X GET "http://localhost:4326/api/teams/1"
```
