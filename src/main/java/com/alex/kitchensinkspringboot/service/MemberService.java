package com.alex.kitchensinkspringboot.service;


import com.alex.kitchensinkspringboot.dto.MemberCreateDTO;
import com.alex.kitchensinkspringboot.dto.MemberDTO;
import com.alex.kitchensinkspringboot.model.Member;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAllByOrderByNameAsc().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<MemberDTO> getMemberById(String id) {
        return memberRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional  // Replaces @Stateless for transaction management
    public MemberDTO register(MemberCreateDTO memberCreateDTO) {
        log.info("Registering member: {}", memberCreateDTO.name());

        Member member = Member.builder()
                .name(memberCreateDTO.name())
                .phoneNumber(memberCreateDTO.phoneNumber())
                .email(memberCreateDTO.email())
                .build();

        Member savedMember = memberRepository.save(member);
        return convertToDTO(savedMember);
    }

    private MemberDTO convertToDTO(Member member) {
        return new MemberDTO(member.getId(), member.getName(), member.getEmail(), member.getPhoneNumber());
    }
}