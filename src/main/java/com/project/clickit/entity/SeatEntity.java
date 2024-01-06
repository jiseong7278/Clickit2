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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_seat")
    private FacilityEntity facilityEntity;

    public SeatEntity(String id, String name, Integer time, Boolean isEmpty) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.isEmpty = isEmpty;
    }

    public SeatDTO toDTO() {
        return SeatDTO.builder()
                .id(this.id)
                .name(this.name)
                .time(this.time)
                .isEmpty(this.isEmpty)
                .facilityId(this.facilityEntity.getId())
                .build();
    }
}
