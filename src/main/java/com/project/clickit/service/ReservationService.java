package com.project.clickit.service;

import com.project.clickit.dto.ReservationDTO;
import com.project.clickit.entity.ReservationEntity;
import com.project.clickit.exceptions.reservation.DuplicatedReservationException;
import com.project.clickit.exceptions.reservation.ReservationNotFoundException;
import com.project.clickit.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    // ========== Create ========== //
    @Transactional
    public void create(ReservationDTO reservationDTO){
        Page<ReservationEntity> reservationEntities = reservationRepository.findBySeatEntityIdAndToday(reservationDTO.getSeatId(), Pageable.unpaged());
        if(!reservationEntities.isEmpty()&&isDuplicatedReservation(reservationEntities)){
            throw new DuplicatedReservationException();
        }
        reservationRepository.save(reservationDTO.toEntity());
    }


    // ========== Read ========== //
    @Transactional
    public Page<ReservationDTO> findAll(Pageable pageable){
        return toDTOPage(reservationRepository.findAll(pageable));
    }

    // for staff
    @Transactional
    public Page<ReservationDTO> findByMemberId(String memberId, Pageable pageable){
        return toDTOPage(reservationRepository.findByMemberEntityId(memberId, pageable));
    }

    // for student
    @Transactional
    public Page<ReservationDTO> findByMemberId(Pageable pageable){
        return toDTOPage(reservationRepository.findByMemberEntityId(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                pageable));
    }

    @Transactional
    public Page<ReservationDTO> findBySeatIdAndToday(String seatId, Pageable pageable){
        return toDTOPage(reservationRepository.findBySeatEntityIdAndToday(seatId, pageable));
    }

    // for staff
    @Transactional
    public Page<ReservationDTO> findByMemberIdAndToday(String memberId, Pageable pageable){
        return toDTOPage(reservationRepository.findByMemberEntityIdAndToday(memberId, pageable));
    }

    // for student
    @Transactional
    public Page<ReservationDTO> findByMemberIdAndToday(Pageable pageable){
        return toDTOPage(reservationRepository.findByMemberEntityIdAndToday(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                pageable));
    }


    // ========== Update ========== //
    @Transactional
    public void update(ReservationDTO reservationDTO){
        reservationRepository.save(reservationDTO.toEntity());
    }

    /**
     * <b>예약 상태 변경</b><br>ex) 예약을 취소할 경우 DB의 Reservation_Status에서 "예약"을 "취소"로 변경
     * @param num 예약 번호
     * @param status 변경할 상태
     */
    @Transactional
    public void updateStatus(Integer num, String status){
        // DB에 예약이 있는지 확인하는 로직 추가
        Page<ReservationEntity> reservationEntity = reservationRepository.findByMemberEntityIdAndToday(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                Pageable.unpaged());
        if(reservationEntity.isEmpty() || !hasReservation(reservationEntity, num)) throw new ReservationNotFoundException();
        reservationRepository.updateReservationStatus(num, status);
    }


    // ========== Delete ========== //
    @Transactional
    public void delete(Integer num){
        if (reservationRepository.findByNum(num) == null) throw new ReservationNotFoundException();
        reservationRepository.deleteByNum(num);
    }

    private Page<ReservationDTO> toDTOPage(Page<ReservationEntity> reservationEntityPage){
        return reservationEntityPage.map(ReservationEntity::toDTO);
    }

    private Boolean hasReservation(Page<ReservationEntity> reservationEntity, Integer num){
        for(ReservationEntity re : reservationEntity){
            if(re.getNum().equals(num)) return true;
        }
        return false;
    }

    private Boolean isDuplicatedReservation(Page<ReservationEntity> reservationEntities){
        for (ReservationEntity re : reservationEntities.getContent()) {
            if(re.getStatus().equals("예약"))
                return true;
        }
        return false;
    }
}
