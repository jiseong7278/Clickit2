package com.project.clickit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DormitoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_num")
    private Integer dormitoryNum;

    @Column(name = "dormitory_name")
    private String dormitoryName;

    @OneToMany(mappedBy = "dormitoryEntity")
    private List<MemberEntity> memberEntity;
}
