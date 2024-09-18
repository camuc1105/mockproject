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

@Configuration
public class SecurityConfig {
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/webjars/**",
						"/fontawesome-free-5.15.4-web/**")
				.permitAll().requestMatchers("/", "/trangchu", "/login", "/register").permitAll()
				.requestMatchers("/users/**", "/products/**", "/categories/**").permitAll() // Cho phép truy cập không
																							// cần đăng
																							// nhập/reset-password
				.anyRequest().authenticated() // Các request khác cần đăng nhập
		).formLogin(formLogin -> formLogin.loginPage("/trangchu") // Trang đăng nhập tùy chỉnh
				.permitAll() // Cho phép tất cả mọi người truy cập trang đăng nhập
		).logout(logout -> logout.permitAll() // Cho phép tất cả mọi người đăng xuất
		);

		return http.build();
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}