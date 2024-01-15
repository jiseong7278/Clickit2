package com.project.clickit.repository;

import com.project.clickit.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<SeatEntity, String> {

    @Query("SELECT new SeatEntity(s.id, s.name, s.time, s.isEmpty, s.facilityEntity) FROM SeatEntity s where s.id = :id")
    SeatEntity findBySeatId(String id);

    @Modifying
    @Query("UPDATE SeatEntity s SET s.name = :name, s.time = :time, s.isEmpty = :isEmpty WHERE s.id = :id")
    void updateSeat(@Param("id") String id, @Param("name") String name,
                    @Param("time") Integer time, @Param("isEmpty") Boolean isEmpty);

    @Modifying
    @Query("UPDATE SeatEntity s SET s.facilityEntity.id = :facilityId WHERE s.id = :id")
    void updateSeatFacility(@Param("id") String id, @Param("facilityId") String facilityId);

    @Modifying
    @Query("UPDATE SeatEntity s SET s.isEmpty = :isEmpty WHERE s.id = :id")
    void updateSeatIsEmpty(@Param("id") String id, @Param("isEmpty") Boolean isEmpty);
}
