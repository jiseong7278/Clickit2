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
    private DormitoryDTO dormitoryDTO;

    /**
     * <b>FacilityEntity로 변환</b>
     * @return FacilityEntity
     */
    public FacilityEntity toEntity() {
        return FacilityEntity.builder()
                .id(this.id)
                .name(this.name)
                .info(this.info)
                .open(this.open)
                .close(this.close)
                .img(this.img)
                .terms(this.terms)
                .dormitoryEntity(this.dormitoryDTO.toEntity())
                .build();
    }
}
