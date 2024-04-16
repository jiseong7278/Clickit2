package com.project.clickit.controller;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.service.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "${requestMapping.dormitory}", produces="application/json;charset=UTF-8")
public class DormitoryController {

    private final DormitoryService dormitoryService;

    @Autowired
    public DormitoryController(DormitoryService dormitoryService){
        this.dormitoryService = dormitoryService;
    }

    @GetMapping("${dormitory.duplicateCheck}")
    public ResponseEntity<Object> duplicationCheck(@RequestParam("id") String id){
        if(dormitoryService.isExist(id))
            return ResponseEntity.badRequest().body("이미 존재하는 기숙사입니다.");
        else
            return ResponseEntity.ok().body("사용 가능한 기숙사입니다.");
    }

    @PostMapping("${dormitory.create}")
    public ResponseEntity<Object> createDormitory(@RequestBody DormitoryDTO dormitoryDTO){
        dormitoryService.createDormitory(dormitoryDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${dormitory.getAll}")
    public ResponseEntity<Object> getAll(@PageableDefault(direction = Sort.Direction.DESC,
            sort = "name", size=10, page=0) Pageable pageable){
        return ResponseEntity.ok().body(dormitoryService.getAll(pageable));
    }

    @GetMapping("${dormitory.findById}")
    public ResponseEntity<Object> findById(@RequestParam("id") String id){
        return ResponseEntity.ok().body(dormitoryService.findById(id));
    }

    @GetMapping("${dormitory.findByName}")
    public ResponseEntity<Object> findByName(@RequestParam("name") String name, @PageableDefault(direction = Sort.Direction.DESC,
            sort = "name", size=10, page=0) Pageable pageable){
        return ResponseEntity.ok().body(dormitoryService.findByName(name, pageable));
    }

    @PutMapping("${dormitory.update}")
    public ResponseEntity<Object> updateDormitoryName(@RequestBody DormitoryDTO dormitoryDTO){
        dormitoryService.updateDormitory(dormitoryDTO);
        return ResponseEntity.ok().build();
    }
}
