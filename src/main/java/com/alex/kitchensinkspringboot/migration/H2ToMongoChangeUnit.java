package com.alex.kitchensinkspringboot.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChangeUnit(id="migrate-h2-to-mongo", order = "001")
@RequiredArgsConstructor
public class H2ToMongoChangeUnit {

    private final H2ToMongoMigrationService migrationService;

    @Execution
    public void executeMigration() {
        migrationService.migrate();
    }
    @RollbackExecution
    public void rollbackMigration() {
        log.info("Rolling back migration");
    }
}