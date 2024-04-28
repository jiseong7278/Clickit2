package com.project.clickit.repository;

import com.project.clickit.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @NonNull
    Page<MemberEntity> findAll(@NonNull Pageable pageable);

    MemberEntity findById(@NonNull String id);

    // find all where type is student
    @Query("SELECT new MemberEntity(m.id, m.password, m.name, m.email, m.phone, m.studentNum, m.type, m.refreshToken, m.dormitoryEntity) FROM MemberEntity m where m.type = :type")
    Page<MemberEntity> findAllStudent(@Param("type") String type, Pageable pageable);

    // find by member name
    @Query("SELECT new MemberEntity(m.id, m.password, m.name, m.email, m.phone, m.studentNum, m.type, m.refreshToken, m.dormitoryEntity) FROM MemberEntity m where m.name like %:memberName% and m.type = :type")
    Page<MemberEntity> findByMemberName(@Param("memberName") String memberName, @Param("type") String type, Pageable pageable);

    // find by dormitory id
    @Query("SELECT new MemberEntity(m.id, m.password, m.name, m.email, m.phone, m.studentNum, m.type, m.refreshToken, m.dormitoryEntity) FROM MemberEntity m where m.dormitoryEntity.id = :dormitoryId")
    Page<MemberEntity> findByDormitoryId(@Param("dormitoryId") String dormitoryId, Pageable pageable);

    // update password by phone
    @Modifying
    @Query("UPDATE MemberEntity m SET m.password = :password WHERE m.phone = :phone")
    void updatePasswordByPhone(@Param("phone") String phone, @Param("password") String password);

    // update password by email
    @Modifying
    @Query("UPDATE MemberEntity m SET m.password = :password WHERE m.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    // update member password
    @Modifying
    @Query("UPDATE MemberEntity m SET m.password = :password WHERE m.id = :id")
    void updatePassword(@Param("id") String id, @Param("password") String password);

    @Modifying
    @Query("UPDATE MemberEntity m SET m.password = :password, m.name = :name, m.email = :email, m.phone = :phone, m.studentNum = :studentNum, m.type = :type, m.dormitoryEntity.id = :dormitoryId WHERE m.id = :id")
    void updateMemberForStaff(@Param("id") String id, @Param("password") String password, @Param("name") String name, @Param("email") String email, @Param("phone") String phone, @Param("studentNum") String studentNum, @Param("type") String type, @Param("dormitoryId") String dormitoryId);

    @Modifying
    @Query("UPDATE MemberEntity m SET m.refreshToken = :refreshToken WHERE m.id = :memberId")
    void updateRefreshToken(@Param("memberId") String memberId, @Param("refreshToken") String refreshToken);

    // exist check by id
    Boolean existsById(String id);

    void deleteById(String id);

    // delete all student
    @Modifying
    @Query("DELETE FROM MemberEntity m WHERE m.type = :type")
    void deleteAllStudent(@Param("type") String type);
}
