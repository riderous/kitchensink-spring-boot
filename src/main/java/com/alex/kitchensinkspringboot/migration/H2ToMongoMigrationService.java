package com.alex.kitchensinkspringboot.migration;

import com.alex.kitchensinkspringboot.model.Member;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class H2ToMongoMigrationService {

    private final JdbcTemplate jdbcTemplate; // ✅ Reads H2 data
    private final MemberRepository memberRepository; // ✅ Saves to MongoDB

    public void migrate() {
        log.info("✅ Starting migration...");

        jdbcTemplate.query("SELECT name, email, phone_number FROM MEMBER",
                rs -> {
                    Member member = Member.builder()
                            .name(rs.getString("name"))
                            .email(rs.getString("email"))
                            .phoneNumber(rs.getString("phone_number"))
                            .build();

                    memberRepository.save(member); // ✅ Saves each record immediately
                    log.debug("✅ Migrated: {}", member.getEmail());
                }
        );

        log.info("🚀 Migration completed!");
    }
}