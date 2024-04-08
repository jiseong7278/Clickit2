package com.project.clickit.service;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.dormitory.DormitoryNotFoundException;
import com.project.clickit.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DormitoryService {

    @Value("${roles.dev}")
    private String TYPE_DEV;

    private final DormitoryRepository dormitoryRepository;

    @Autowired
    public DormitoryService(DormitoryRepository dormitoryRepository) {
        this.dormitoryRepository = dormitoryRepository;
    }

    /**
     * 해당 id의 기숙사가 존재하는지 확인
     * @param id String
     * @return return true if existed, else return false
     */
    @Transactional
    public Boolean isExist(String id) {
        return dormitoryRepository.existsById(id);
    }

    /**
     * 기숙사 생성
     * @param dormitoryDTO DormitoryDTO
     */
    @Transactional
    public void createDormitory(DormitoryDTO dormitoryDTO) {
        if(isExist(dormitoryDTO.getId())){
            throw new DuplicatedIdException();
        }
        dormitoryRepository.save(dormitoryDTO.toEntity());
    }

    /**
     * 기숙사 전체 조회
     * @return List<DormitoryDTO>
     */
    @Transactional
    public List<DormitoryDTO> getAll() {
        return toDTOList(dormitoryRepository.findAll());
    }

    /**
     * 기숙사 id로 조회
     * @param id String
     * @return DormitoryDTO
     */
    @Transactional
    public DormitoryDTO findById(String id) {
        return dormitoryRepository.findByDormitoryId(id).toDTO();
    }

    /**
     * 기숙사 이름으로 조회
     * @param name String
     * @return DormitoryDTO
     */
    @Transactional
    public DormitoryDTO findByName(String name) {
        return dormitoryRepository.findByDormitoryName(name).toDTO();
    }

    /**
     * 기숙사 이름 수정
     * @param id String
     * @param name String
     */
    @Transactional
    public void updateDormitoryName(String id, String name) {
        if(!isExist(id)){
            throw new DormitoryNotFoundException();
        }
        dormitoryRepository.updateDormitoryName(id, name);
    }

    /**
     * 기숙사 삭제
     * @param id String
     */
    @Transactional
    public void deleteById(String id) {
        if(!isExist(id)){
            throw new DormitoryNotFoundException();
        }
        dormitoryRepository.deleteById(id);
    }

    public List<DormitoryDTO> toDTOList(List<DormitoryEntity> dormitoryEntityList) {
        List<DormitoryDTO> dormitoryDTOList = new ArrayList<>();
        for (DormitoryEntity dormitoryEntity : dormitoryEntityList) {
            dormitoryDTOList.add(dormitoryEntity.toDTO());
        }
        return dormitoryDTOList;
    }
}
