package com.site.restauranttier.controller;


import com.site.restauranttier.user.UserCreateForm;
import com.site.restauranttier.service.UserService;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    
    // 실제 로그인을 진행하는 post는 스프링 시큐리티가 해줌
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }


    // signup에 Get => 회원가입 템플릿
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }
    // signup에 POST => 회원가입 진행 -> db에 user 생성
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        // 비밀번호와 비밀번호 확인에 입력값이 다를때 예외처리
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        
        // 닉네임과 email이 겹치는 회원가입일때 예외처리
        try {
            
            // 일단 service 안에 throw DataIntegrityViolationException 처리 해놨음. 엔티티 수정 필요
            userService.create(userCreateForm.getUserId(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getNickname());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/user/login";
    }
    @GetMapping("/myPage")
    public String myPage(){
        return "mypage";
    }

}
