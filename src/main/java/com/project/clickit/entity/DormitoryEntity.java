package com.project.clickit.entity;

import com.project.clickit.dto.DormitoryDTO;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Table(name = "dormitory")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DormitoryEntity {
    @Id
    @Column(name = "dormitory_id")
    private String id;

    @Column(name = "dormitory_name")
    private String name;

    /**
     * <b>DormitoryDTO로 변환</b>
     * @return DormitoryDTO
     */
    public DormitoryDTO toDTO() {
        return DormitoryDTO.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
