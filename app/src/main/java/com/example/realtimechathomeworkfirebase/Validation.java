package com.example.realtimechathomeworkfirebase;

public class Validation {


    public static boolean validate_email_password(String email, String password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern) && email.length() > 0 && Validation.validate_password(password)) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean validate_email(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern) && email.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validate_password(String password) {
        if (!(password.equals("") || password.length() < 15 || password.contains("-") || password.contains("_") || password.equals(" "))) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkMessage(String messgae) {
        if (messgae.equals(null) || messgae.equals(" ")) {
            return false;

        } else {
            return true;
        }
    }
}
