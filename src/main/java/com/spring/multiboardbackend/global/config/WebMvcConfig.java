package com.spring.multiboardbackend.global.config;

import com.spring.multiboardbackend.global.interceptor.AdminAuthInterceptor;
import com.spring.multiboardbackend.global.interceptor.AuthInterceptor;
import com.spring.multiboardbackend.global.interceptor.LoggerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggerInterceptor loggerInterceptor;
    private final AuthInterceptor authInterceptor;
    private final AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**")
                .addResourceLocations("classpath:/static/bootstrap/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/vendor/**")
                .addResourceLocations("classpath:/static/vendor/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/api/**");

        registry.addInterceptor(adminAuthInterceptor)
                .order(2)
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/login",
                        "/bootstrap/**",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/vendor/**",
                        "/fonts/**",
                        "/favicon.ico"
                );

        registry.addInterceptor(loggerInterceptor)
                .order(3)
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/bootstrap/**", "/favicon.ico");
    }
}