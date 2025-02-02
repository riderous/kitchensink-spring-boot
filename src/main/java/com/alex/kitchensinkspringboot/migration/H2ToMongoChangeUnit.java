package com.alex.kitchensinkspringboot.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChangeUnit(id = "migrate-h2-to-mongo", order = "001")
@RequiredArgsConstructor
public class H2ToMongoChangeUnit {

    private final MongoTemplate mongoTemplate;
    private final H2ToMongoMigrationService migrationService;

    @Execution
    public void executeMigration() {
        log.info("✅ Migrating data from H2 to MongoDB...");
        migrationService.migrate();
    }

    @RollbackExecution
    public void rollbackMigration() {
        log.info("🚨 Rolling back: Removing migrated data from MongoDB...");

        // Deletes all members that were migrated from H2
        mongoTemplate.getCollection("members").drop();

        log.info("✅ Rollback complete: MongoDB members collection deleted.");
    }
}