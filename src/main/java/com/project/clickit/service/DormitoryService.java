package com.project.clickit.service;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.dormitory.DormitoryNotFoundException;
import com.project.clickit.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DormitoryService {

    private final DormitoryRepository dormitoryRepository;

    @Autowired
    public DormitoryService(DormitoryRepository dormitoryRepository) {
        this.dormitoryRepository = dormitoryRepository;
    }

    @Transactional
    public Boolean isExist(String id) {
        return dormitoryRepository.existsById(id);
    }

    @Transactional
    public void createDormitory(DormitoryDTO dormitoryDTO) {
        if(isExist(dormitoryDTO.getId())){
            throw new DuplicatedIdException();
        }
        dormitoryRepository.save(dormitoryDTO.toEntity());
    }

    @Transactional
    public List<DormitoryDTO> getAll() {
        return toDTOList(dormitoryRepository.findAll());
    }

    @Transactional
    public DormitoryDTO findById(String id) {
        return dormitoryRepository.findByDormitoryId(id).toDTO();
    }

    @Transactional
    public void updateDormitoryName(String id, String name) {
        if(!isExist(id)){
            throw new DormitoryNotFoundException();
        }
        dormitoryRepository.updateDormitoryName(id, name);
    }

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
