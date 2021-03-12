package com.soroush.xdtrial.ui.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.soroush.xdtrial.App;
import com.soroush.xdtrial.JsonParser;
import com.soroush.xdtrial.MyService;
import com.soroush.xdtrial.R;
import com.travijuu.numberpicker.library.NumberPicker;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;

public class SmsFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private LinearLayout linearCdv;
    private ConstraintLayout contentLayout;
    private FloatingActionButton fabSms;
    public static ExtendedFloatingActionButton fabStop;
    private TextInputEditText etSms;
    private AppCompatTextView tvInfoDays;
    private NumberPicker picker;
    private CountdownView cdView;
    public static LottieAnimationView sendingLottie;

    private Context context;
    private String step;
    private SharedPreferences pref, prefID;
    public static PendingIntent pendingIntent;
    public static AlarmManager manager;

    private int mID;

    public SmsFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findElements(view);

        if (context.checkSelfPermission(Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){

            fabSms.setEnabled(false);
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 123);

        }

        step = "normal";
        pref = context.getSharedPreferences("sms", Context.MODE_PRIVATE);
        prefID = context.getSharedPreferences("id", Context.MODE_PRIVATE);
        mID = prefID.getInt("marketer-id", 0);

        fabSms.setOnClickListener(this);
        fabStop.setOnClickListener(this);
        etSms.addTextChangedListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123 && grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
            fabSms.setEnabled(true);
        }

        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toasty.warning(context, "برای دسترسی به این قسمت باید اجازه داده شود").show();
            }


    }

    @Override
    public void onStart() {
        super.onStart();

        int check = pref.getInt("check", 0);
        if (check == 1){
            waitingForeground();
        } else {
            step = "normal";
        }

    }

    private void setSmsAlarm(int days, String msg){

        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, MyService.class);
        saveNumbersToSend();
        pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        long trigger = days * 24 * 60 * 60 * 1000;
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + trigger, pendingIntent);

        SharedPreferences.Editor editor = context.
                getSharedPreferences("sms", Context.MODE_PRIVATE).edit();
        editor.putInt("check", 1);
        editor.putString("msg", msg);
        editor.apply();
        waitingForeground();
    }

    private void findElements(View view){
        cdView = view.findViewById(R.id.countview_sms);
        linearCdv = view.findViewById(R.id.sms_countdown_layout);
        contentLayout = view.findViewById(R.id.sms_content_layout);
        fabSms = view.findViewById(R.id.fab_sms);
        fabStop = view.findViewById(R.id.fab_stop_sending);
        etSms = view.findViewById(R.id.et_sms_field);
        picker = view.findViewById(R.id.number_picker_sms);
        tvInfoDays = view.findViewById(R.id.tv_info_days);
        sendingLottie = view.findViewById(R.id.sending_lottie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    private void saveNumbersToSend(){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.GET_NUMBERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<String> numbers = JsonParser.parseNumbers(response);
                        SharedPreferences.Editor editor = context.getSharedPreferences
                                ("sms", Context.MODE_PRIVATE).edit();
                        int i = 0;
                        for (String number : numbers){
                            Log.i("TAG", "onResponse: " + number);
                            ++i;
                            editor.putString("n" + i, number);
                        }
                        editor.putInt("length", i);
                        editor.apply();

                    }}, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mid", mID + "");
                return map;
            }};
        App.queue.add(request);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab_sms:
                fabSmsClick();
                break;

            case R.id.fab_stop_sending:
                fabStopClick();
                break;

        }

    }

    private void fabSmsClick() {

        if (step.equals("normal")){

            crossfade();

        } else if (step.equals("send")){

            int days = picker.getValue();
            String msg = etSms.getText().toString().trim();
            setSmsAlarm(days, msg);

        }

    }

    private void crossfade() {

        int animDuration = 1000;
        contentLayout.setAlpha(0f);
        picker.setAlpha(0f);
        tvInfoDays.setAlpha(0f);
        contentLayout.setVisibility(View.VISIBLE);
        picker.setVisibility(View.VISIBLE);
        tvInfoDays.setVisibility(View.VISIBLE);

        contentLayout.animate()
                .alpha(1f)
                .setDuration(animDuration)
                .setListener(null);

        picker.animate()
                .alpha(1f)
                .setDuration(animDuration)
                .setListener(null);

        tvInfoDays.animate()
                .alpha(1f)
                .setDuration(animDuration)
                .setListener(null);

        fabSms.animate()
                .alpha(0f)
                .setDuration(animDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fabSms.hide(); }});

    }

    private void fabStopClick(){

        sendingLottie.setVisibility(View.GONE);
        sendingLottie.cancelAnimation();
        fabStop.hide();
        contentLayout.setVisibility(View.VISIBLE);
        picker.setVisibility(View.VISIBLE);
        tvInfoDays.setVisibility(View.VISIBLE);
        fabSms.show();
        SharedPreferences.Editor editor = context.
                getSharedPreferences("sms", Context.MODE_PRIVATE).edit();
        editor.putInt("check", 0);
        editor.apply();
        if (manager != null){
            manager.cancel(pendingIntent);
        }

    }

    private void waitingForeground(){
        contentLayout.setVisibility(View.GONE);
        picker.setVisibility(View.GONE);
        tvInfoDays.setVisibility(View.GONE);
        fabSms.hide();
        fabStop.show();
        sendingLottie.setVisibility(View.VISIBLE);
        sendingLottie.playAnimation();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0){
            fabSms.setImageResource(R.drawable.ic_send_new);
            fabSms.setImageTintList(ColorStateList.valueOf
                    (Color.rgb(52, 126, 240)));
            fabSms.show();
            step = "send";
        } else{
            fabSms.hide();
            step = "normal";
        }

    }
    @Override
    public void afterTextChanged(Editable s) {}

}
