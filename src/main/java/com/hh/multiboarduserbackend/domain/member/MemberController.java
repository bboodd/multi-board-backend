package com.hh.multiboarduserbackend.domain.member;

import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.domain.member.request.DuplicateCheckRequestDto;
import com.hh.multiboarduserbackend.domain.member.request.LogInRequestDto;
import com.hh.multiboarduserbackend.domain.member.request.SignUpDto;
import com.hh.multiboarduserbackend.domain.member.response.LogInResponseDto;
import com.hh.multiboarduserbackend.mappers.MemberMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/members")
public class MemberController {

    private final MemberService memberService;

    private final MemberMapper memberModelMapper = Mappers.getMapper(MemberMapper.class);

    // 아이디 중복체크
    @PostMapping("/check-duplicate")
    public ResponseEntity<Response> checkDuplicate(@RequestBody @Valid DuplicateCheckRequestDto duplicateCheckRequestDto) {

        try {
            memberService.duplicateCheck(duplicateCheckRequestDto.loginId());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.valid(false));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.valid(true));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Response> logIn(@RequestBody @Valid LogInRequestDto logInRequestDto) {

        MemberVo memberVo = memberModelMapper.toVo(logInRequestDto);

        LogInResponseDto tokenAndNickname = memberService.logIn(memberVo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(tokenAndNickname));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Response> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        MemberVo memberVo = memberModelMapper.toVo(signUpDto);

        memberService.signUp(memberVo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message("회원가입 완료"));
    }

}
