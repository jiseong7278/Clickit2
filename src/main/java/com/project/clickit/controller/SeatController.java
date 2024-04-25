package com.project.clickit.controller;

import com.project.clickit.dto.SeatDTO;
import com.project.clickit.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${requestMapping.seat}", produces="application/json;charset=UTF-8")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("${seat.duplicateCheck}")
    public ResponseEntity<Object> duplicationCheck(@RequestParam("id") String id){
        if(seatService.isExist(id))
            return ResponseEntity.badRequest().body("이미 존재하는 좌석 아이디입니다.");
        else
            return ResponseEntity.ok().body("사용 가능한 좌석 아이디입니다.");
    }

    @PostMapping("${seat.create}")
    public ResponseEntity<Object> createSeat(@RequestBody SeatDTO seatDTO){
        seatService.createSeat(seatDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("${seat.createList}")
    public ResponseEntity<Object> createList(@RequestBody List<SeatDTO> seatDTOList){
        seatService.createList(seatDTOList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${seat.getAll}")
    public ResponseEntity<Page<SeatDTO>> getAll(@PageableDefault(direction = Sort.Direction.ASC,
            sort = "id")Pageable pageable){
        return ResponseEntity.ok().body(seatService.getAll(pageable));
    }

    @GetMapping("${seat.findById}")
    public ResponseEntity<SeatDTO> findById(@RequestParam("id") String id){
        return ResponseEntity.ok().body(seatService.findById(id));
    }

    @GetMapping("${seat.findByFacilityId}")
    public ResponseEntity<Page<SeatDTO>> findByFacilityId(@RequestParam("facilityId") String facilityId,
                                                   @PageableDefault(direction = Sort.Direction.ASC,
                                                           sort = "id")Pageable pageable){
        return ResponseEntity.ok().body(seatService.findByFacilityId(facilityId, pageable));
    }

    @PutMapping("${seat.update}")
    public ResponseEntity<Object> updateSeat(@RequestBody SeatDTO seatDTO){
        seatService.updateSeat(seatDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${seat.updateSeatFacility}")
    public ResponseEntity<Object> updateSeatFacility(@RequestParam("id") String id, @RequestParam("facilityId") String facilityId){
        seatService.updateSeatFacility(id, facilityId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${seat.updateIsEmpty}")
    public ResponseEntity<Object> updateIsEmpty(@RequestParam("id") String id, @RequestParam("isEmpty") boolean isEmpty){
        seatService.updateSeatIsEmpty(id, isEmpty);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("${seat.delete}")
    public ResponseEntity<Object> deleteSeat(@RequestParam("id") String id){
        seatService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
