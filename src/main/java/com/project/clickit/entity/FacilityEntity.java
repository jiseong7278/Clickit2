package com.project.clickit.entity;

import com.project.clickit.dto.FacilityDTO;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Table(name = "facility")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FacilityEntity {
    @Id
    @Column(name = "facility_id")
    private String id;

    @Column(name = "facility_name")
    private String name;

    @Column(name = "facility_info")
    private String info;

    @Column(name = "facility_open")
    private Integer open;

    @Column(name = "facility_close")
    private Integer close;

    @Column(name = "facility_capacity")
    private Integer capacity;

    @Column(name = "facility_img")
    private String img;

    @Column(name = "facility_terms")
    private String terms;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "facility_dormitory", insertable = false, updatable = false)
    private DormitoryEntity dormitoryEntity;

    /**
     * <b>FacilityDTO로 변환</b>
     * @return FacilityDTO
     */
    public FacilityDTO toDTO() {
        return FacilityDTO.builder()
                .id(this.id)
                .name(this.name)
                .info(this.info)
                .open(this.open)
                .close(this.close)
                .capacity(this.capacity)
                .img(this.img)
                .terms(this.terms)
                .dormitoryDTO(this.dormitoryEntity.toDTO())
                .build();
    }
}
