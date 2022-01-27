package com.jnh.board.service;

import com.jnh.board.common.PagingConst;
import com.jnh.board.dto.BoardDetailDTO;
import com.jnh.board.dto.BoardPagingDTO;
import com.jnh.board.dto.BoardSaveDTO;
import com.jnh.board.dto.BoardUpdateDTO;
import com.jnh.board.entity.BoardEntity;
import com.jnh.board.entity.MemberEntity;
import com.jnh.board.repository.BoardRepository;
import com.jnh.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository br;
    private final MemberRepository mr;

    @Override
    public Long save(BoardSaveDTO boardSaveDTO) throws IllegalStateException, IOException {

        MultipartFile boardFile = boardSaveDTO.getBoardFile();
        String boardProfileName = boardFile.getOriginalFilename();
        boardProfileName = System.currentTimeMillis() + "-" + boardProfileName;
        String savePath = "C:\\development_jnh\\SpringBoot\\MemberBoardProject\\src\\main\\resources\\static\\upload\\"+boardProfileName;

        if(!boardFile.isEmpty()) {
            boardFile.transferTo(new File(savePath));
        }

        boardSaveDTO.setBoardFileName(boardProfileName);

        MemberEntity memberEntity = mr.findByMemberEmail(boardSaveDTO.getBoardWriter());
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardSaveDTO, memberEntity);
        Long boardId = br.save(boardEntity).getId();
        return boardId;
    }

    @Override
    @Transactional
    public Page<BoardPagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber(); // page 번호 가져옴
        // 요청한 페이지가 1이면 페이지값을 0으로 하고 1이 아니면 요청 페이지에서 1을 뺀다.
        page = (page == 1)? 0 : (page -1);
        Page<BoardEntity> boardEntities = br.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));// 요청페이지, 한페이지당 글갯수(PagingConst에 지정해둠), 내림차순, id (Entity에 지정한 필드 이름, 언더바 인식 불가)
        // Page<BoardEntity> -> Page<BoardPagingDTO>
        // map() : 엔티티가 담긴 페이지 객체를 dto가 담긴 페이지객체로 변환해주는 역할 (converter)
        Page<BoardPagingDTO> boardList = boardEntities.map(
                board -> new BoardPagingDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle())
        );
        return boardList;
    }

    @Override
    public BoardDetailDTO findById(Long boardId) {
        Optional<BoardEntity> optionalBoardEntity = br.findById(boardId); // Optional 객체에 BoardEntity 담음

        BoardDetailDTO boardDetailDTO = null;
        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            boardDetailDTO = BoardDetailDTO.toBoardDetailDTO(boardEntity);
        }
        return boardDetailDTO;
    }

    @Override
    public void update(BoardUpdateDTO boardUpdateDTO) throws IllegalStateException, IOException {
        MultipartFile boardFile = boardUpdateDTO.getBoardFile();
        String boardProfileName = boardFile.getOriginalFilename();
        boardProfileName = System.currentTimeMillis() + "-" + boardProfileName;
        String savePath = "C:\\development_jnh\\SpringBoot\\MemberBoardProject\\src\\main\\resources\\static\\upload\\"+boardProfileName;

        if(!boardFile.isEmpty()) {
            boardFile.transferTo(new File(savePath));
        }

        boardUpdateDTO.setBoardFileName(boardProfileName);
        BoardEntity boardEntity = BoardEntity.toUpdateBoard(boardUpdateDTO);
    }

    @Override
    public void deleteById(Long boardId) {
        br.deleteById(boardId);
    }

    @Override
    public Page<BoardPagingDTO> search(String searchType, String keyword, Pageable pageable) {
        int page = pageable.getPageNumber();
        page = (page == 1) ? 0 : (page - 1);

        Page<BoardEntity> searchEntity = null;
        br.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));


        if (searchType.equals("boardTitle")){
            searchEntity = br.findByBoardTitleContainingIgnoreCase(keyword,PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        } else {
            searchEntity = br.findByBoardWriterContainingIgnoreCase(keyword,PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        }

        Page<BoardPagingDTO> boardList = searchEntity.map(
                board -> new BoardPagingDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle())
        );
        return boardList;
    }
}
