package com.alex.kitchensinkspringboot.unit.controller;

import com.alex.kitchensinkspringboot.model.Member;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MemberRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MemberRepository memberRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the full application context
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Clear and set up test data
        memberRepository.deleteAll();
        memberRepository.save(Member.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build());
        memberRepository.save(Member.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phoneNumber("0987654321")
                .build());
    }

    @Test
    void testListAllMembers() throws Exception {
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].name").value("John Doe"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetMemberById() throws Exception {
        Member member = memberRepository.findAll().getFirst(); // Retrieve the first member for testing

        mockMvc.perform(get("/api/members/" + member.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    void testGetMemberById_NotFound() throws Exception {
        mockMvc.perform(get("/api/members/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMember() throws Exception {
        String newMemberJson = """
                {
                    "name": "Alice Brown",
                    "email": "alice.brown@example.com",
                    "phoneNumber": "5551234567"
                }
                """;

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMemberJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Alice Brown"))
                .andExpect(jsonPath("$.email").value("alice.brown@example.com"));

        // Verify that the new member was added to the database
        assert memberRepository.count() == 3;
    }
    @Test
    void testCreateMember_DuplicateEmail() throws Exception {
        String newMemberJson = """
                {
                    "name": "Alice Brown",
                    "email": "alice.brown@example.com",
                    "phoneNumber": "5551234567"
                }
                """;

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMemberJson))
                .andExpect(status().is2xxSuccessful());


        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMemberJson))
                .andExpect(status().isConflict());

        assert memberRepository.count() == 3;
    }
}