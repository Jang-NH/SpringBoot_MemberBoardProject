package com.jnh.board.controller;

import com.jnh.board.common.PagingConst;
import com.jnh.board.dto.*;
import com.jnh.board.service.BoardService;
import com.jnh.board.service.CommentService;
import com.jnh.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.jnh.board.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final MemberService ms;
    private final BoardService bs;
    private final CommentService cs;

    // 글작성 폼
    @GetMapping("/save")
    public String saveForm(@Validated @ModelAttribute("member") MemberLoginDTO memberLoginDTO, Model model, HttpSession session) {
        session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
        model.addAttribute("board", new BoardSaveDTO());
        return "board/save";
    }

    // 글작성 처리 (파일 첨부)
    @PostMapping("/save")
    public String save(@Validated @ModelAttribute("board") BoardSaveDTO boardSaveDTO) throws IllegalStateException, IOException { // ModelAttribute 생략 가능(Timeleaf 사용하면 필수 작성!)
        bs.save(boardSaveDTO);
        return "redirect:/board";
    }

    // 페이징 처리 (/board?page=5) -> 글이 추가되면 페이지에 해당하는 글이 바뀜(페이지 : 고유 정보 아님)으로 Query String(주소값 뒤에 물음표)을 쓰는 것이 좋다.
    // restful한 주소(주소만으로 뭘하고 싶은지 알 수 있음)로 5번 글(글 : 고유 정보) 확인 (/board/5)
    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model, HttpSession session) { // page defailt(기본) 값 : 1
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO member = ms.findByMemberEmail(memberEmail);
        model.addAttribute("member", member);

        Page<BoardPagingDTO> boardList = bs.paging(pageable);
        model.addAttribute("boardList", boardList);

        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "board/paging";
    }

    // 검색
    @GetMapping ("/search")
    public String search(@PageableDefault(page = 1) Pageable pageable, @RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword, Model model) {
        Page<BoardPagingDTO> boardList = bs.search(searchType, keyword, pageable);
        model.addAttribute("boardList", boardList);

        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT-1)< boardList.getTotalPages())?startPage + PagingConst.BLOCK_LIMIT -1 : boardList.getTotalPages();
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "redirect:/board";
    }

    // 상세 조회 (get, /board/{boardId})
    @GetMapping("/{boardId}")
    public String findById(@PathVariable Long boardId, Model model) {
        log.info("글보기 메서드 호출. 요청 글번호 : {}", boardId); // 변수 출력 원할 시 {} 반드시 필요!
        BoardDetailDTO board = bs.findById(boardId);
        model.addAttribute("board", board);
        List<CommentDetailDTO> commentList = cs.findAll(boardId);
        model.addAttribute("commentList", commentList);
        return "board/findById";
    }

    // 글 수정 폼
    @GetMapping("/update/{boardId}")
    public String updateForm(@PathVariable Long boardId, Model model) {
        BoardDetailDTO board = bs.findById(boardId);
        model.addAttribute("board", board);
        return "board/update";
    }

    // 글 수정 처리 (put)
    @PutMapping("/{boardId}")
    public String update(@ModelAttribute BoardUpdateDTO boardUpdateDTO) throws IllegalStateException, IOException {
        bs.update(boardUpdateDTO);
        return "redirect:/board/" + boardUpdateDTO.getBoardId();
    }

    // 글 삭제 (delete)
    @DeleteMapping("/{boardId}")
    public String deleteById(@PathVariable Long boardId) {
        bs.deleteById(boardId);
        return "redirect:/board";
    }
}
