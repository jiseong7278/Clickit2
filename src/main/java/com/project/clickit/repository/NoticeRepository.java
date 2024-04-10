package com.project.clickit.repository;

import com.project.clickit.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    /**
     * <b>모든 공지사항 조회</b>
     * @param pageable Pageable
     * @return Page&lt;NoticeEntity&gt;
     */
    @NonNull
    Page<NoticeEntity> findAll(@NonNull Pageable pageable);

    /**
     * <b>notice_num 에 해당하는 공지사항 조회</b>
     * @param num Integer
     * @return NoticeEntity
     */
    @Query("SELECT new NoticeEntity(n.num, n.title, n.content, n.date, n.img, n.memberEntity) FROM NoticeEntity n where n.num = :num")
    NoticeEntity findByNum(@Param("num") Integer num);

    /**
     * <b>member_id에 해당하는 공지사항 조회</b>
     * @param memberId String
     * @param pageable Pageable
     * @return Page&lt;NoticeEntity&gt;
     */
    @Query("SELECT new NoticeEntity(n.num, n.title, n.content, n.date, n.img, n.memberEntity) FROM NoticeEntity n where n.memberEntity.id = :memberId")
    Page<NoticeEntity> findByWriterId(@Param("memberId") String memberId, Pageable pageable);

    /**
     * <b>해당 num 의 공지사항이 존재하는지 확인</b>
     * @param num Integer
     * @return Boolean
     */
    Boolean existsByNum(Integer num);
}
