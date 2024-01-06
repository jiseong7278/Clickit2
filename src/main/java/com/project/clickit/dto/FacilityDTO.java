package com.project.clickit.dto;

import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.FacilityEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FacilityDTO {
    private String id;
    private String name;
    private String info;
    private Integer open;
    private Integer close;
    private String img;
    private String dormitoryId;

    public FacilityDTO(String id, String name, String info, Integer open,
                       Integer close, String img) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.open = open;
        this.close = close;
        this.img = img;
    }

    @Builder
    public FacilityDTO(String id, String name, String info, Integer open,
                       Integer close, String img, String dormitoryId) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.open = open;
        this.close = close;
        this.img = img;
        this.dormitoryId = dormitoryId;
    }

    public FacilityEntity toEntity() {
        return FacilityEntity.builder()
                .id(this.id)
                .name(this.name)
                .info(this.info)
                .open(this.open)
                .close(this.close)
                .img(this.img)
                .dormitoryEntity(DormitoryEntity.builder()
                        .id(this.dormitoryId)
                        .build())
                .build();
    }
}
