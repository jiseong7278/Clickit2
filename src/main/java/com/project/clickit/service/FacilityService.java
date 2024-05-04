package com.project.clickit.service;

import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    /**
     * <b>해당 id의 시설이 존재하는지 확인</b> <br>(존재한다면 true, 존재하지 않는다면 false)
     * @param id String
     * @return Boolean
     */
    @Transactional
    public Boolean isExist(String id) {
        return facilityRepository.existsById(id);
    }


    // ========== Create ========== //
    /**
     * <b>시설 생성</b>
     * @param facilityDTO FacilityDTO
     */
    @Transactional
    public void createFacility(FacilityDTO facilityDTO) {
        if(isExist(facilityDTO.getId())){
            throw new DuplicatedIdException(ErrorCode.DUPLICATED_ID);
        }
        facilityRepository.save(facilityDTO.toEntity());
    }


    // ========== Read ========== //
    /**
     * <b>시설 전체 조회</b>
     * @return Page&lt;FacilityDTO&gt;
     */
    @Transactional
    public Page<FacilityDTO> getAll(Pageable pageable) {
        return toDTOPage(facilityRepository.findAll(pageable));
    }

    /**
     * <b>시설 id로 조회</b>
     * @param id String
     * @return FacilityDTO
     */
    @Transactional
    public FacilityDTO findById(String id) {
        if (!isExist(id)) {
            throw new ObjectNotFoundException(ErrorCode.FACILITY_NOT_FOUND);
        }
        return facilityRepository.findByFacilityId(id).toDTO();
    }

    /**
     * <b>시설 이름으로 조회</b>
     * @param name String
     * @return FacilityDTO
     */
    @Transactional
    public Page<FacilityDTO> findByName(String name, Pageable pageable) {
        return toDTOPage(facilityRepository.findByFacilityName(name, pageable));
    }

    /**
     * <b>기숙사 id로 시설 조회</b>
     * @param dormitoryId String
     * @return Page&lt;FacilityDTO&gt;
     */
    @Transactional
    public Page<FacilityDTO> findByDormitoryId(String dormitoryId, Pageable pageable) {
        return toDTOPage(facilityRepository.findByDormitoryId(dormitoryId, pageable));
    }


    // ========== Update ========== //
    /**
     * <b>시설 수정</b>
     * @param facilityDTO FacilityDTO
     */
    @Transactional
    public void updateFacility(FacilityDTO facilityDTO) {
        if(!isExist(facilityDTO.getId())){
            throw new ObjectNotFoundException(ErrorCode.FACILITY_NOT_FOUND);
        }
        facilityRepository.save(facilityDTO.toEntity());
    }

    /**
     * <b>시설 id 변경</b>
     * @param id String
     */
    @Transactional
    public void updateFacilityId(String id, String newId) {
        if(!isExist(id)){
            throw new ObjectNotFoundException(ErrorCode.FACILITY_NOT_FOUND);
        }
        if (isExist(newId)) {
            throw new DuplicatedIdException(ErrorCode.DUPLICATED_ID);
        }
        facilityRepository.updateFacilityId(id, newId);
    }


    // ========== Delete ========== //
    /**
     * <b>시설 이름 변경</b>
     * @param id String
     */
    @Transactional
    public void deleteById(String id) {
        if(!isExist(id)){
            throw new ObjectNotFoundException(ErrorCode.FACILITY_NOT_FOUND);
        }
        facilityRepository.deleteById(id);
    }

    /**
     * <b>Page&lt;FacilityEntity&gt;를 Page&lt;FacilityDTO&gt;로 변환</b>
     * @param facilityEntityPage Page
     * @return Page&lt;FacilityDTO&gt;
     */
    public Page<FacilityDTO> toDTOPage(Page<FacilityEntity> facilityEntityPage) {
        return facilityEntityPage.map(FacilityEntity::toDTO);
    }
}
