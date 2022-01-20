package com.jnh.board.service;

import com.jnh.board.dto.MemberSaveDTO;

import java.io.IOException;

public interface MemberService{
    Long save(MemberSaveDTO memberSaveDTO) throws IllegalStateException, IOException;

    String emailDuplicate(String memberEmail);
}
