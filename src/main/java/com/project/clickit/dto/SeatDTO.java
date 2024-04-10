package com.project.clickit.dto;

import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.entity.SeatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {
    private String id;
    private String name;
    private Integer time;
    private Boolean isEmpty;
    private String facilityId;

    /**
     * <b>SeatEntity로 변환</b>
     * @return SeatEntity
     */
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
