package com.mainway.skinnerbox.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {
    private SharedPreferences sharedPreferences;


    public AppSharedPreference(Context context){
        sharedPreferences=context.getSharedPreferences("app_information",Context.MODE_PRIVATE);
    }

    public void setAppLanguage(String language){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("app_language",language);
        editor.commit();
    }

    public String getAppLanguage(){
        return sharedPreferences.getString("app_language","en");
    }

    public void setLastId(int id){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("lastId",id);
        editor.commit();
    }
    public int getLastId(){
        return sharedPreferences.getInt("lastId",0);
    }

    public void setAudioFPath(String path){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("audioFront",path);
        editor.commit();
    }
    public String getAudioFPath(){
        return sharedPreferences.getString("audioBack","");
    }

    public void setAudioBPath(String path){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("audioFront",path);
        editor.commit();
    }
    public String getAudioBPath(){
        return sharedPreferences.getString("audioBack","");
    }

    public void setPicFront(String path){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("picFront",path);
        editor.commit();
    }
    public void setPicBack(String path){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("picBack",path);
        editor.commit();
    }

    public String getPicFront(){
        return sharedPreferences.getString("picFront","");
    }
    public String getPicBack(){
        return sharedPreferences.getString("picBack","");
    }

}
