package com.jnh.board.service;

import com.jnh.board.dto.MemberDetailDTO;
import com.jnh.board.dto.MemberLoginDTO;
import com.jnh.board.dto.MemberSaveDTO;
import com.jnh.board.dto.MemberUpdateDTO;
import com.jnh.board.entity.MemberEntity;
import com.jnh.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository mr;

    @Override
    public Long save(MemberSaveDTO memberSaveDTO) throws IllegalStateException, IOException {

        MultipartFile memberProfile = memberSaveDTO.getMemberProfile();
        String memberProfileName = memberProfile.getOriginalFilename();
        memberProfileName = System.currentTimeMillis() + "-" + memberProfileName;
        String savePath = "C:\\development_jnh\\SpringBoot\\MemberBoardProject\\src\\main\\resources\\static\\profile\\"+memberProfileName;

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

    @Override
    public boolean login(MemberLoginDTO memberLoginDTO) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberLoginDTO.getMemberEmail());
        if(memberEntity != null) {
            if(memberEntity.getMemberPassword().equals(memberLoginDTO.getMemberPassword())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<MemberDetailDTO> findAll() {
        List<MemberEntity> memberEntityList = mr.findAll();
        List<MemberDetailDTO> memberList = new ArrayList<>();
        for (MemberEntity m: memberEntityList) {
            memberList.add(MemberDetailDTO.toMemberDetailDTO(m)); // toMemberDetailDTO() : MemberEntity -> MemberDetailDTO 메서드
        }
        return memberList;
    }

    @Override
    public MemberDetailDTO findById(Long memberId) {
        Optional<MemberEntity> memberEntityOptional = mr.findById(memberId);
        MemberEntity memberEntity = memberEntityOptional.get();
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        return memberDetailDTO;
    }

    @Override
    public void deleteById(Long memberId) {
        mr.deleteById(memberId);
    }

    @Override
    public MemberDetailDTO findByMemberEmail(String memberEmail) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberEmail);
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        return memberDetailDTO;
    }

    @Override
    public Long update(MemberUpdateDTO memberUpdateDTO) throws IllegalStateException, IOException{

        MultipartFile memberProfile = memberUpdateDTO.getMemberProfile();
        String memberProfileName = memberProfile.getOriginalFilename();
        memberProfileName = System.currentTimeMillis() + "-" + memberProfileName;
        String savePath = "C:\\development_jnh\\SpringBoot\\MemberBoardProject\\src\\main\\resources\\static\\profile\\"+memberProfileName;

        if(!memberProfile.isEmpty()) {
            memberProfile.transferTo(new File(savePath));
        }

        memberUpdateDTO.setMemberProfileName(memberProfileName);

        MemberEntity memberEntity = MemberEntity.toUpdateMember(memberUpdateDTO);
        Long memberId = mr.save(memberEntity).getId();
        return memberId;
    }


}
