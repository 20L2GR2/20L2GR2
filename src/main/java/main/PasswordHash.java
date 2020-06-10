package main;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Klasa wykorzystywana jako klasa, zawierajaca logike, ktora jest wykorzystywana przy hashowaniu podanego hasla.
 */

public class PasswordHash {
    /**
     * Metoda odpowiadajaca za hashowania podanego hasla z klawiatury.
     *
     * @param plainTextPassword Parametr przyjmuje podane haslo z klawiatury.
     * @return Zwracane jest haslo hashowane.
     */

    public static String hashPassword(String plainTextPassword) {

        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Metoda sprawdzajaca, czy haslo podane z klawiatury po przehashowaniu jest haslem zahashowanym w bazie danych.
     *
     * @param plainPassword  Haslo podane z klawiatury przez uzytkownika.
     * @param hashedPassword Zahashowane haslo w bazie danych.
     * @return Zwracana wartosc true albo false, w zaleznosci od zgodnosci podanych hasel.
     */
    public static boolean combinePasswords(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}