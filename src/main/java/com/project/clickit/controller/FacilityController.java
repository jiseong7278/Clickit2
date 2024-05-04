package com.project.clickit.controller;

import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "${requestMapping.facility}", produces="application/json;charset=UTF-8")
public class FacilityController {

    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping("${facility.duplicateCheck}")
    public ResponseEntity<Object> duplicationCheck(@RequestParam("id") String id){
        if(facilityService.isExist(id))
            return ResponseEntity.badRequest().body("이미 존재하는 시설 아이디입니다.");
        else
            return ResponseEntity.ok().body("사용 가능한 시설 아이디입니다.");
    }

    @PostMapping("${facility.create}")
    public ResponseEntity<String> createFacility(@RequestBody FacilityDTO facilityDTO){
        facilityService.createFacility(facilityDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${facility.getAll}")
    public ResponseEntity<Page<FacilityDTO>> getAll(@PageableDefault(direction = Sort.Direction.ASC,
    sort = "name") Pageable pageable){
        return ResponseEntity.ok().body(facilityService.getAll(pageable));
    }

    @GetMapping("${facility.findById}")
    public ResponseEntity<FacilityDTO> findById(@RequestParam("id") String id){
        return ResponseEntity.ok().body(facilityService.findById(id));
    }

    @GetMapping("${facility.findByName}")
    public ResponseEntity<Page<FacilityDTO>> findByName(@RequestParam("name") String name,
                                           @PageableDefault(direction = Sort.Direction.ASC, sort = "name") Pageable pageable){
        return ResponseEntity.ok().body(facilityService.findByName(name, pageable));
    }

    @GetMapping("${facility.findByDormitoryId}")
    public ResponseEntity<Page<FacilityDTO>> findByDormitoryId(@RequestParam("dormitoryId") String dormitoryId,
                                                    @PageableDefault(direction = Sort.Direction.ASC, sort = "name") Pageable pageable){
        return ResponseEntity.ok().body(facilityService.findByDormitoryId(dormitoryId, pageable));
    }

    @PutMapping("${facility.update}")
    public ResponseEntity<String> updateFacility(@RequestBody FacilityDTO facilityDTO){
        facilityService.updateFacility(facilityDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${facility.updateFacilityId}")
    public ResponseEntity<String> updateFacilityId(@RequestParam("id") String id, @RequestParam("newId") String newId){
        facilityService.updateFacilityId(id, newId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("${facility.delete}")
    public ResponseEntity<String> deleteFacility(@RequestParam("id") String id){
        facilityService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
