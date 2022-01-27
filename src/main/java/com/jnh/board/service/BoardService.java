package com.jnh.board.service;

import com.jnh.board.dto.BoardDetailDTO;
import com.jnh.board.dto.BoardPagingDTO;
import com.jnh.board.dto.BoardSaveDTO;
import com.jnh.board.dto.BoardUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    Long save(BoardSaveDTO boardSaveDTO) throws IllegalStateException, IOException;

    Page<BoardPagingDTO> paging(Pageable pageable);

    BoardDetailDTO findById(Long boardId);

    void update(BoardUpdateDTO boardUpdateDTO) throws IllegalStateException, IOException;

    void deleteById(Long boardId);

    Page<BoardPagingDTO> search(String searchType, String keyword, Pageable pageable);
}
