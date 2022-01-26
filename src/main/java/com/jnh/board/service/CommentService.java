package com.jnh.board.service;

import com.jnh.board.dto.CommentDetailDTO;
import com.jnh.board.dto.CommentSaveDTO;

import java.util.List;

public interface CommentService {
    Long save(CommentSaveDTO commentSaveDTO);

    List<CommentDetailDTO> findAll(Long boardId);
}
