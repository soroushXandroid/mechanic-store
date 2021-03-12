package com.soroush.xdtrial;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import com.soroush.xdtrial.ui.main.SmsFragment;

public class MyService extends BroadcastReceiver{

    public static final String TAG = "MyService";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences pref = context.getSharedPreferences("sms", Context.MODE_PRIVATE);
        String msg = pref.getString("msg", "");
        SmsManager sManager = SmsManager.getDefault();
        int length = pref.getInt("length", 0);

        for (int i = length; i > 0; --i){

            String number = pref.getString("n" + i, "");
            if (number != null && !number.isEmpty() && number.startsWith("09")){

                String tel = number.replaceFirst("0", "+98");
                sManager.sendTextMessage(tel, null, msg, null, null);

            }

        }

        if (SmsFragment.manager != null)
            SmsFragment.manager.cancel(SmsFragment.pendingIntent);

        SmsFragment.sendingLottie.cancelAnimation();
        SmsFragment.sendingLottie.setVisibility(View.GONE);
        SmsFragment.fabStop.hide();

        SharedPreferences.Editor editor = context.
                getSharedPreferences("sms", Context.MODE_PRIVATE).edit();
        editor.putInt("check", 0);
        editor.apply();

    }

}