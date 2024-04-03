package com.project.clickit.controller;


import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/login", produces="application/json;charset=UTF-8")
public class LoginController {
    private final LoginService loginService;

    @Value("${roles.dev}")
    private String TYPE_DEV;

    @Value("${roles.staff}")
    private String TYPE_STAFF;

    @Value("${roles.student}")
    private String TYPE_STUDENT;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @GetMapping("/duplicateCheck")
    public ResponseEntity duplicateCheck(@RequestParam("id") String id){
        if(loginService.duplicateCheck(id))
            return ResponseEntity.badRequest().body("이미 가입된 아이디입니다.") ;
        else
            return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok().body(loginService.signUp(memberDTO, TYPE_STUDENT));
    }

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok().body(loginService.signIn(loginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token){
        loginService.logout(token);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }
}
