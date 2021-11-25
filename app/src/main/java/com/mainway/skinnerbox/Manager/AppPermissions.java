package com.mainway.skinnerbox.Manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class AppPermissions {
    public  static boolean checkAudioPermissions(Context context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }
    public static void  requestAudioPermissions(Activity act){
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.RECORD_AUDIO},23);
    }

    public static boolean checkImagePermissions(Context context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }
    public static void requestImagePermissions(Activity act){
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1032);

    }
}
