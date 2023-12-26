package com.project.clickit.controller;


import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseEntity("이미 가입된 아이디입니다.", HttpStatus.BAD_REQUEST) ;
        else
            return new ResponseEntity("사용 가능한 아이디입니다.", HttpStatus.OK) ;
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody MemberDTO memberDTO){
        return new ResponseEntity(loginService.signUp(memberDTO), HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody LoginDTO loginDTO){
        return new ResponseEntity(loginService.login(loginDTO), HttpStatus.OK);
    }
}
