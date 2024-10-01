package com.nbe2.domain.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.nbe2.domain.user.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenGenerator tokenGenerator;
    private final TokenManager tokenManager;
    private final Authenticator authenticator;
    private final UserValidator userValidator;
    private final UserAppender userAppender;
    private final TokenProvider tokenProvider;

    @Transactional
    public void signUp(
            UserProfile userProfile, Optional<Long> emergencyRoomId, Optional<Long> licenseId) {
        userValidator.validate(userProfile.email());

        if (userValidator.isMedicalUser(emergencyRoomId, licenseId)) {
            MedicalProfile medicalProfile = userValidator.validate(emergencyRoomId, licenseId);
            userAppender.append(userProfile, medicalProfile);
        } else {
            userAppender.append(userProfile);
        }
    }

    public Tokens login(Login login) {
        UserPrincipal userPrincipal = authenticator.authenticate(login);
        Tokens tokens = tokenGenerator.generate(userPrincipal);
        System.out.println(tokens);
        tokenManager.save(RefreshToken.of(userPrincipal.userId(), tokens.refreshToken()));

        return tokens;
    }

    public void logout(long userId) {
        tokenManager.removeRefreshToken(userId);
    }

    public Tokens updateToken(Tokens tokens) {
        UserPrincipal userPrincipal = tokenProvider.getTokenUserPrincipal(tokens.refreshToken());
        tokenManager.checkRefreshToken(userPrincipal.userId());
        Tokens newRefreshToken = tokenGenerator.generate(userPrincipal);
        tokenManager.save(newRefreshToken.getRefreshToken(userPrincipal.userId()));

        return newRefreshToken;
    }
}
