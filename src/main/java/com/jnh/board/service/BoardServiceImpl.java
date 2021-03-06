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
        int page = pageable.getPageNumber(); // page ?????? ?????????
        // ????????? ???????????? 1?????? ??????????????? 0?????? ?????? 1??? ????????? ?????? ??????????????? 1??? ??????.
        page = (page == 1)? 0 : (page -1);
        Page<BoardEntity> boardEntities = br.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));// ???????????????, ??????????????? ?????????(PagingConst??? ????????????), ????????????, id (Entity??? ????????? ?????? ??????, ????????? ?????? ??????)
        // Page<BoardEntity> -> Page<BoardPagingDTO>
        // map() : ???????????? ?????? ????????? ????????? dto??? ?????? ?????????????????? ??????????????? ?????? (converter)
        Page<BoardPagingDTO> boardList = boardEntities.map(
                board -> new BoardPagingDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle())
        );
        return boardList;
    }

    @Override
    public BoardDetailDTO findById(Long boardId) {
        Optional<BoardEntity> optionalBoardEntity = br.findById(boardId); // Optional ????????? BoardEntity ??????

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
