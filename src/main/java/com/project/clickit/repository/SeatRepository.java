package com.project.clickit.repository;

import com.project.clickit.entity.SeatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, String> {

    @NonNull
    Page<SeatEntity> findAll(@NonNull Pageable pageable);

    @Query("SELECT new SeatEntity(s.id, s.name, s.time, s.isEmpty, s.facilityEntity) FROM SeatEntity s where s.id = :id")
    SeatEntity findBySeatId(String id);

    @Query("SELECT new SeatEntity(s.id, s.name, s.time, s.isEmpty, s.facilityEntity) FROM SeatEntity s where s.facilityEntity.id = :facilityId")
    Page<SeatEntity> findByFacilityId(@Param("facilityId") String facilityId, Pageable pageable);

    @Modifying
    @Query("UPDATE SeatEntity s SET s.facilityEntity.id = :facilityId WHERE s.id = :id")
    void updateSeatFacility(@Param("id") String id, @Param("facilityId") String facilityId);

    @Modifying
    @Query("UPDATE SeatEntity s SET s.isEmpty = :isEmpty WHERE s.id = :id")
    void updateSeatIsEmpty(@Param("id") String id, @Param("isEmpty") Boolean isEmpty);
}
