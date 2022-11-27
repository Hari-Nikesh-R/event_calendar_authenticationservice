package com.example.authenticationService.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.authenticationService.Utils.Constants.EMAIL_VALIDATION;
import static com.example.authenticationService.Utils.Constants.PASSWORD_VALIDATION;

public class Utility {

    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_VALIDATION);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public static boolean validateEmailId(String mailId){
    Pattern pattern=Pattern.compile(EMAIL_VALIDATION);
    Matcher matcher=pattern.matcher(mailId);
    return  matcher.matches();
}
}
