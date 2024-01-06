package com.project.clickit.dto;

import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DormitoryDTO {
    private String id;
    private String name;
    private List<MemberDTO> memberDTO;

    @Builder
    public DormitoryDTO(String id, String name, List<MemberDTO> memberDTO) {
        this.id = id;
        this.name = name;
        this.memberDTO = memberDTO;
    }

    public DormitoryEntity toEntity() {

        List<MemberEntity> memberEntityList = new ArrayList<>();

        for (MemberDTO memberDTO : this.memberDTO) {
            memberEntityList.add(memberDTO.toEntity());
        }

        return DormitoryEntity.builder()
                .id(this.id)
                .name(this.name)
                .memberEntity(memberEntityList)
                .build();
    }
}
