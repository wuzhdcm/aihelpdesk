package com.example.aihelpdesk.service;

import com.example.aihelpdesk.model.dto.LoginRequest;
import com.example.aihelpdesk.model.dto.LoginResponse;

/**
 * @Author wzh
 * @Date 2026/6/3 03:09
 */
public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
}
