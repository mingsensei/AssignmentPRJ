package org.example.rf.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InputValidator {

    // Check if string is null or empty
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Validate email
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(regex);
    }

    // Validate phone number (e.g., 10 digits)
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        String regex = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$";
        return phone.matches(regex);
    }

    // Validate integer
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate floating number
    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate string length
    public static boolean isLengthBetween(String input, int min, int max) {
        if (input == null) return false;
        int length = input.length();
        return length >= min && length <= max;
    }

    // Validate date format (e.g., "yyyy-MM-dd")
    public static boolean isValidDate(String date, String format) {
        if (isEmpty(date)) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false); // exact match
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Validate strong password
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password)) return false;
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(regex);
    }

    // Validate number in range
    public static boolean isInRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    // Validate URL
    public static boolean isValidURL(String url) {
        if (isEmpty(url)) return false;
        String regex = "^(https?:\\/\\/)?[\\w.-]+\\.[a-zA-Z]{2,}(:\\d+)?(\\/.*)?$";
        return url.matches(regex);
    }

    // === Extended Validators ===

    // Check if string only contains letters and spaces (for name)
    public static boolean isAlphabetic(String input) {
        if (isEmpty(input)) return false;
        return input.matches("^[A-Za-zÀ-ỹ\\s]+$");
    }

    // Format name: "nguyễn văn a" → "Nguyễn Văn A"
    public static String formatName(String input) {
        if (isEmpty(input)) return "";
        input = input.trim().toLowerCase().replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1));
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    // Remove special characters (keep only letters, digits, space)
    public static String removeSpecialChars(String input) {
        if (isEmpty(input)) return "";
        return input.replaceAll("[^\\w\\s]", "");
    }

    // Normalize string (trim and lowercase)
    public static String normalize(String input) {
        if (isEmpty(input)) return "";
        return input.trim().toLowerCase();
    }

    // Match 2 strings exactly
    public static boolean match(String a, String b) {
        return a != null && a.equals(b);
    }

    // Check if string contains only digits
    public static boolean hasOnlyDigits(String input) {
        return input != null && input.matches("^\\d+$");
    }

}
