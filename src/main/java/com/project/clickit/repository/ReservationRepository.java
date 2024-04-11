package com.project.clickit.repository;

import com.project.clickit.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<ReservationEntity, String> {

    Page<ReservationEntity> findAll(Pageable pageable);

    // find by ReservationEntity memberEntity memberId
    @Query("SELECT r FROM ReservationEntity r WHERE r.memberEntity.id = :memberId")
    Page<ReservationEntity> findByMemberEntityId(String memberId, Pageable pageable);
}
