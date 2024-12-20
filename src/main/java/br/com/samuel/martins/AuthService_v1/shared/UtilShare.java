package br.com.samuel.martins.AuthService_v1.shared;

public class UtilShare {

    public static final String PASSWORD_MESSAGE = "\"Password must have at least: \\\"\\n\" + \"\"\n" +
            "                    + \"+ \\\"1 uppercase letter, 1 lowercase letter, 1 special character, \" +\n" +
            "                    \"and a minimum length of 12 characters.\"";

    public static final String EMAIL_MESSAGE = "Invalid email format. An email must follow these rules:\n" +
            "1. Start with letters, numbers, or special characters like '.', '_', '%', '+', '-'.\n" +
            "2. Contain exactly one '@'.\n" +
            "3. Have a valid domain name (e.g., domain.com, sub.domain.org).\n" +
            "4. End with a valid domain extension (e.g., .com, .org, .net).";

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final String REGEX_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\W_]).{8,}$";

}
