# RestApiH2
Java Rest Controller using embedded H2 DB

## Application Ports:
(develop)     http://localhost:8000/
(production)  http://localhost:9000/

Swagger Ui Url = /swagger-ui/index.html

## REST ENDPOINT SPECS
| Http Verb | Endpoint           | Function           |
| --------- | ------------------ | ------------------ |
| GET       |     api/users      | return all Users   |
| POST      |     api/users      | add User           |
| DELETE    |     api/users      | delete all User    |
| GET       |     api/users/{id} | return User by ID  |
| PUT       |     api/users/{id} | update User by ID  |
| DELETE    |     api/users/{id} | delete User by ID  |

## USER SCHEMA
| column   | type               | constraints        |
| -------- | ------------------ | ------------------ |
|id	       |integer($int64)     |PK, AUTOINCREMENT   |
|name	   |string              |NOT NULL            |
|vorname   |string              |                    |
|email	   |string              |UNIQUE, NOT NULL    |

## VALIDATION
name, vorname  - Name REGEX
email          - OWASP REGEX

## Maven Build JAR:
### Develop (Unit Tests active) (default)
```
mvn clean package -Pdev
```

### Production (Unit Tests disabled)
```
mvn clean package -Pprod
```

## Start Application
```
java -jar target/RestApiH2-1.0.jar 
```
or
```
mvn spring-boot:run
```
