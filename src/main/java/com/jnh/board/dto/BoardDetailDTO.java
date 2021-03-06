package com.jnh.board.dto;

import com.jnh.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDTO {
    private Long boardId;
    private String boardWriter;
    private String boardTitle;
    private String boardContents;
    private MultipartFile boardFile;
    private String boardFileName;
    private LocalDateTime createTime;

    public static BoardDetailDTO toBoardDetailDTO(BoardEntity boardEntity) {
    BoardDetailDTO boardDetailDTO = new BoardDetailDTO();
    boardDetailDTO.setBoardId(boardEntity.getId());
    boardDetailDTO.setBoardWriter(boardEntity.getBoardWriter());
    boardDetailDTO.setBoardTitle(boardEntity.getBoardTitle());
    boardDetailDTO.setBoardContents(boardEntity.getBoardContents());
    boardDetailDTO.setBoardFileName(boardDetailDTO.getBoardFileName());
    return boardDetailDTO;
    }
}
