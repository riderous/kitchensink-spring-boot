package com.alex.kitchensinkspringboot.migration;

import com.alex.kitchensinkspringboot.model.Member;
import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Slf4j
@ChangeUnit(id = "create-index-on-name", order = "002")
@RequiredArgsConstructor
public class MemberNameIndexChangeUnit {

    public static final String NAME_INDEX_NAME = "member_name_asc";
    private final MongoTemplate mongoTemplate;

    @BeforeExecution
    public void createIndex() {
        mongoTemplate.indexOps(Member.class).ensureIndex(
                new Index().background().named(NAME_INDEX_NAME).on("name", Sort.Direction.ASC));
        log.info("✅ Created index on 'name'");
    }

    @RollbackBeforeExecution
    public void removeIndex() {
        mongoTemplate.indexOps("members").dropIndex(NAME_INDEX_NAME); // ✅ Removes the index if rollback is triggered
        log.info("🚨 Rolled back: Removed index on 'name'");
    }

    @Execution
    public void execute(final MongoTemplate mongoTemplate) {
        // omit.
    }

    @RollbackExecution
    public void executeRollback(final MongoTemplate mongoTemplate) {
        // omit.
    }
}