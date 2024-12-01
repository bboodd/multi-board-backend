package com.spring.multiboardbackend.domain.member.api;

import com.spring.multiboardbackend.domain.member.annotation.LoginMember;
import com.spring.multiboardbackend.domain.member.docs.MemberControllerDocs;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.mapper.MemberMapper;
import com.spring.multiboardbackend.domain.member.service.MemberService;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/members")
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @GetMapping("/me")
    @LoginMember
    public ResponseEntity<MemberResponse> getMyInfo(
    ) {
        Long memberId = AuthenticationContextHolder.getContext();

        MemberVO member = memberService.findById(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberMapper.toResponse(member));
    }

}
