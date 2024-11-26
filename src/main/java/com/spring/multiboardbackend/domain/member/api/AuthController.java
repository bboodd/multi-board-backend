package com.spring.multiboardbackend.domain.member.api;

import com.spring.multiboardbackend.domain.member.docs.AuthControllerDocs;
import com.spring.multiboardbackend.domain.member.dto.request.DuplicateCheckRequest;
import com.spring.multiboardbackend.domain.member.dto.request.LoginRequest;
import com.spring.multiboardbackend.domain.member.dto.request.SignUpRequest;
import com.spring.multiboardbackend.domain.member.dto.response.LoginResponse;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MemberResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(request));
    }

    @PostMapping("/check-duplicate/login-id")
    public ResponseEntity<Boolean> checkDuplicateLoginId(
            @Valid @RequestBody DuplicateCheckRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.checkDuplicate(request.value(), "LOGIN_ID"));
    }

    @PostMapping("/check-duplicate/nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(
            @Valid @RequestBody DuplicateCheckRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.checkDuplicate(request.value(), "NICKNAME"));

    }


}
