package com.project.clickit.repository;

import com.project.clickit.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, String> {

    @NonNull
    Page<ReservationEntity> findAll(@NonNull Pageable pageable);

    ReservationEntity findById(@Param("num") Integer num);

    Boolean existsById(@Param("num") Integer num);

    // find by ReservationEntity memberEntity memberId
    @Query("SELECT r FROM ReservationEntity r WHERE r.memberEntity.id = :memberId")
    Page<ReservationEntity> findByMemberEntityId(@Param("memberId") String memberId, Pageable pageable);

    @Modifying
    @Query("UPDATE ReservationEntity r SET r.status = :status WHERE r.num = :num")
    void updateReservationStatus(@Param("num") Integer num, @Param("status") String status);

    @Query("SELECT r FROM ReservationEntity r WHERE r.seatEntity.id = :seatId AND DATE(r.timestamp) = DATE(NOW())")
    Page<ReservationEntity> findBySeatEntityIdAndToday(@Param("seatId") String seatId, Pageable pageable);

    @Query("SELECT r FROM ReservationEntity r WHERE r.memberEntity.id = :memberId AND DATE(r.timestamp) = DATE(NOW())")
    Page<ReservationEntity> findByMemberEntityIdAndToday(@Param("memberId") String memberId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ReservationEntity r WHERE r.num = :num")
    void deleteByNum(@Param("num") Integer num);
}