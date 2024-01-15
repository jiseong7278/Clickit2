package com.project.clickit.repository;

import com.project.clickit.entity.FacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityRepository extends JpaRepository<FacilityEntity, String> {
    @Query("SELECT new FacilityEntity(f.id, f.name, f.info, f.open, f.close, f.img, f.terms, f.extensionLimit, f.dormitoryEntity) FROM FacilityEntity f where f.id = :id")
    FacilityEntity findByFacilityId(String id);

    @Modifying
    @Query("UPDATE FacilityEntity f SET f.name = :name, f.info = :info, f.open = :open, f.close = :close, f.img = :img, f.terms = :terms, f.extensionLimit = :extensionLimit WHERE f.id = :id")
    void updateFacility(@Param("id") String id, @Param("name") String name,
                        @Param("info") String info, @Param("open") Integer open,
                        @Param("close") Integer close, @Param("img") String img,
                        @Param("terms") String terms, @Param("extensionLimit") Integer extensionLimit);
}
