package com.project.clickit.controller;

import com.project.clickit.dto.ReservationDTO;
import com.project.clickit.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="${requestMapping.reservation}", produces="application/json;charset=UTF-8")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PutMapping("${reservation.updateStatus}")
    public ResponseEntity<Object> updateStatus(@RequestBody List<ReservationDTO> reservationDTOList){
        for (ReservationDTO reservationDTO : reservationDTOList) {
            reservationService.updateStatus(reservationDTO.getNum(), reservationDTO.getStatus());
        }
        return ResponseEntity.ok().build();
    }
}
