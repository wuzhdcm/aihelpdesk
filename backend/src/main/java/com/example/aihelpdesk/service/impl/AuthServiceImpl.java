package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.common.JwtTokenService;
import com.example.aihelpdesk.model.dto.LoginRequest;
import com.example.aihelpdesk.model.dto.LoginResponse;
import com.example.aihelpdesk.model.dto.LoginUserInfo;
import com.example.aihelpdesk.model.entity.SysUser;
import com.example.aihelpdesk.service.AuthService;
import com.example.aihelpdesk.service.ISysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/3 03:09
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final ISysUserService sysUserService;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            ISysUserService sysUserService,
            JwtTokenService tokenService,
            PasswordEncoder passwordEncoder
    ) {
        this.sysUserService = sysUserService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        //查询数据库是否有当前用户
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername,loginRequest.username()).one();

        if(sysUser == null){
            throw new IllegalArgumentException("用户名或密码错误");
        }

        if (!"ENABLED".equals(sysUser.getStatus())) {
            throw new IllegalArgumentException("用户已被禁用");
        }

        if (!passwordEncoder.matches(loginRequest.password(),  sysUser.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        String token = tokenService.generate(sysUser.getId(), sysUser.getUsername());

        LoginUserInfo loginUserInfo = new LoginUserInfo(
                sysUser.getId(),
                sysUser.getUsername(),
                sysUser.getNickname(),
                List.of()
        );

        return new  LoginResponse(token, loginUserInfo);
    }
}
