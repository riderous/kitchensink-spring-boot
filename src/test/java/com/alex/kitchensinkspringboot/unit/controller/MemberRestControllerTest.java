package com.alex.kitchensinkspringboot.unit.controller;

import com.alex.kitchensinkspringboot.controller.MemberRestController;
import com.alex.kitchensinkspringboot.dto.MemberCreateDTO;
import com.alex.kitchensinkspringboot.dto.MemberDTO;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import com.alex.kitchensinkspringboot.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberRestController.class) // ✅ Load only the web layer
class MemberRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc; // ✅ Simulates HTTP requests

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private MemberRepository memberRepository;

    private List<MemberDTO> mockMembers;

    @BeforeEach
    void setUp() {
        mockMembers = List.of(
                new MemberDTO("2L", "Jane Smith", "jane.smith@example.com", "0987654321"),
                new MemberDTO("1L", "John Doe", "john.doe@example.com", "1234567890")
        );
    }

    @Test
    void testListAllMembers() throws Exception {
        when(memberService.getAllMembers()).thenReturn(mockMembers);

        mockMvc.perform(get("/rest/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].name").value("John Doe"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetMemberById() throws Exception {
        MemberDTO member = mockMembers.getFirst();
        when(memberService.getMemberById(member.id())).thenReturn(Optional.of(member));

        mockMvc.perform(get("/rest/members/" + member.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(member.name()))
                .andExpect(jsonPath("$.email").value(member.email()));
    }

    @Test
    void testGetMemberById_NotFound() throws Exception {
        when(memberService.getMemberById("42")).thenReturn(Optional.empty());
        mockMvc.perform(get("/rest/members/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMember() throws Exception {
        MemberDTO member = mockMembers.getFirst();
        when(
                memberService.register(new MemberCreateDTO(member.name(), member.email(), member.phoneNumber())))
                .thenReturn(new MemberDTO("1L", member.name(), member.email(), member.phoneNumber()));

        mockMvc.perform(post("/rest/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(member.name()))
                .andExpect(jsonPath("$.email").value(member.email()));

    }
}