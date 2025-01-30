package com.alex.kitchensinkspringboot.repository;


import com.alex.kitchensinkspringboot.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByOrderByNameAsc();
    Optional<Member> findByEmail(String email);
}