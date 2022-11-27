package com.example.authenticationService.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {  public static boolean validatePassword(String password){
    Pattern pattern= Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
    Matcher matcher=pattern.matcher(password);
    return matcher.matches();
}
}
