package com.project.clickit.entity;

import com.project.clickit.dto.SeatDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Table(name = "seat")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SeatEntity {
    @Id
    @Column(name = "seat_id")
    private String id;

    @Column(name = "seat_name")
    private String name;

    @Column(name = "seat_time")
    private Integer time;

    @Column(name = "seat_empty")
    private Boolean isEmpty;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "facility_seat", insertable = false, updatable = false)
    private FacilityEntity facilityEntity;

    /**
     * <b>SeatDTO로 변환</b>
     * @return SeatDTO
     */
    public SeatDTO toDTO() {
        if(this.facilityEntity == null) {
            return SeatDTO.builder()
                    .id(this.id)
                    .name(this.name)
                    .time(this.time)
                    .isEmpty(this.isEmpty)
                    .build();
        }
        return SeatDTO.builder()
                .id(this.id)
                .name(this.name)
                .time(this.time)
                .isEmpty(this.isEmpty)
                .facilityDTO(this.facilityEntity.toDTO())
                .build();
    }
}
