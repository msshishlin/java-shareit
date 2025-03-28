package ru.practicum.shareit.generator;

import java.util.Random;

public class StringGenerator {
    public static String generateString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateUserName() {
        return String.format("%s %s", generateString(7), generateString(10));
    }

    public static String generateUserEmail() {
        return String.format("%s@%s.ru", generateString(10), generateString(4));
    }

    public static String generateItemRequestDescription() {
        return generateString(100);
    }

    public static String generateItemName() {
        return generateString(15);
    }

    public static String generateItemDescription() {
        return generateString(100);
    }

    public static String generateCommentText() {
        return generateString(150);
    }
}
