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
@RequestMapping(value = "${requestMapping.member}", produces="application/json;charset=UTF-8")
public class MemberController {
    final MemberService memberService;

    @GetMapping("${member.getAll}")
    @ResponseBody
    public ResponseEntity getAll() {
        return ResponseEntity.ok().body(memberService.getAll());
    }

    @PostMapping("${member.create}")
    public ResponseEntity create(@RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok().body(memberService.create(memberDTO));
    }

    @PostMapping("${member.createList}")
    public ResponseEntity createList(@RequestBody List<MemberDTO> MemberDTOList) {
        memberService.createList(MemberDTOList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${member.findByMemberId}")
    public ResponseEntity findByMemberId(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(memberService.findByMemberId(id));
    }

    @PutMapping("${member.update}")
    public ResponseEntity updateMember(@RequestBody MemberDTO memberDTO) {
        memberService.updateMember(memberDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("${member.delete}")
    public ResponseEntity deleteMember(@RequestParam("id") String id) {
        memberService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
