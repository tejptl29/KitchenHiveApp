package com.example.kitchenhive;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Utility {

    public static final String PREF_NAME = "cart_pref";
    public static final String CART_KEY = "cart_items";

//    public static String generateOrderId(String user_id) {
//        return "ORDER_" + System.currentTimeMillis();
//    }

    public boolean checkIfNotEmptySharedpreference(SharedPreferences sharedPreferences, String name, String type)
    {
        switch(type){
            case "boolean":
                return sharedPreferences.contains(name) && sharedPreferences.getBoolean(name, false);
            case "string":
                return sharedPreferences.contains(name) && !sharedPreferences.getString(name, "").trim().isEmpty();
            case "integer":
                return sharedPreferences.contains(name);
            default :
                return false;
        }
    }

    public String getNameInitials(String name){
        String name_initial = "N/A";
        if(!name.trim().isEmpty()) {
            String[] splited = name.split(" ");
            if (splited.length == 2) {
                name_initial = String.valueOf(splited[0].charAt(0));
                name_initial += String.valueOf(splited[1].charAt(0));
            } else if (splited.length == 1) {
                name_initial = String.valueOf(splited[0].charAt(0));
            } else {
                name_initial = String.valueOf(name.charAt(0));
            }
        }
        return name_initial.toUpperCase();
    }

    public boolean checkJSONDataNotNull(JSONObject jsonObject, String key){
        try {
            if(jsonObject.has(key) && !jsonObject.isNull(key) && !jsonObject.getString(key).trim().isEmpty()){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean isValidMobile(String phone) {
        if(Pattern.matches("^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$", phone)) {
            return true;
        }
        return false;
    }

    public boolean isValidEmail(String email) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern) && email.length() > 3)
        {
            // or
            return true;
        }
        else
        {
            //or
            return false;
        }
    }

    public  boolean isvalidpincode(String pincode){
        if (Pattern.matches("([1-9]{1}[0-9]{5}|[1-9]{1}[0-9]{3}\\\\s[0-9]{3})", pincode)) {
            return true;
        }
        return false;
    }

    public int calculateDays(String duration, String interval){
        int totalDays = 0;
        int durationDays = 0;
        if(duration.equals("Yearly")){
            durationDays = 365;
        }
        else if(duration.equals("Quarterly")){
            durationDays = 90;
        }
        else if(duration.equals("Monthly")){
            durationDays = 30;
        }
        if(interval.equals("Every Months")){
            if(durationDays < 30){
                totalDays = 1;
            }
            else{
                totalDays = durationDays / 30;
            }
        }
        else if(interval.equals("Every Day")){
            totalDays = durationDays;
        }
        return totalDays;
    }

    public double calculateFinalAmount(double price, int qty, String duration, String interval){
        int totalDays = calculateDays(duration, interval);
        return ( price * qty ) * totalDays;
    }
}
