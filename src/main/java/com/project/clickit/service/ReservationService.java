package com.project.clickit.service;

import com.project.clickit.dto.ReservationDTO;
import com.project.clickit.entity.ReservationEntity;
import com.project.clickit.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Page<ReservationDTO> findAll(Pageable pageable){
        return toDTOPage(reservationRepository.findAll(pageable));
    }

    private Page<ReservationDTO> toDTOPage(Page<ReservationEntity> reservationEntityPage){
        return reservationEntityPage.map(ReservationEntity::toDTO);
    }
}
