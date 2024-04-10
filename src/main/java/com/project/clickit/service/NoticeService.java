package com.project.clickit.service;

import com.project.clickit.dto.NoticeDTO;
import com.project.clickit.entity.NoticeEntity;
import com.project.clickit.exceptions.notice.NoticeNotFoundException;
import com.project.clickit.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    /**
     * <b>해당 num 의 공지사항이 존재하는지 확인</b> (존재한다면 true, 존재하지 않는다면 false)
     * @param num Integer
     * @return Boolean
     */
    @Transactional
    public Boolean isExist(Integer num) {
        return noticeRepository.existsByNum(num);
    }

    // ========== Create ========== //
    /**
     * <b>공지사항 생성</b>
     * @param noticeDTO NoticeDTO
     */
    @Transactional
    public void createNotice(NoticeDTO noticeDTO) {
        noticeRepository.save(noticeDTO.toEntity());
    }


    // ========== Read ========== //
    /**
     * <b>모든 공지사항 조회</b>
     * @param pageable Pageable
     * @return Page&lt;NoticeDTO&gt;
     */
    @Transactional
    public Page<NoticeDTO> getAll(Pageable pageable){
        return toDTOPage(noticeRepository.findAll(pageable));
    }

    /**
     * <b>notice_num 에 해당하는 공지사항 조회</b>
     * @param num Integer
     * @return NoticeDTO
     */
    @Transactional
    public NoticeDTO findNoticeById(Integer num) {
        return noticeRepository.findByNum(num).toDTO();
    }

    /**
     * <b>member_id에 해당하는 공지사항 조회</b>
     * @param memberId String
     * @param pageable Pageable
     * @return Page&lt;NoticeDTO&gt;
     */
    @Transactional
    public Page<NoticeDTO> findNoticeByWriterId(String memberId, Pageable pageable) {
        return toDTOPage(noticeRepository.findByWriterId(memberId, pageable));
    }


    // ========== Update ========== //
    /**
     * <b>공지사항 수정</b>
     * @param noticeDTO NoticeDTO
     */
    @Transactional
    public void updateNotice(NoticeDTO noticeDTO) {
        noticeRepository.save(noticeDTO.toEntity());
    }


    // ========== Delete ========== //
    /**
     * <b>공지사항 삭제</b>
     * @param num Integer
     */
    @Transactional
    public void deleteNotice(Integer num) {
        if(isExist(num)){
            throw new NoticeNotFoundException();
        }
        noticeRepository.deleteById(num);
    }

    /**
     * <b>Page&lt;NoticeEntity&gt; 를 Page&lt;NoticeDTO&gt; 로 변환</b>
     * @param noticeEntityPage Page&lt;NoticeEntity&gt;
     * @return Page&lt;NoticeDTO&gt;
     */
    public Page<NoticeDTO> toDTOPage(Page<NoticeEntity> noticeEntityPage) {
        return noticeEntityPage.map(NoticeEntity::toDTO);
    }
}