package com.project.clickit.dto;

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
    private FacilityDTO facilityDTO;

    /**
     * <b>SeatEntity로 변환</b>
     * @return SeatEntity
     */
    public SeatEntity toEntity() {
        if (this.facilityDTO == null) {
            return SeatEntity.builder()
                    .id(this.id)
                    .name(this.name)
                    .time(this.time)
                    .isEmpty(this.isEmpty)
                    .build();
        }
        return SeatEntity.builder()
                .id(this.id)
                .name(this.name)
                .time(this.time)
                .isEmpty(this.isEmpty)
                .facilityEntity(this.facilityDTO.toEntity())
                .build();
    }
}
