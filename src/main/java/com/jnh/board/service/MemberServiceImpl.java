package com.jnh.board.service;

import com.jnh.board.dto.MemberSaveDTO;
import com.jnh.board.entity.MemberEntity;
import com.jnh.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository mr;

    @Override
    public Long save(MemberSaveDTO memberSaveDTO) throws IllegalStateException, IOException {

        MultipartFile memberProfile = memberSaveDTO.getMemberProfile();
        String memberProfileName = memberProfile.getOriginalFilename();
        memberProfileName = System.currentTimeMillis() + "-" + memberProfileName;
        String savePath = "C:\\development_jnh\\SpringBoot\\MemberBoardProject\\src\\main\\resources" + memberProfileName;

        if(!memberProfile.isEmpty()) {
            memberProfile.transferTo(new File(savePath));
        }

        memberSaveDTO.setMemberProfileName(memberProfileName);

        MemberEntity memberEntity = MemberEntity.toSaveMember(memberSaveDTO);

        return mr.save(memberEntity).getId();
    }

    @Override
    public String emailDuplicate(String memberEmail) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberEmail);
        if(memberEntity == null)
            return "ok";
        else
            return "no";
    }
}
