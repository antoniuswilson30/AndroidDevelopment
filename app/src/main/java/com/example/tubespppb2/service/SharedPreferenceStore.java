package com.example.tubespppb2.service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceStore {
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USERID = "userId";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private SharedPreferences sp;
    public SharedPreferenceStore(Context context){
        this.sp = context.getSharedPreferences("PrefToken",context.MODE_PRIVATE);
    }

    public String getToken(){
        String token = sp.getString(SharedPreferenceStore.KEY_TOKEN,"");
        return token;
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceStore.KEY_TOKEN,token);
        editor.apply();
    }

    public void setUserId(String userId){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceStore.KEY_USERID,userId);
        editor.apply();
    }

    public String getUserId(){
        String userId = sp.getString(SharedPreferenceStore.KEY_USERID,"");
        return userId;
    }

    public void setNama(String nama){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceStore.KEY_NAMA,nama);
        editor.apply();
    }

    public String getNama(){
        String nama = sp.getString(SharedPreferenceStore.KEY_NAMA,"");
        return nama;
    }

    public void setEmail(String email){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceStore.KEY_EMAIL,email);
        editor.apply();
    }

    public String getEmail(){
        String email = sp.getString(SharedPreferenceStore.KEY_EMAIL,"");
        return email;
    }

    public void setRole(String role){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceStore.KEY_ROLE,role);
        editor.apply();
    }

    public String getRole(){
        String role = sp.getString(KEY_ROLE,"");
        return role;
    }
}
