package com.project.clickit.controller;


import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "${requestMapping.login}", produces="application/json;charset=UTF-8")
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

    @GetMapping("${login.duplicateCheck}")
    public ResponseEntity<String> duplicateCheck(@RequestParam("id") String id){
        if(loginService.isExist(id))
            return ResponseEntity.badRequest().body("이미 가입된 아이디입니다.") ;
        else
            return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
    }

    @PostMapping("${login.signUp}")
    public ResponseEntity<TokenDTO> signUp(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok().body(loginService.signUp(memberDTO, TYPE_STUDENT));
    }

    @PostMapping("${login.signIn}")
    public ResponseEntity<TokenDTO> signIn(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok().body(loginService.signIn(loginDTO));
    }

    @PostMapping("${login.smsVerify}")
    public ResponseEntity<String> smsVerify(@RequestParam("id")String id){
        loginService.sendVerifyCodeBySMS(id);
        return ResponseEntity.ok().body("인증번호가 전송되었습니다.");
    }

    @PostMapping("${login.emailVerify}")
    public ResponseEntity<String> emailVerify(@RequestParam("id")String id){
        loginService.sendVerifyCodeByEmail(id);
        return ResponseEntity.ok().body("인증번호가 전송되었습니다.");
    }

    @PostMapping("${login.verifyCodeBySMS}")
    public ResponseEntity<String> verifyCode(@RequestParam("key")String key, @RequestParam("code")String code){
        if (!loginService.verification(key, code))
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");

        return ResponseEntity.ok().body(loginService.findPasswordByPhone(key));
    }

    @PostMapping("${login.verifyCodeByEmail}")
    public ResponseEntity<String> verifyCodeByEmail(@RequestParam("key")String key, @RequestParam("code")String code){
        if (!loginService.verification(key, code))
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");

        return ResponseEntity.ok().body(loginService.findPasswordByEmail(key));
    }

    @PostMapping("${login.logout}")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token){
        loginService.logout(token);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }
}
