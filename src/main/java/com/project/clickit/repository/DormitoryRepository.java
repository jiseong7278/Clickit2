package com.project.clickit.repository;

import com.project.clickit.entity.DormitoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface DormitoryRepository extends JpaRepository<DormitoryEntity, String> {

    @NonNull
    Page<DormitoryEntity> findAll(@NonNull Pageable pageable);

    @Query("SELECT new DormitoryEntity(d.id, d.name) FROM DormitoryEntity d where d.id = :id")
    DormitoryEntity findByDormitoryId(@Param("id")String id);

    @Query("SELECT new DormitoryEntity(d.id, d.name) FROM DormitoryEntity d where d.name like %:name%")
    Page<DormitoryEntity> findByDormitoryName(@Param("name")String name, Pageable pageable);

    @Modifying
    @Query("UPDATE DormitoryEntity d SET d.name = :name WHERE d.id = :id")
    void updateDormitoryName(@Param("id") String id, @Param("name") String name);
}
