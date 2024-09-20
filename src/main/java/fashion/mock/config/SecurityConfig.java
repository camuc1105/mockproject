/**
 * Author: Ngô Văn Quốc Thắng 11/05/1996
 */
package fashion.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(authorize -> authorize
//				.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/webjars/**", "/register", "/save",
//						"/forgot-password", "/verify-code", "/reset-password", "/users/**", "/products/**",
//						"/categories/**", "/product/**", "/inputCode", "/inputCode?email=", "/resendCode", "/inputCode",
//						"/verify", "/phuc")
//				.permitAll() // Allow access to static resources and public endpoints
//				.anyRequest().authenticated() // All other requests require authentication
//		).formLogin(formLogin -> formLogin.loginPage("/login").permitAll()) // Custom login page// Allow everyone to
//																			// access the login page
//				.logout(logout -> logout.permitAll()); // Allow everyone to log out
//
//		return http.build();
//	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.requestMatchers("/register", "/save", "/forgot-password",
				"/verify-code", "/reset-password", "/product/**","/css/**", "/js/**", "/images/**"
				,"/users/**", "/products/**", "/categories/**", "/discounts/**").permitAll().anyRequest().authenticated())
				.formLogin((form) -> form.loginPage("/login").permitAll()).logout((logout) -> logout.permitAll());
		return http.build();
	}

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests((requests) -> requests
//				.requestMatchers("/register", "/save", "/forgot-password", "/verify-code", "/reset-password")
//				.permitAll() // Cho phép truy cập không cần đăng nhập
//				.anyRequest().authenticated()).formLogin((form) -> form.loginPage("/login") // Trang login tùy chỉnh
//						.permitAll())
//				.logout((logout) -> logout.permitAll());
//
//		return http.build();
//	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
