package com.project.clickit.controller;

import com.project.clickit.service.DormitoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/dormitory", produces="application/json;charset=UTF-8")
public class DormitoryController {
    private final DormitoryService dormitoryService;

    @Autowired
    public DormitoryController(DormitoryService dormitoryService){
        this.dormitoryService = dormitoryService;
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        return ResponseEntity.ok().body(dormitoryService.getAll());
    }
}
