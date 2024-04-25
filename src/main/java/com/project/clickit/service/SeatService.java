package com.project.clickit.service;

import com.project.clickit.dto.SeatDTO;
import com.project.clickit.entity.SeatEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.seat.SeatNotFoundException;
import com.project.clickit.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * <b>해당 id의 좌석이 존재하는지 확인</b>
     * @param id String
     * @return Boolean
     */
    @Transactional
    public Boolean isExist(String id) {
        return seatRepository.existsById(id);
    }



    // ========== Create ========== //
    /**
     * <b>좌석 생성</b>
     * @param seatDTO SeatDTO
     */
    @Transactional
    public void createSeat(SeatDTO seatDTO) {
        if(isExist(seatDTO.getId())) throw new DuplicatedIdException();
        seatRepository.save(seatDTO.toEntity());
    }

    /**
     * <b>좌석 List 생성</b>
     * @param seatDTOList List&lt;SeatDTO&gt;
     */
    @Transactional
    public void createList(List<SeatDTO> seatDTOList) {
        for(SeatDTO seatDTO : seatDTOList){
            if(isExist(seatDTO.getId())){
                throw new DuplicatedIdException();
            }
        }
        seatRepository.saveAll(toEntityList(seatDTOList));
    }


    // ========== Read ========== //
    /**
     * <b>좌석 전체 조회</b>
     * @param pageable Pageable
     * @return Page&lt;SeatDTO&gt;
     */
    @Transactional
    public Page<SeatDTO> getAll(Pageable pageable){
        return toDTOPage(seatRepository.findAll(pageable));
    }

    /**
     * <b>좌석 id로 조회</b>
     * @param id String
     * @return SeatDTO
     */
    @Transactional
    public SeatDTO findById(String id) {
        if (!isExist(id)) throw new SeatNotFoundException();
        return seatRepository.findBySeatId(id).toDTO();
    }

    /**
     * <b>시설물 id로 조회</b>
     * @param facilityId String
     * @param pageable Pageable
     * @return Page&lt;SeatDTO&gt;
     */
    @Transactional
    public Page<SeatDTO> findByFacilityId(String facilityId, Pageable pageable){
        return toDTOPage(seatRepository.findByFacilityId(facilityId, pageable));
    }


    // ========== Update ========== //
    /**
     * <b>좌석 정보 수정</b>
     * @param seatDTO SeatDTO
     */
    @Transactional
    public void updateSeat(SeatDTO seatDTO) {
        if(!isExist(seatDTO.getId())) throw new SeatNotFoundException();
        seatRepository.save(seatDTO.toEntity());
    }

    /**
     * <b>좌석 시설 수정</b>
     * @param seatId String
     * @param facilityId String
     */
    @Transactional
    public void updateSeatFacility(String seatId, String facilityId) {
        if(!isExist(seatId)) throw new SeatNotFoundException();
        seatRepository.updateSeatFacility(seatId, facilityId);
    }

    /**
     * <b>좌석 비어있는지 여부를 수정</b>
     * @param seatId String
     * @param isEmpty Boolean
     */
    @Transactional
    public void updateSeatIsEmpty(String seatId, Boolean isEmpty) {
        if(!isExist(seatId)) throw new SeatNotFoundException();
        seatRepository.updateSeatIsEmpty(seatId, isEmpty);
    }


    // ========== Delete ========== //
    /**
     * <b>좌석 삭제</b>
     * @param id String
     */
    @Transactional
    public void deleteById(String id) {
        if(!isExist(id)) throw new SeatNotFoundException();
        seatRepository.deleteById(id);
    }

    /**
     * <b>List&lt;SeatDTO&gt;를 List&lt;SeatEntity&gt;로 변환</b>
     * @param seatDTOList List
     * @return List&lt;SeatEntity&gt;
     */
    public List<SeatEntity> toEntityList(List<SeatDTO> seatDTOList) {
        List<SeatEntity> seatEntityList = new ArrayList<>();
        for (SeatDTO seatDTO : seatDTOList) {
            seatEntityList.add(seatDTO.toEntity());
        }
        return seatEntityList;
    }

    /**
     * <b>Page&lt;SeatEntity&gt;를 Page&lt;SeatDTO&gt;로 변환</b>
     * @param seatEntityPage Page
     * @return Page&lt;SeatDTO&gt;
     */
    public Page<SeatDTO> toDTOPage(Page<SeatEntity> seatEntityPage) {
        return seatEntityPage.map(SeatEntity::toDTO);
    }
}
