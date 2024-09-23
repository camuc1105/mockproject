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
		// Kiểm tra số lần đã gửi mã xác minh trong 5 phút
		String attemptsKey = email + ":attempts";
		String attemptCountStr = redisTemplate.opsForValue().get(attemptsKey);
		// kiểm tra attemptCountStr nếu null thì gán là 0, k null thì ép kiểu thành số nguyên
		int attemptCount = attemptCountStr != null ? Integer.parseInt(attemptCountStr) : 0;

		if (attemptCount >= 3) {
			throw new IllegalStateException("Bạn chỉ có thể gửi mã xác minh tối đa 3 lần trong 5 phút.");
		}

		// Tạo mã và lưu trữ trong Redis với thời hạn 5 phút
		String code = String.format("%06d", new Random().nextInt(1000000));
		redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

		// Cập nhật số lần gửi mã
		redisTemplate.opsForValue().increment(attemptsKey);
		// Đặt thời gian hết hạn cho số lần gửi là 5 phút nếu đây là lần gửi đầu tiên
		if (attemptCount == 0) {
			redisTemplate.expire(attemptsKey, 5, TimeUnit.MINUTES);
		}

		return code;
	}

	public boolean validateCode(String email, String code) {
		String storedCode = redisTemplate.opsForValue().get(email);
		return code.equals(storedCode);
	}
}

