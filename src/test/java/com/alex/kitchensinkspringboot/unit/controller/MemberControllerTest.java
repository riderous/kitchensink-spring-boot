package com.alex.kitchensinkspringboot.unit.controller;

import com.alex.kitchensinkspringboot.controller.MemberController;
import com.alex.kitchensinkspringboot.dto.MemberCreateDTO;
import com.alex.kitchensinkspringboot.dto.MemberDTO;
import com.alex.kitchensinkspringboot.repository.MemberRepository;
import com.alex.kitchensinkspringboot.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class) // ✅ Load only the web layer
class MemberControllerTest {

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
                new MemberDTO("1L", "Alice", "alice@example.com", "1234567890"),
                new MemberDTO("2L", "Bob", "bob@example.com", "1234567890")
        );
    }

    // ✅ Test GET /members
    @Test
    void testListAllMembers() throws Exception {
        when(memberService.getAllMembers()).thenReturn(mockMembers);

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk()) // ✅ Expect HTTP 200
                .andExpect(view().name("index")) // ✅ Expect Thymeleaf view "index"
                .andExpect(model().attributeExists("members", "newMember")) // ✅ Check model attributes
                .andExpect(model().attribute("members", mockMembers)); // ✅ Verify members list
    }

    // ✅ Test POST /members/register - Successful Registration
    @Test
    void testCreateMember_Success() throws Exception {
        MemberCreateDTO validMember = new MemberCreateDTO("John Doe", "john@example.com", "1234567890");

        when(memberService.register(Mockito.any(MemberCreateDTO.class))).thenReturn(new MemberDTO("1L",
                validMember.name(), validMember.email(), validMember.phoneNumber()));

        mockMvc.perform(post("/members/register")
                        .param("name", validMember.name())
                        .param("email", validMember.email())
                        .param("phoneNumber", validMember.phoneNumber()))
                .andExpect(status().is3xxRedirection()) // ✅ Expect Redirect (302)
                .andExpect(redirectedUrl("/members")); // ✅ Redirects to members list
    }

    // ✅ Test POST /members/register - Validation Failure (Missing Name)
    @Test
    void testCreateMember_ValidationFailure() throws Exception {
        MemberCreateDTO invalidMember = new MemberCreateDTO("", "john@example.com", "1234567890");

        mockMvc.perform(post("/members/register")
                        .param("name", invalidMember.name())
                        .param("email", invalidMember.email())
                        .param("phoneNumber", invalidMember.phoneNumber()))
                .andExpect(status().isOk()) // ✅ Expect page reload (not redirect)
                .andExpect(view().name("index")) // ✅ Renders "index" view
                .andExpect(model().attributeExists("newMember", "members")) // ✅ Model contains attributes
                .andExpect(MockMvcResultMatchers.model()
                        .attributeHasFieldErrors("newMember", "name")); // ✅ Expect validation error on "name"
    }
}
