package prm392.orderfood.androidapp.utils;

public class PhoneNumberUtils {
    /**
     * Validates a phone number based on a simple pattern.
     * This method checks if the phone number starts with a '+' followed by digits,
     * and has a length between 10 and 15 characters.
     *
     * @param phoneNumber The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.matches("^(84|0[35789])[0-9]{8}$");
    }
    /**
     * Formats a Vietnamese phone number to the international format.
     * If the phone number starts with '0', it replaces it with '+84'.
     * If it starts with '84', it replaces it with '+84'.
     * Otherwise, it returns the phone number as is.
     *
     * @param raw The raw phone number string.
     * @return The formatted phone number string.
     */
    public static String formatVietnamesePhone(String raw) {
        if (raw.startsWith("0")) return "+84" + raw.substring(1);
        if (raw.startsWith("84")) return "+84" + raw.substring(2);
        return raw; // fallback
    }
}
