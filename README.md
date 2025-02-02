# Kitchensink app

Jboss EAP migration to Java 21 with Spring Boot and MongoDB.

## Running the project

### Prerequisites
- Docker
- Maven for testing

### Run project
Starts the spring boot app and a MongoDB instance (configured for mongock).
```bash
docker-compose up --build
```

### Testing
- UI: http://localhost:8081/members
- running unit and integration tests: 
```bash
mvn verify -PallTests
```

#### Remove MongoDB volume
To test out the migration you can remove the mongo volume:
```bash
docker compose down
docker volume rm kitchensink-spring-boot_mongo_data
```

#### Against the running legacy app
It's also possible to run the REST integration tests against the running legacy Jboss application:
```bash
mvn test -PallTests -Dspring.profiles.active=legacy-test
```

# Development Notes
- No delete method in the legacy app, making testing harder.
- Preserved error messages and responses to match the old app.
- Returned 400 instead of 409 to simplify validation logic.
- Kept frontend monolithic for simplicity and consistency with the original structure.
- Migration approach:
  - First migrated using H2 database with tests.
  - Then switched to MongoDB (see commit history).
  - H2 dependencies retained for migration support.
- Scalability Considerations:
  - Batch processing needed for large dataset migrations.
  - Index added on name for faster sorting (findAllByOrderByNameAsc).
  - No pagination.
- Future Improvements:
  - Add auth and better configuration.
  - Expand test coverage with more integration and edge case tests.
  - Add metrics for observability.
