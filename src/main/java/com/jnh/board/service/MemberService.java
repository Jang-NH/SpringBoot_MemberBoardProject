package com.jnh.board.service;

import com.jnh.board.dto.MemberDetailDTO;
import com.jnh.board.dto.MemberLoginDTO;
import com.jnh.board.dto.MemberSaveDTO;
import com.jnh.board.dto.MemberUpdateDTO;

import java.io.IOException;
import java.util.List;

public interface MemberService{
    Long save(MemberSaveDTO memberSaveDTO) throws IllegalStateException, IOException;

    String emailDuplicate(String memberEmail);

    boolean login(MemberLoginDTO memberLoginDTO);

    List<MemberDetailDTO> findAll();

    MemberDetailDTO findById(Long memberId);

    void deleteById(Long memberId);

    MemberDetailDTO findByMemberEmail(String memberEmail);

    Long update(MemberUpdateDTO memberUpdateDTO) throws IllegalStateException, IOException;
}
