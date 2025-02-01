package com.alex.kitchensinkspringboot.repository;


import com.alex.kitchensinkspringboot.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String> {
    List<Member> findAllByOrderByNameAsc();

    Optional<Member> findByEmail(String email);
}