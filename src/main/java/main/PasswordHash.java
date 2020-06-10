package main;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHash {

    public static String hashPassword(String plainTextPassword) {

        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean combinePasswords(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
