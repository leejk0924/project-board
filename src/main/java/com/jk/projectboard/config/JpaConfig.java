package com.jk.projectboard.config;

import com.jk.projectboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware(){
        // TODO : 스프링 시큐리티로 인증 기능을 붙이고 기능 추가
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                // Authentication 에 인증 되었는지 확인하는 필터
                .filter(Authentication::isAuthenticated)
                // 인증되었을 경우 Principal 을 꺼내온다.
                .map(Authentication::getPrincipal)
                // BoardPrincipal 으로 타입 캐스팅
                .map(BoardPrincipal.class::cast)
                .map(BoardPrincipal::getUsername);
    }
}
