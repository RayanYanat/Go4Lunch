package com.example.go4lunch.utils;

public class StringFormat {

    public static String getFormattedString(String str1,String str2,String str3) {
        try {
            return String.format("%s%s%s",str1,str2,str3);
        }
        catch (Exception ignored) {
        }

        return null;
    }

    public static String getFormattedString2(String str1,String str2) {
        try {
            return String.format("%s%s",str1,str2);
        }
        catch (Exception ignored) {
        }

        return null;
    }
}
