package com.project.clickit.controller;

import com.project.clickit.dto.MemberDTO;
import com.project.clickit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping(value = "${requestMapping.member}", produces="application/json;charset=UTF-8")
public class MemberController {
    final MemberService memberService;

    // Create
    @PostMapping("${member.create}")
    public ResponseEntity<String> create(@RequestBody MemberDTO memberDTO) {
        memberService.create(memberDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("${member.createList}")
    public ResponseEntity<String> createList(@RequestBody List<MemberDTO> MemberDTOList) {
        memberService.createList(MemberDTOList);
        return ResponseEntity.ok().build();
    }


    // Read
    @GetMapping("${member.getAll}")
    @ResponseBody
    public ResponseEntity<Page<MemberDTO>> getAll(@PageableDefault(direction = Sort.Direction.ASC,
            sort = "studentNum") Pageable pageable) {
        return ResponseEntity.ok().body(memberService.getAll(pageable));
    }

    @GetMapping("${member.getAllStudent}")
    @ResponseBody
    public ResponseEntity<Page<MemberDTO>> getAllStudent(@PageableDefault(direction = Sort.Direction.ASC,
            sort = "studentNum") Pageable pageable) {
        return ResponseEntity.ok().body(memberService.getAllStudent(pageable));
    }

    @GetMapping("${member.findByMemberId}")
    public ResponseEntity<MemberDTO> findByMemberId(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(memberService.findByMemberId(id));
    }

    @GetMapping("${member.findByMemberName}")
    public ResponseEntity<Page<MemberDTO>> findByMemberName(@RequestParam("name") String name,
                                           @PageableDefault(direction = Sort.Direction.ASC,
                                                   sort = "studentNum") Pageable pageable) {
        return ResponseEntity.ok().body(memberService.findByMemberName(name, pageable));
    }

    @GetMapping("${member.findByDormitoryId}")
    public ResponseEntity<Page<MemberDTO>> findByDormitoryId(@RequestParam("dormitoryId") String dormitoryId,
                                            @PageableDefault(direction = Sort.Direction.ASC,
                                                    sort = "studentNum")Pageable pageable){
        return ResponseEntity.ok().body(memberService.findByDormitoryId(dormitoryId, pageable));
    }


    // Update
    @PutMapping("${member.updatePhone}")
    public ResponseEntity<String> updatePhone(@RequestParam("phone") String phone) {
        memberService.updatePhone(phone);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${member.updateEmail}")
    public ResponseEntity<String> updateEmail(@RequestParam("email") String email) {
        memberService.updateEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${member.updatePassword}")
    public ResponseEntity<String> updatePassword(@RequestParam("password") String password) {
        memberService.updatePassword(password);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${member.updateMemberForStaff}")
    public ResponseEntity<String> updateMemberForStaff(@RequestBody MemberDTO memberDTO) {
        memberService.updateMemberForStaff(memberDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("${member.updateRefreshToken}")
    public ResponseEntity<String> updateRefreshToken(@RequestParam("id") String id, @RequestParam("refreshToken") String refreshToken) {
        memberService.updateRefreshToken(id, refreshToken);
        return ResponseEntity.ok().build();
    }


    // Delete
    @DeleteMapping("${member.delete}")
    public ResponseEntity<Object> deleteMember(@RequestParam("id") String id) {
        memberService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("${member.deleteAllStudent}")
    public ResponseEntity<Object> deleteAll() {
        memberService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
