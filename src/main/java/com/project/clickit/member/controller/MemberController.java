package com.project.clickit.member.controller;

import com.project.clickit.member.domain.entity.MemberEntity;
import com.project.clickit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    final MemberService memberService;

    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity getAll() {
        return memberService.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody MemberEntity memberEntity) {
        return memberService.create(memberEntity);
    }

    @PostMapping("/createList")
    public ResponseEntity createList(@RequestBody List<MemberEntity> memberEntityList) {
        return memberService.createList(memberEntityList);
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }
}
