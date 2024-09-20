/**
 * Trần Thảo
 */
package fashion.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // Định nghĩa bean PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Định nghĩa bean SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF để đơn giản hóa (chỉ nên làm điều này khi cần)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home/**", "/css/**", "/js/**", "/login/**").permitAll() // Cho phép truy cập không cần xác thực
                        .requestMatchers("/admin").hasAuthority("ADMIN") // Chỉ ADMIN mới được truy cập
                        .anyRequest().authenticated() // Các yêu cầu khác phải xác thực
                )
                .formLogin(form -> form
                        .loginPage("/login/loginform") // Trang đăng nhập tùy chỉnh
                        .defaultSuccessUrl("/home", true) // Chuyển hướng sau khi đăng nhập thành công
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home") // Chuyển hướng sau khi đăng xuất
                        .permitAll()
                );
        return http.build();
    }
}
