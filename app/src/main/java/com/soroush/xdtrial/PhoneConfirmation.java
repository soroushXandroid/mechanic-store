package com.soroush.xdtrial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class PhoneConfirmation extends Fragment implements View.OnClickListener, TextWatcher {

    private String name1, family1, code1, brand1, phone, verCode;
    private Context context;

    private LinearLayout linearPhone;
    private AppCompatEditText etInputPhone, etInputCode;
    private MaterialButton mbSubmitPhone, mbSubmitCode;
    private AppCompatTextView tvCaptionCode;
    private LottieAnimationView lottieLoading;

    PhoneConfirmation(Context context){
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findElements(view);
        startHideVisibility();
        name1 = SignUpFragment.name;
        family1 = SignUpFragment.family;
        code1 = SignUpFragment.code;
        brand1 = SignUpFragment.brand;

        etInputPhone.addTextChangedListener(this);
        etInputCode.addTextChangedListener(this);
        mbSubmitPhone.setOnClickListener(this);
        mbSubmitCode.setOnClickListener(this);
    }

    private void findElements(View view) {
        etInputPhone = view.findViewById(R.id.et_input_phone);
        etInputCode = view.findViewById(R.id.et_code_cf);
        mbSubmitPhone = view.findViewById(R.id.mb_conf_phone);
        mbSubmitCode = view.findViewById(R.id.mb_conf_code);
        tvCaptionCode = view.findViewById(R.id.tv_caption_cf);
        linearPhone = view.findViewById(R.id.linear_phone);
        lottieLoading = view.findViewById(R.id.conf_lottie_bee_loading);
    }

    private void startHideVisibility(){
        mbSubmitPhone.setVisibility(View.GONE);
        linearPhone.setVisibility(View.GONE);
        crossfade();
    }

    private void crossfade() {

        linearPhone.setAlpha(0f);
        mbSubmitPhone.setAlpha(0f);

        linearPhone.setTranslationX(150f);
        mbSubmitPhone.setTranslationX(150f);

        linearPhone.setVisibility(View.VISIBLE);
        mbSubmitPhone.setVisibility(View.VISIBLE);

        int shortAnimationDuration = 800;
        linearPhone.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 400)
                .setListener(null);
        mbSubmitPhone.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        lottieLoading.playAnimation();
        lottieLoading.animate().alpha(0f).setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lottieLoading.setVisibility(View.GONE);
                        lottieLoading.cancelAnimation();}});

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_conf, container, false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.mb_conf_phone:
                confPhoneNumber();
                break;

            case R.id.mb_conf_code:
                confCodeNumber();
                break;

        }

    }

    private void confPhoneNumber(){

        phone = etInputPhone.getText().toString();
        String lastTime = System.currentTimeMillis() + "";
        verCode = lastTime.substring(lastTime.length() - 3) + code1.substring(8);
        mbSubmitPhone.setEnabled(false);
        tvCaptionCode.setVisibility(View.VISIBLE);
        etInputCode.setVisibility(View.VISIBLE);
        mbSubmitCode.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.LOGIN_SMS_URL,
                null, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", "0" + phone);
                params.put("code", verCode);
                params.put("name", brand1);
                return params;
            }};
        App.queue.add(request);

    }

    private void confCodeNumber(){

        String typedCode = etInputCode.getText().toString().trim();
        if (verCode.equals(typedCode)){

            StringRequest signReq = new StringRequest(
                    Request.Method.POST,
                    App.BASE_URL + App.SIGNUP_MARKETER_URL,
                    null, null) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name1);
                    params.put("family", family1);
                    params.put("codemelli", code1);
                    params.put("brand", brand1);
                    params.put("phone", "0" + phone);
                    return params;
                }};
            App.queue.add(signReq);

            StringRequest welcomeReq = new StringRequest(
                    Request.Method.POST,
                    App.BASE_URL + App.WELCOME_SMS_URL,
                    null, null) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("phone", "0" + phone);
                    params.put("nameUser", name1);
                    params.put("name", brand1);
                    return params;
                }};
            App.queue.add(welcomeReq);

            signIn();
        }

    }

    private void signIn() {
        Toasty.success(context, "وارد حساب خود میشوید", Toasty.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setClass(context, MainPanel.class);
        context.startActivity(intent);

        SharedPreferences.Editor logEditor = context.
                getSharedPreferences("login", Context.MODE_PRIVATE).edit();
        logEditor.putInt("step", 1);
        logEditor.apply();

        saveMarketerId();
    }

    private void saveMarketerId(){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.LAST_MARKETER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.trim();
                        SharedPreferences.Editor editor = context.
                                getSharedPreferences("id", Context.MODE_PRIVATE).edit();
                        editor.putInt("marketer-id", Integer.parseInt(result));
                        editor.apply();
                    }}, null);
        App.queue.add(request);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (etInputPhone.getText().toString().trim().length() == 10){
            mbSubmitPhone.setEnabled(true);
        }

        if (etInputCode.getText().toString().trim().length() == 5){
            mbSubmitCode.setEnabled(true);
            mbSubmitPhone.setEnabled(false);
        }

    }
    @Override public void afterTextChanged(Editable s) { }

}
