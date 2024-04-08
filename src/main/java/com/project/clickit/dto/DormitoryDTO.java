package com.project.clickit.dto;

import com.project.clickit.entity.DormitoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryDTO {
    private String id;
    private String name;


    public DormitoryEntity toEntity() {
        return DormitoryEntity.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
