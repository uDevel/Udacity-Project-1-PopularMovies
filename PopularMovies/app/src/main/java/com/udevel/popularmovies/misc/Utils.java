package com.udevel.popularmovies.misc;

import java.util.UUID;

/**
 * Created by benny on 8/9/2015.
 */
public class Utils {
    public static String getUniqueStringId() {
        return UUID.randomUUID().toString();
    }

    public static int getUniqueIntId() {
        return UUID.randomUUID().hashCode();
    }

    public static boolean compareString(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }
}
