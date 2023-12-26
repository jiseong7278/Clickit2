package com.project.clickit.controller;

import com.project.clickit.dormitory.service.DormitoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/dormitory")
public class DormitoryController {
    final DormitoryService dormitoryService;

    @GetMapping("/test")
    public String test() {
        return "dormitory_test";
    }
}
