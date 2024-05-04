package com.project.clickit.controller;

import com.project.clickit.dto.ReservationDTO;
import com.project.clickit.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value="${requestMapping.reservation}", produces="application/json;charset=UTF-8")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("${reservation.create}")
    public ResponseEntity<String> create(@RequestBody ReservationDTO reservationDTO){
        reservationService.create(reservationDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${reservation.getAll}")
    public ResponseEntity<Page<ReservationDTO>> getAll(@PageableDefault(direction = Sort.Direction.ASC, sort = "num")Pageable pageable){
        return ResponseEntity.ok(reservationService.findAll(pageable));
    }

    @GetMapping("${reservation.findByMemberId}")
    public ResponseEntity<Page<ReservationDTO>> findByMemberId(@RequestParam("memberId") String memberId, @PageableDefault(direction = Sort.Direction.ASC, sort = "num")Pageable pageable){
        return ResponseEntity.ok(reservationService.findByMemberId(memberId, pageable));
    }

    @GetMapping("${reservation.findMyReservation}")
    public ResponseEntity<Page<ReservationDTO>> findMyReservation(@PageableDefault(direction = Sort.Direction.ASC, sort = "num")Pageable pageable){
        return ResponseEntity.ok(reservationService.findByMemberId(pageable));
    }

    @GetMapping("${reservation.findBySeatIdAndToday}")
    public ResponseEntity<Page<ReservationDTO>> findBySeatIdAndToday(@RequestParam("seatId") String seatId, @PageableDefault(direction = Sort.Direction.ASC, sort = "num")Pageable pageable){
        return ResponseEntity.ok(reservationService.findBySeatIdAndToday(seatId, pageable));
    }

    @GetMapping("${reservation.findByMemberIdAndToday}")
    public ResponseEntity<Page<ReservationDTO>> findByMemberIdAndToday(@RequestParam("memberId") String memberId, @PageableDefault(direction = Sort.Direction.ASC, sort = "num")Pageable pageable){
        return ResponseEntity.ok(reservationService.findByMemberIdAndToday(memberId, pageable));
    }

    @GetMapping("${reservation.findMyReservationToday}")
    public ResponseEntity<Page<ReservationDTO>> findMyReservationToday(@PageableDefault(direction = Sort.Direction.ASC, sort = "num")Pageable pageable){
        return ResponseEntity.ok(reservationService.findByMemberIdAndToday(pageable));
    }

    @PutMapping("${reservation.update}")
    public ResponseEntity<String> update(@RequestBody ReservationDTO reservationDTO){
        reservationService.update(reservationDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${reservation.updateStatus}")
    public ResponseEntity<String> updateStatus(@RequestBody List<ReservationDTO> reservationDTOList){
        for (ReservationDTO reservationDTO : reservationDTOList) {
            reservationService.updateStatus(reservationDTO.getNum(), reservationDTO.getStatus());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("${reservation.delete}")
    public ResponseEntity<String> delete(@RequestParam("num") Integer num){
        reservationService.delete(num);
        return ResponseEntity.ok().build();
    }
}
