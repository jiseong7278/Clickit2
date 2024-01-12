package com.project.clickit.controller;

import com.project.clickit.dto.MemberDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping(value = "/member", produces="application/json;charset=UTF-8")
public class MemberController {
    final MemberService memberService;

    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity getAll() {
        return ResponseEntity.ok().body(memberService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok().body(memberService.create(memberDTO));
    }

    @PostMapping("/createList")
    public ResponseEntity createList(@RequestBody List<MemberDTO> MemberDTOList) {
        memberService.createList(MemberDTOList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findByMemberId")
    public ResponseEntity findByMemberId(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(memberService.findByMemberId(id));
    }
}
