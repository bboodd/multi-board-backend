package com.hh.multiboarduserbackend.domain.member;

import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.exception.CustomException;
import com.hh.multiboarduserbackend.jwt.JwtToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hh.multiboarduserbackend.domain.member.MemberVo.toVo;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/members")
public class MemberController {

    private final MemberService memberService;

    // 아이디 중복체크
    @PostMapping("/check-duplicate")
    public ResponseEntity<Response> checkDuplicate(@RequestBody @Valid DuplicateCheckRequestDto duplicateCheckRequestDto) {

        try {
            memberService.duplicateCheck(duplicateCheckRequestDto.loginId());
        } catch (CustomException e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message("Duplicate!!"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message("Not Duplicate"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Response> logIn(@RequestBody @Valid LogInRequestDto logInRequestDto) {

        LogInResponseDto tokenAndNickname = memberService.logIn(toVo(logInRequestDto));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(tokenAndNickname));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Response> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        memberService.signUp(toVo(signUpDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message("회원가입 완료"));
    }

}
