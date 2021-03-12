package com.soroush.xdtrial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout tilCode, tilPhone;
    private TextInputEditText etCode, etPhone;
    private MaterialButton btnSignIn, btnGuidToSignUp;
    private LinearLayout linear;
    private LottieAnimationView lottie;

    private String codeMelli, phone;
    private Context context;

    public SignInFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findElements(view);
        startHideVisibility();
        crossfade();

        btnGuidToSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void findElements(View view){
        tilCode = view.findViewById(R.id.si_code_melli);
        etCode = view.findViewById(R.id.et_si_code_melli);
        etPhone = view.findViewById(R.id.et_phone_si);
        tilPhone = view.findViewById(R.id.si_phone);
        lottie = view.findViewById(R.id.si_lottie_bee_loading);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        btnGuidToSignUp = view.findViewById(R.id.btn_guid_sign_up);
        linear = view.findViewById(R.id.linear_sign_in);
    }

    private void startHideVisibility(){
        tilCode.setVisibility(View.GONE);
        tilPhone.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.GONE);
        linear.setVisibility(View.GONE);
    }

    private void switchToSignUp(){
        Fragment suFragment = new SignUpFragment(context);
        FragmentTransaction transaction = getActivity().
                getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.right_in, R.animator.left_out);
        transaction.replace(R.id.login_container, suFragment);
        transaction.commit();
    }

    private void crossfade() {

        tilCode.setAlpha(0f);
        tilPhone.setAlpha(0f);
        btnSignIn.setAlpha(0f);
        linear.setAlpha(0f);
        tilCode.setTranslationX(150f);
        tilPhone.setTranslationX(150f);
        tilCode.setVisibility(View.VISIBLE);
        tilPhone.setVisibility(View.VISIBLE);
        btnSignIn.setVisibility(View.VISIBLE);
        linear.setVisibility(View.VISIBLE);

        int shortAnimationDuration = 1600;
        tilCode.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 1200)
                .setListener(null);
        tilPhone.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 800)
                .setListener(null);
        btnSignIn.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration - 400)
                .setListener(null);
        linear.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        lottie.playAnimation();
        lottie.animate().alpha(0f).setDuration(shortAnimationDuration - 200)
               .setListener(new AnimatorListenerAdapter() {
                   @Override
                   public void onAnimationEnd(Animator animation) {
                       lottie.setVisibility(View.GONE);
                       lottie.cancelAnimation();}});

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_guid_sign_up:
                switchToSignUp();
                break;

            case R.id.btn_sign_in:

                codeMelli = Objects.requireNonNull
                        (etCode.getText()).toString().trim();
                phone = Objects.requireNonNull
                        (etPhone.getText()).toString().trim();
                ValidateInfo();

                break;

        }

    }

    private void signIn() {
        Toasty.success(context, "وارد حساب خود میشوید", Toasty.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setClass(context, MainPanel.class);
        startActivity(intent);

        SharedPreferences.Editor logEditor = context.
                getSharedPreferences("login", Context.MODE_PRIVATE).edit();
        logEditor.putInt("step", 1);
        logEditor.apply();

        saveMarketerId();
    }

    private void saveMarketerId(){

        StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.GET_MARKETER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.trim();
                        SharedPreferences.Editor editor = context.
                                getSharedPreferences("id", Context.MODE_PRIVATE).edit();
                        editor.putInt("marketer-id", Integer.parseInt(result));
                        editor.apply();
                    }}, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("codemelli", codeMelli);
                params.put("phone", phone);
                return params;
            }};
        App.queue.add(request);

    }

    private void ValidateInfo(){

        if (codeMelli.length() != 10){
            tilCode.setError("کد ملی باید 10 رقم باشد!");
        }
        if (!phone.startsWith("09")){
            tilPhone.setError("شماره همراه باید با 09 شروع شود!");
        }
        if (phone.length() != 11){
            tilPhone.setError("شماره همراه باید 11 رقم باشد!");
        }

        if (!codeMelli.isEmpty() && !phone.isEmpty() &&
                phone.startsWith("09") && phone.length()
                == 11 && codeMelli.length() == 10) {

            tilCode.setError(null);
            tilPhone.setError(null);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    App.BASE_URL + App.CHECK_MARKETER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String result = response.trim();
                            if ("1".equals(result)) {
                                signIn();
                            }
                            else{
                                Toasty.error(context, "کاربری با این مشخصات وجود ندارد",
                                        Toasty.LENGTH_LONG).show();
                            }
                        }
                    }, null) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("codemelli", codeMelli);
                    param.put("phone", phone);
                    return param;
                }
            };
            App.queue.add(request);

        }

    }

}
