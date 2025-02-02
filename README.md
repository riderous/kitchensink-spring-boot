Jboss EAP migration to Spring Boot.

# Steps to build

## Run project
docker-compose up --build

### Testing
http://localhost:8081/members
mvn test -PallTests

#### Against the running legacy app
mvn test -PallTests -Dspring.profiles.active=legacy-test

### Remove MongoDB volume
docker compose down
docker volume rm kitchensink-spring-boot_mongo_data


# Development Notes
- no delete method in the legacy app (makes it harder to test) 
- kept frontend monolithic
- first added tests and migrated using h2 db, then switch to MongoDB
- kept h2 and dependencies for the migration
- added index on name field to speed up findAllByOrderByNameAsc (has also drawbacks)
