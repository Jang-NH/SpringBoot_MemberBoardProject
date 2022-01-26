package com.jnh.board.repository;

import com.jnh.board.dto.BoardDetailDTO;
import com.jnh.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BoardRepository extends JpaRepository <BoardEntity, Long> {
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id = :boardId")
    int boardHits(@Param("boardId") Long boardId);

    List<BoardDetailDTO> search(Map<String, String> searchParam);

    Page<BoardEntity> findByBoardTitleContainingIgnoreCase(String keyword, PageRequest id);

    Page<BoardEntity> findByBoardWriterContainingIgnoreCase(String keyword, PageRequest id);
}
