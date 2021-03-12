package com.soroush.xdtrial;

import android.app.Application;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import es.dmoral.toasty.Toasty;


public class App extends Application {

    public static final String BASE_URL = "http://192.168.43.136//Mechanic/";
    public static final String SAVE_PELAK_URL = "SignUpPelak.php";
    public static final String SIGNUP_MARKETER_URL = "SignUpMarketer.php";
    public static final String SAVE_PERSONAL_URL = "SignUpPersonal.php";
    public static final String SAVE_CAR_URL = "SignUpCar.php";
    public static final String USED_OIL_URL = "OilUsed.php";
    public static final String SAVE_OIL_URL = "SaveOils.php";
    public static final String GET_OILS_URL = "GetOils.php";
    public static final String CHECK_MARKETER_URL = "CheckSignIn.php";
    public static final String GET_MARKETER_URL = "GetMarketerId.php";
    public static final String LAST_MARKETER_URL = "GetLastMarketer.php";
    public static final String GET_PERSONAL_URL = "GetLastId.php";
    public static final String GET_PERSONS_URL = "PersonList.php";
    public static final String GET_NUMBERS_URL = "PhoneNumbers.php";
    public static final String LOGIN_SMS_URL = "Login.php";
    public static final String WELCOME_SMS_URL = "Welcome.php";

    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);

        Typeface tf = ResourcesCompat.getFont(this, R.font.behdad);
        assert tf != null;
        Toasty.Config.getInstance()
                .setTextSize(12).setToastTypeface(tf)
                .allowQueue(true).apply();
    }





}
