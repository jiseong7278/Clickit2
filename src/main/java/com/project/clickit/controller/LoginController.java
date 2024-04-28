package com.project.clickit.controller;


import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("${login.sendVerifyBySMS}")
    public ResponseEntity<String> smsVerify(@RequestParam("id")String id){
        loginService.sendVerifyCodeBySMS(id);
        return ResponseEntity.ok().body("인증번호가 전송되었습니다.");
    }

    @PostMapping("${login.sendVerifyByEmail}")
    public ResponseEntity<String> emailVerify(@RequestParam("id")String id){
        loginService.sendVerifyCodeByEmail(id);
        return ResponseEntity.ok().body("인증번호가 전송되었습니다.");
    }

    @PostMapping("${login.verifyCodeBySMS}")
    public ResponseEntity<String> verifyCode(@RequestParam("key")String key, @RequestParam("code")String code){
        if (!loginService.verification(key, code))
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");

        return ResponseEntity.ok().body("인증되었습니다.");
    }

    @PostMapping("${login.verifyCodeByEmail}")
    public ResponseEntity<String> verifyCodeByEmail(@RequestParam("key")String key, @RequestParam("code")String code){
        if (!loginService.verification(key, code))
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");

        return ResponseEntity.ok().body("인증되었습니다.");
    }

    @PutMapping("${login.updatePasswordBySMS}")
    public ResponseEntity<String> updatePasswordBySMS(@RequestParam("phone")String phone, @RequestParam("password")String password){
        loginService.updatePasswordByPhone(phone, password);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    @PutMapping("${login.updatePasswordByEmail}")
    public ResponseEntity<String> updatePasswordByEmail(@RequestParam("email")String email, @RequestParam("password")String password){
        loginService.updatePasswordByEmail(email, password);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    @PostMapping("${login.logout}")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token){
        loginService.logout(token);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }
}
