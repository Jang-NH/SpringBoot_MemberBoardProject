package com.jnh.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveDTO {
    private String boardWriter;
    private String boardTitle;
    private String boardContents;
    private MultipartFile boardFile;
    private String boardFileName;

}
