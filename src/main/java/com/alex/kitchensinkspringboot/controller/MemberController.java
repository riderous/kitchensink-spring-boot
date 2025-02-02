package com.alex.kitchensinkspringboot.controller;

import com.alex.kitchensinkspringboot.dto.MemberCreateDTO;
import com.alex.kitchensinkspringboot.dto.MemberDTO;
import com.alex.kitchensinkspringboot.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String listAllMembers(Model model) {
        List<MemberDTO> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("newMember", MemberCreateDTO.empty());
        return "index";
    }

    @PostMapping("/register")
    public String createMember(@Valid @ModelAttribute("newMember") MemberCreateDTO memberCreateDTO,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("members", memberService.getAllMembers());
            return "index";
        }

        memberService.register(memberCreateDTO);
        return "redirect:/members";
    }
}