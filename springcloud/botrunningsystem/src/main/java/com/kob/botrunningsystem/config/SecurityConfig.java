package com.kob.botrunningsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Spring Security 过滤链（Boot 4 写法）
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1️⃣ 关闭 CSRF（JWT 项目必须关）
                .csrf(AbstractHttpConfigurer::disable)

                // 2️⃣ 无状态 Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3️⃣ 请求权限配置
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/bot/add/", "/bot/remove/")
                        .access(new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1')"))
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 这是 Spring Security 6.x 推荐的写法，优先级最高，100% 生效
        return (web) -> web.ignoring()
                .requestMatchers("/websocket/**"); // 放行 WebSocket 所有路径
    }


}
