package com.example.fyp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jadephong on 19/1/2016.
 */
public class Validator {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isOnlyChar(String target) {
        Boolean isChar = null;
        if (target == null)
            isChar = false;
        if (target.matches("[a-zA-Z]+")) {
            isChar = true;
        } else {
            isChar = false;
        }
        return isChar;
    }

    public final static boolean validatePhoneNum(String target) {
        Boolean isChar = null;
        if (target == null)
            isChar = false;
        if (target.matches("\\d{10,11}+")) {
            isChar = true;
        } else {
            isChar = false;
        }
        return isChar;
    }

    public final static boolean validatePassword(String target) {
        Boolean isChar = null;
        if (target == null)
            isChar = false;
        //digit must occur
        //char must occur
        //no whitespace allow
        //atleast 6 place
        if (target.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            isChar = true;
        } else {
            isChar = false;
        }
        return isChar;
    }

    //check is online
    public static Boolean checknetwork(Context mContext) {

        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }

        return true;

    }

}
