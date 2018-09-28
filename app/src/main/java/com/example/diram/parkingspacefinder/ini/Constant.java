package com.example.diram.parkingspacefinder.ini;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.diram.parkingspacefinder.models.Profile;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Constant {
    public static Profile getUserProfile(Context context) {

        SharedPreferences prof = context.getSharedPreferences("parking", Context.MODE_PRIVATE);
        String storage = prof.getString("profile", nullProfile());

        Profile profile = new Profile();
        try {
            JSONObject pro = new JSONObject(storage);
            profile.setEmail(pro.getString("email"));
            profile.setIconPath(pro.getString("iconPath"));
            profile.setName(pro.getString("fullName"));
            profile.setMobile(pro.getString("mobileNumber"));
            profile.setLoggedIn(pro.getBoolean("loggedIn"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profile;
    }

    private static String nullProfile() {
        try {
            JSONObject profileNull = new JSONObject();
            profileNull.put("loggedIn", false);
            profileNull.put("email", "");
            profileNull.put("iconPath", "");
            profileNull.put("name", "");
            profileNull.put("mobile", "");
            return profileNull.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setUserProfile(Context context, JSONObject profile) {
        try {
            profile.accumulate("loggedIn", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = context.getSharedPreferences("parking", Context.MODE_PRIVATE).edit();
        editor.putString("profile", profile.toString());
        editor.apply();
    }
}
