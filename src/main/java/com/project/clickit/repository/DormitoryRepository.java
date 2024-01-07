package com.project.clickit.repository;

import com.project.clickit.entity.DormitoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DormitoryRepository extends JpaRepository<DormitoryEntity, String> {

    @Query("SELECT new DormitoryEntity(d.id, d.name) FROM DormitoryEntity d where d.id = :id")
    DormitoryEntity findByDormitoryId(@Param("id")String id);

    @Modifying
    @Query("UPDATE DormitoryEntity d SET d.name = :name WHERE d.id = :id")
    void updateDormitoryName(@Param("id") String id, @Param("name") String name);
}
