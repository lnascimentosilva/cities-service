# Eurovision interview exercise

### Prerequisites:

You must have installed Docker in your OS.

### Running instructions:

`mvn clean package`

`docker-compose up`

_It may take a while to have the container up since it waits for mariadb readiness._

### Task chosen:
**a) Biggest Sequence**

### Accessing the endpoint
`curl -G --data "page=0" --data "size=30" http://localhost:8080/api/cities/queryLargestSequenceByPage`
