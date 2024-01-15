package com.project.clickit.service;

import com.project.clickit.dto.SeatDTO;
import com.project.clickit.entity.SeatEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return boolean
     */
    @Transactional
    public Boolean isExist(String id) {
        return seatRepository.existsById(id);
    }


    /**
     * <b>좌석 생성</b>
     * @param seatDTO SeatDTO
     */
    @Transactional
    public void createSeat(SeatDTO seatDTO) {
        if(isExist(seatDTO.getId())){
            throw new DuplicatedIdException();
        }
        seatRepository.save(seatDTO.toEntity());
    }

    /**
     * <b>좌석 전체 조회</b>
     * @return List<SeatDTO>
     */
    @Transactional
    public List<SeatDTO> getAll() {
        return toDTOList(seatRepository.findAll());
    }

    /**
     * <b>좌석 id로 조회</b>
     * @param id String
     * @return SeatDTO
     */
    @Transactional
    public SeatDTO findById(String id) {
        return seatRepository.findBySeatId(id).toDTO();
    }

    /**
     * <b>좌석 정보 수정</b>
     * @param seatDTO SeatDTO
     * @return void
     */
    @Transactional
    public void updateSeat(SeatDTO seatDTO) {
        if(!isExist(seatDTO.getId())){
            throw new DuplicatedIdException();
        }
        seatRepository.updateSeat(seatDTO.getId(), seatDTO.getName(), seatDTO.getTime(), seatDTO.getIsEmpty());
    }

    /**
     * <b>좌석 시설 수정</b>
     * @param seatDTO SeatDTO
     * @return void
     */
    @Transactional
    public void updateSeatFacility(SeatDTO seatDTO) {
        if(!isExist(seatDTO.getId())){
            throw new DuplicatedIdException();
        }
        seatRepository.updateSeatFacility(seatDTO.getId(), seatDTO.getFacilityId());
    }

    /**
     * <b>좌석 비어있는지 수정</b>
     * @param seatDTO SeatDTO
     * @return void
     */
    @Transactional
    public void updateSeatIsEmpty(SeatDTO seatDTO) {
        if(!isExist(seatDTO.getId())){
            throw new DuplicatedIdException();
        }
        seatRepository.updateSeatIsEmpty(seatDTO.getId(), seatDTO.getIsEmpty());
    }

    /**
     * <b>좌석 삭제</b>
     * @param id String
     * @return void
     */
    @Transactional
    public void deleteById(String id) {
        if(!isExist(id)){
            throw new DuplicatedIdException();
        }
        seatRepository.deleteById(id);
    }

    /**
     * <b>SeatEntity List를 SeatDTO List로 변환</b>
     * @param seatEntities
     * @return List<SeatDTO>
     */
    public List<SeatDTO> toDTOList(List<SeatEntity> seatEntities) {
        List<SeatDTO> seatDTOList = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            seatDTOList.add(seatEntity.toDTO());
        }
        return seatDTOList;
    }
}
