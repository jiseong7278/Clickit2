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

    @Builder
    public DormitoryDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DormitoryEntity toEntity() {
        return DormitoryEntity.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
