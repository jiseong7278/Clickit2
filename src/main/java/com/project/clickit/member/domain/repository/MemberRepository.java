package com.project.clickit.member.domain.repository;

import com.project.clickit.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Query(value = "SELECT * FROM clickit.member", nativeQuery = true)
    List<MemberEntity> getAll();

    @Query("SELECT new MemberEntity(m.password, m.refreshToken) FROM MemberEntity m where m.id = :memberId")
    MemberEntity findByMemberId(@Param("memberId") String memberId);

    @Query(value = "SELECT m.member_password FROM clickit.member as m WHERE m.member_id = :memberId", nativeQuery = true)
    String findPasswordByMemberId(@Param("memberId") String memberId);

    @Modifying
    @Query("UPDATE MemberEntity m SET m.refreshToken = :refreshToken WHERE m.id = :memberId")
    void updateRefreshToken(@Param("refreshToken") String refreshToken, @Param("memberId") String memberId);
}
