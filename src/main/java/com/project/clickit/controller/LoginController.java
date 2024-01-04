package com.project.clickit.controller;


import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.exceptions.login.ConcurrentlySignUpException;
import com.project.clickit.exceptions.login.DuplicatedIdException;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

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
        return ResponseEntity.ok().body(loginService.signUp(memberDTO));
    }

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody LoginDTO loginDTO){
        return new ResponseEntity(loginService.login(loginDTO), HttpStatus.OK);
    }
}
