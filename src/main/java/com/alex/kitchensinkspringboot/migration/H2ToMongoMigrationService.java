package com.alex.kitchensinkspringboot.migration;

import com.alex.kitchensinkspringboot.model.Member;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class H2ToMongoMigrationService {

    private final JdbcTemplate jdbcTemplate; // ✅ Reads H2 data
    private final MemberRepository memberRepository; // ✅ Saves to MongoDB

    public void migrate() {
        // Step 1: Read all members from H2
        List<Member> members = jdbcTemplate.query(
                "SELECT name, email, phone_number FROM MEMBER",
                (rs, rowNum) -> Member.builder()
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phone_number"))
                        .build()
        );

        // Step 2: Save them into MongoDB
        memberRepository.saveAll(members);

        log.info("✅ Migrated {} members from H2 to MongoDB.", members.size());
    }
}