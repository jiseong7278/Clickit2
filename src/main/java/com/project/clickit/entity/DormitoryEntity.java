package com.project.clickit.entity;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@Table(name = "dormitory")
@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class DormitoryEntity {
    @Id
    @Column(name = "dormitory_id")
    private String id;

    @Column(name = "dormitory_name")
    private String name;

    @Builder
    public DormitoryEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DormitoryDTO toDTO() {
        return DormitoryDTO.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
