package com.alex.kitchensinkspringboot.controller;

import com.alex.kitchensinkspringboot.dto.MemberCreateDTO;
import com.alex.kitchensinkspringboot.dto.MemberDTO;
import com.alex.kitchensinkspringboot.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/members")
@AllArgsConstructor
@Validated
public class MemberRestController {
    private final MemberService memberService; // ✅ Use service instead of repository

    @GetMapping
    public List<MemberDTO> listAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable String id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberCreateDTO memberCreateDTO) {
        MemberDTO savedMember = memberService.register(memberCreateDTO);
        return ResponseEntity.status(200).body(savedMember); // ✅ Use 200 to match old API
    }
}