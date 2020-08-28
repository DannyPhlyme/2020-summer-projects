package com.udacity.jwdnd.course1.cloudstorage.utils;

import java.security.SecureRandom;

public class StringGenerator {
    public static String random() {
        return random(8);
    }

    public static String random(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        SecureRandom random = new SecureRandom();
        String generatedString;

        generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toLowerCase();

        return generatedString;
    }
}
