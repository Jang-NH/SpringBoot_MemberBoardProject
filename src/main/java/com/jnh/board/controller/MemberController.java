package com.jnh.board.controller;

import com.jnh.board.dto.MemberDetailDTO;
import com.jnh.board.dto.MemberLoginDTO;
import com.jnh.board.dto.MemberSaveDTO;
import com.jnh.board.dto.MemberUpdateDTO;
import com.jnh.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.jnh.board.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService ms;

    // 회원가입 폼
    @GetMapping("/save")
    public String saveForm(Model model) {
        model.addAttribute("member", new MemberSaveDTO());
        return "member/save";
    }

    // 회원가입 처리 (프로필사진 첨부)
    @PostMapping("/save")
    public String save(@Validated @ModelAttribute("member") MemberSaveDTO memberSaveDTO, BindingResult bindingResult) throws IllegalStateException, IOException {

        if(bindingResult.hasErrors()) {
            return "member/save";
        } else {
            ms.save(memberSaveDTO);
            return "member/login";
            }
        }

    // 이메일 중복 체크
    @PostMapping("/emailDuplicate")
    public @ResponseBody String emailDuplicate(@RequestParam String memberEmail) {
        String result = ms.emailDuplicate(memberEmail);
        return result;
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("member", new MemberSaveDTO());
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("member") MemberLoginDTO memberLoginDTO, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "member/login";
        } else {
            boolean loginResult = ms.login(memberLoginDTO);
            if (loginResult) {
                session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
                String redirectURL = (String) session.getAttribute("redirectURL");

                if (redirectURL != null) {
                    return "redirect:" + redirectURL;
                } else {
                    return "redirect:/board/paging";
                }
            } else {
                return "member/login";
            }
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 관리자 페이지 이동
    @GetMapping("/admin")
    public String admin(HttpSession session) {
        return "member/admin";
    }

    // 회원목록
    @GetMapping
    public String findAll(Model model) {
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList", memberList);
        return "member/findAll";
    }

    // 회원조회 (get, /member/5)
    @GetMapping("/{memberId}")
    public String findById(Model model, HttpSession session) {
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO member = ms.findByMemberEmail(memberEmail);
        model.addAttribute("member", member);
        return "member/myPage";
    }

    // 회원삭제 (/member/5)
    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteById(@PathVariable Long memberId) {
        ms.deleteById(memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 수정화면 요청
    @GetMapping("/update")
    public String updateForm(Model model, HttpSession session) {
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO member = ms.findByMemberEmail(memberEmail);
        model.addAttribute("member", member);
        return "member/update";
    }

    // 수정처리 (put)
    @PutMapping("/{memberId}")
    // json 으로 데이터가 전달되면 @RequestBody로 받아줘야함.
    public ResponseEntity update(@ModelAttribute MemberUpdateDTO memberUpdateDTO) throws IllegalStateException, IOException {
        ms.update(memberUpdateDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

}
