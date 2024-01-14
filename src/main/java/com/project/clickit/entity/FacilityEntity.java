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

    @Column(name = "facility_img")
    private String img;

    @Column(name = "facility_terms")
    private String terms;

    @Column(name = "facility_extension_limit")
    private Integer extensionLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_dormitory")
    private DormitoryEntity dormitoryEntity;

    public FacilityEntity(String id, String name, String info, Integer open,
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

    public FacilityDTO toDTO() {
        return FacilityDTO.builder()
                .id(this.id)
                .name(this.name)
                .info(this.info)
                .open(this.open)
                .close(this.close)
                .img(this.img)
                .terms(this.terms)
                .extensionLimit(this.extensionLimit)
                .dormitoryDTO(this.dormitoryEntity.toDTO())
                .build();
    }
}
