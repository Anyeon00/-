package com.nbe2.api.auth;

import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.nbe2.api.auth.dto.SignupRequest;
import com.nbe2.api.global.dto.Response;
import com.nbe2.domain.auth.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;

    @PostMapping("signup")
    public Response<Void> signUp(
            @RequestBody SignupRequest signupRequest,
            @RequestParam(required = false) Optional<Long> emergencyRoomId,
            @RequestParam(required = false) Optional<Long> fileId) {
        authService.signUp(signupRequest.toUserProfile(), emergencyRoomId, fileId);
        return Response.success();
    }
}
