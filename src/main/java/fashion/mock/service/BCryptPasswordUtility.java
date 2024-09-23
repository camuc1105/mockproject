package fashion.mock.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordUtility {
    private static final BCryptPasswordEncoder passwordEcorder = new BCryptPasswordEncoder();

    public String bcryptEncryptor(String plainText){
        return passwordEcorder.encode(plainText);
    }
    public Boolean doPasswordMatch(String rawPassword, String encodedPassword){
        return passwordEcorder.matches(rawPassword, encodedPassword);
    }
}