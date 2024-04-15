package com.project.clickit.repository;

import com.project.clickit.entity.FacilityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<FacilityEntity, String> {

    @NonNull
    Page<FacilityEntity> findAll(@NonNull Pageable pageable);

    @Query("SELECT new FacilityEntity(f.id, f.name, f.info, f.open, f.close, f.capacity, f.img, f.terms, f.dormitoryEntity) FROM FacilityEntity f where f.id = :id")
    FacilityEntity findByFacilityId(@Param("id") String id);

    @Query("SELECT new FacilityEntity(f.id, f.name, f.info, f.open, f.close, f.capacity, f.img, f.terms, f.dormitoryEntity) FROM FacilityEntity f where f.name like %:name%")
    FacilityEntity findByFacilityName(@Param("name") String name);

    // select by dormitory
    @Query("SELECT new FacilityEntity(f.id, f.name, f.info, f.open, f.close, f.capacity, f.img, f.terms, f.dormitoryEntity) FROM FacilityEntity f where f.dormitoryEntity.id = :dormitoryId")
    Page<FacilityEntity> findByDormitoryId(@Param("dormitoryId") String dormitoryId, Pageable pageable);

    // update facility id
    @Modifying
    @Query("UPDATE FacilityEntity f SET f.id = :newId WHERE f.id = :id")
    void updateFacilityId(@Param("id") String id, @Param("newId") String newId);
}
