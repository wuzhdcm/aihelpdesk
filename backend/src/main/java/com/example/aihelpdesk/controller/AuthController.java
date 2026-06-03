package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.dto.LoginRequest;
import com.example.aihelpdesk.model.dto.LoginResponse;
import com.example.aihelpdesk.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author wzh
 * @Date 2026/6/3 03:31
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    Result<LoginResponse> loginResult(@Valid  @RequestBody LoginRequest loginRequest){
        return Result.success(authService.login(loginRequest));
    }

    @GetMapping("/me")
    public Result<CurrentUser> me() {
        return Result.success(CurrentUserContext.getRequired());
    }
}
