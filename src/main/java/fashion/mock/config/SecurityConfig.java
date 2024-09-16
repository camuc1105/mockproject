package fashion.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests((requests) -> requests
                    .requestMatchers("/register", "/save", "/forgot-password", "/verify-code", "/reset-password" ).permitAll()  // Cho phép truy cập không cần đăng nhập
                    .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                    .loginPage("/login")  // Trang login tùy chỉnh
                    .permitAll()
            )
            .logout((logout) -> logout.permitAll());

    return http.build();
}
}
