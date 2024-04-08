package com.project.clickit.dto;

import com.project.clickit.entity.FacilityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDTO {
    private String id;
    private String name;
    private String info;
    private Integer open;
    private Integer close;
    private String img;
    private String terms;
    private Integer extensionLimit;
    private DormitoryDTO dormitoryDTO;

    public FacilityDTO(String id, String name, String info, Integer open,
                       Integer close, String img, String terms, Integer extensionLimit) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.open = open;
        this.close = close;
        this.img = img;
        this.terms = terms;
        this.extensionLimit = extensionLimit;
    }


    public FacilityEntity toEntity() {
        return FacilityEntity.builder()
                .id(this.id)
                .name(this.name)
                .info(this.info)
                .open(this.open)
                .close(this.close)
                .img(this.img)
                .terms(this.terms)
                .extensionLimit(this.extensionLimit)
                .dormitoryEntity(this.dormitoryDTO.toEntity())
                .build();
    }
}
