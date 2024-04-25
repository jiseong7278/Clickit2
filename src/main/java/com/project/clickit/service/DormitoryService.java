package com.project.clickit.service;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * <b>해당 id의 기숙사가 존재하는지 확인</b> <br>(존재한다면 true, 존재하지 않는다면 false)
     * @param id String
     * @return Boolean
     */
    @Transactional
    public Boolean isExist(String id) {
        return dormitoryRepository.existsById(id);
    }


    // ========== Create ========== //
    /**
     * <b>기숙사 생성</b>
     * @param dormitoryDTO DormitoryDTO
     */
    @Transactional
    public void createDormitory(DormitoryDTO dormitoryDTO) {
        if(isExist(dormitoryDTO.getId())){
            throw new DuplicatedIdException(ErrorCode.DUPLICATED_ID);
        }
        dormitoryRepository.save(dormitoryDTO.toEntity());
    }


    // ========== Read ========== //
    /**
     * <b>기숙사 전체 조회</b>
     * @return Page&lt;DormitoryDTO&gt;
     */
    @Transactional
    public Page<DormitoryDTO> getAll(Pageable pageable) {
        return toDTOPage(dormitoryRepository.findAll(pageable));
    }

    /**
     * <b>기숙사 id로 조회</b>
     * @param id String
     * @return DormitoryDTO
     */
    @Transactional
    public DormitoryDTO findById(String id) {
        if (!isExist(id)) throw new ObjectNotFoundException(ErrorCode.DORMITORY_NOT_FOUND);
        return dormitoryRepository.findByDormitoryId(id).toDTO();
    }

    /**
     * <b>기숙사 이름으로 조회</b>
     * @param name String
     * @return Page&lt;DormitoryDTO&gt;
     */
    @Transactional
    public Page<DormitoryDTO> findByName(String name, Pageable pageable) {
        return toDTOPage(dormitoryRepository.findByDormitoryName(name, pageable));
    }


    // ========== Update ========== //
    /**
     * <b>기숙사 이름 변경</b>
     * @param dormitoryDTO DormitoryDTO
     */
    @Transactional
    public void updateDormitory(DormitoryDTO dormitoryDTO) {
        if(!isExist(dormitoryDTO.getId())){
            throw new ObjectNotFoundException(ErrorCode.DORMITORY_NOT_FOUND);
        }
        dormitoryRepository.save(dormitoryDTO.toEntity());
    }


    // ========== Delete ========== //
    /**
     * <b>기숙사 삭제</b>
     * @param id String
     */
    @Transactional
    public void deleteById(String id) {
        if(!isExist(id)){
            throw new ObjectNotFoundException(ErrorCode.DORMITORY_NOT_FOUND);
        }
        dormitoryRepository.deleteById(id);
    }

    /**
     * <b>Page&lt;DormitoryEntity&gt;를 Page&lt;DormitoryDTO&gt;로 변환</b>
     * @param dormitoryEntityPage Page&lt;DormitoryEntity&gt;
     * @return Page&lt;DormitoryDTO&gt;
     */
    private Page<DormitoryDTO> toDTOPage(Page<DormitoryEntity> dormitoryEntityPage) {
        return dormitoryEntityPage.map(DormitoryEntity::toDTO);
    }
}
