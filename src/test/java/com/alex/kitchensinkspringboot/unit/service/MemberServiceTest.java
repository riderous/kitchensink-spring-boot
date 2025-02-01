package com.alex.kitchensinkspringboot.service;

import com.alex.kitchensinkspringboot.dto.MemberCreateDTO;
import com.alex.kitchensinkspringboot.dto.MemberDTO;
import com.alex.kitchensinkspringboot.model.Member;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Enables Mockito in JUnit 5
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository; // ✅ Mock repository (no real DB calls)

    @InjectMocks
    private MemberService memberService; // ✅ Inject mocked repository into service

    private Member member;

    @BeforeEach
    void setUp() {
        // ✅ Sample Member
        member = Member.builder()
                .id("1L")
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    // ✅ Test Case: Create Member Successfully
    @Test
    void testCreateMember_Success() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // Act
        MemberDTO savedMember = memberService.register(new MemberCreateDTO(
                "John Doe", "john.doe@example.com", "1234567890"
        ));

        // Assert
        assertNotNull(savedMember);
        assertEquals("John Doe", savedMember.name());
        assertEquals("john.doe@example.com", savedMember.email());

        // Verify: `save()` was called once
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    // ✅ Test Case: Get All Members
    @Test
    void testGetAllMembers() {
        // Mock: Return a list of members
        when(memberRepository.findAllByOrderByNameAsc()).thenReturn(List.of(member));

        // Act
        List<MemberDTO> members = memberService.getAllMembers();

        // Assert
        assertFalse(members.isEmpty());
        assertEquals(1, members.size());
        assertEquals("John Doe", members.getFirst().name());

        // Verify: `findAll()` was called once
        verify(memberRepository, times(1)).findAllByOrderByNameAsc();
    }

    // ✅ Test Case: Get Member By ID (Found)
    @Test
    void testGetMemberById_Found() {
        // Mock: Member exists
        when(memberRepository.findById("1L")).thenReturn(Optional.of(member));

        // Act
        Optional<MemberDTO> foundMember = memberService.getMemberById("1L");

        // Assert
        assertTrue(foundMember.isPresent());
        assertEquals("John Doe", foundMember.get().name());

        // Verify: `findById()` was called once
        verify(memberRepository, times(1)).findById("1L");
    }

    // ✅ Test Case: Get Member By ID (Not Found)
    @Test
    void testGetMemberById_NotFound() {
        // Mock: Member not found
        when(memberRepository.findById("1L")).thenReturn(Optional.empty());

        // Act
        Optional<MemberDTO> foundMember = memberService.getMemberById("1L");

        // Assert
        assertFalse(foundMember.isPresent());

        // Verify: `findById()` was called once
        verify(memberRepository, times(1)).findById("1L");
    }
}