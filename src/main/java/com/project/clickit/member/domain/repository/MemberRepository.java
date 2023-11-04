package com.project.clickit.member.domain.repository;

import com.project.clickit.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Query(value = "SELECT * FROM clickit.member", nativeQuery = true)
    List<MemberEntity> getAll();
}
