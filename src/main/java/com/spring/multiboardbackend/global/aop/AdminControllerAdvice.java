package com.spring.multiboardbackend.global.aop;

import com.spring.multiboardbackend.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice(basePackages = "com.spring.multiboardbackend.domain.admin")
@RequiredArgsConstructor
public class AdminControllerAdvice {

    private final SecurityUtil securityUtil;

    @ModelAttribute
    public void addAttributes(Model model) {
        try {
            String adminName = securityUtil.getCurrentMemberNickname();
            model.addAttribute("adminName", adminName);
        } catch (Exception e) {
            log.warn("Admin name could not be retrieved: {}", e.getMessage());
            // 필요 시 기본값을 설정하거나, 특정 로직을 추가할 수 있습니다.
            model.addAttribute("adminName", "관리자");
        }
    }
}

