/**
 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
 */
package fashion.mock.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

	private final StringRedisTemplate redisTemplate;

	public VerificationService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String generateAndStoreCode(String email) {
		String code = String.format("%06d", new Random().nextInt(1000000));
		redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
		return code;
	}

	public boolean validateCode(String email, String code) {
		String storedCode = redisTemplate.opsForValue().get(email);
		return code.equals(storedCode);
	}
}
