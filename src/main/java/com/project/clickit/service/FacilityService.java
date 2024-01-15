package com.project.clickit.service;

import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    /**
     * 해당 id의 시설이 존재하는지 확인
     * @param id String
     * @return boolean
     */
    @Transactional
    public Boolean isExist(String id) {
        return facilityRepository.existsById(id);
    }

    /**
     * 시설 생성
     * @param facilityDTO FacilityDTO
     */
    @Transactional
    public void createFacility(FacilityDTO facilityDTO) {
        if(isExist(facilityDTO.getId())){
            throw new DuplicatedIdException();
        }
        facilityRepository.save(facilityDTO.toEntity());
    }

    /**
     * 시설 전체 조회
     * @return List<FacilityDTO>
     */
    @Transactional
    public List<FacilityDTO> getAll() {
        return toDTOList(facilityRepository.findAll());
    }

    /**
     * 시설 id로 조회
     * @param id String
     * @return FacilityDTO
     */
    @Transactional
    public FacilityDTO findById(String id) {
        return facilityRepository.findByFacilityId(id).toDTO();
    }

    /**
     * 시설 정보 수정
     * @param facilityDTO FacilityDTO
     * @return void
     */
    @Transactional
    public void updateFacility(FacilityDTO facilityDTO) {
        if(!isExist(facilityDTO.getId())){
            throw new DuplicatedIdException();
        }
        facilityRepository.updateFacility(facilityDTO.getId(), facilityDTO.getName(),
                facilityDTO.getInfo(), facilityDTO.getOpen(), facilityDTO.getClose(),
                facilityDTO.getImg(), facilityDTO.getTerms(), facilityDTO.getExtensionLimit());
    }

    /**
     * 시설 삭제
     * @param id String
     * @return void
     */
    @Transactional
    public void deleteById(String id) {
        if(!isExist(id)){
            throw new DuplicatedIdException();
        }
        facilityRepository.deleteById(id);
    }

    /**
     * Entity List를 DTO List로 변환
     * @param facilityEntityList List
     * @return List<FacilityDTO>
     */
    public List<FacilityDTO> toDTOList(List<FacilityEntity> facilityEntityList){
        List<FacilityDTO> facilityDTOList = new ArrayList<>();
        for (FacilityEntity facilityEntity : facilityEntityList) {
            facilityDTOList.add(facilityEntity.toDTO());
        }
        return facilityDTOList;
    }
}
