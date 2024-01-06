package com.project.clickit.dto;

import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.entity.SeatEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatDTO {
    private String id;
    private String name;
    private Integer time;
    private Boolean isEmpty;
    private String facilityId;

    public SeatDTO(String id, String name, Integer time, Boolean isEmpty) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.isEmpty = isEmpty;
    }

    @Builder
    public SeatDTO(String id, String name, Integer time, Boolean isEmpty, String facilityId) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.isEmpty = isEmpty;
        this.facilityId = facilityId;
    }

    public SeatEntity toEntity() {
        return SeatEntity.builder()
                .id(this.id)
                .name(this.name)
                .time(this.time)
                .isEmpty(this.isEmpty)
                .facilityEntity(FacilityEntity.builder()
                        .id(this.facilityId)
                        .build())
                .build();
    }
}
