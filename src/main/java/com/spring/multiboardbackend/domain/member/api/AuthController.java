package com.spring.multiboardbackend.domain.member.api;

import com.spring.multiboardbackend.domain.member.docs.AuthControllerDocs;
import com.spring.multiboardbackend.domain.member.dto.request.DuplicateCheckRequest;
import com.spring.multiboardbackend.domain.member.dto.request.LoginRequest;
import com.spring.multiboardbackend.domain.member.dto.request.SignUpRequest;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.mapper.MemberMapper;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/boards/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MemberResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        validateSignupPasswords(request.password(), request.checkPassword(), request.loginId());

        MemberVO signup = memberMapper.toVO(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberMapper.toResponse(authService.signUp(signup)));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody LoginRequest request) {

        MemberVO member = authService.login(request.loginId(), request.password());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtProvider.generateToken(member.getId()));
    }

    @PostMapping("/check-duplicate/login-id")
    public ResponseEntity<Boolean> checkDuplicateLoginId(@Valid @RequestBody DuplicateCheckRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.checkDuplicate(request.value(), "LOGIN_ID"));
    }

    @PostMapping("/check-duplicate/nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@Valid @RequestBody DuplicateCheckRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.checkDuplicate(request.value(), "NICKNAME"));

    }

    private void validateSignupPasswords(String password, String checkPassword, String loginId) {
        if (password == null || checkPassword == null) {
            throw MemberErrorCode.SIGN_UP_PASSWORD_CHECK_ERROR.defaultException();
        }

        if (!password.equals(checkPassword)) {
            throw MemberErrorCode.SIGN_UP_PASSWORD_CHECK_ERROR.defaultException();
        }

        if (loginId != null && loginId.equals(password)) {
            throw MemberErrorCode.ID_PASSWORD_EQUALS_ERROR.defaultException();
        }
    }

}
