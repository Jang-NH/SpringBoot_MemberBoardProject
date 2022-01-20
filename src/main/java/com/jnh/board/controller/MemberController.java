package com.jnh.board.controller;

import com.jnh.board.dto.MemberLoginDTO;
import com.jnh.board.dto.MemberSaveDTO;
import com.jnh.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.jnh.board.common.LOGIN_EMAIL;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService ms;

    // 회원가입 폼
    @GetMapping("/save")
    public String saveForm() {
        return "member/save";
    }

    // 회원가입 처리 (프로필사진 첨부)
    @PostMapping("/save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO) throws IllegalStateException, IOException {
        ms.save(memberSaveDTO);
        return "member/login";
    }

    // 이메일 중복 체크
    @PostMapping("/emailDuplicate")
    public @ResponseBody String emailDuplicate(@RequestParam String memberEmail) {
        String result = ms.emailDuplicate(memberEmail);
        return result;
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session) {
        boolean loginResult = ms.login(memberLoginDTO);
        if(loginResult) {
            session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
            String redirectURL = (String) session.getAttribute("redirectURL");

            if(redirectURL != null) {
                return "redirect:" + redirectURL;
            } else {
                return "redirect:/";
            }
        } else {
            return "member/login";
        }
    }
}
