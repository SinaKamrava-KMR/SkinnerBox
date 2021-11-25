package com.mainway.skinnerbox.Access.Restart;

import android.app.Activity;
import android.content.Intent;

public class RestartActivity {
    public static void restartActivity(Activity act){

        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.finish();
    }
}
