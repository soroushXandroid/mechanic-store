package com.soroush.xdtrial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout tilName, tilFamily, tilBrand, tilCode;
    private TextInputEditText etName, etFamily, etBrand, etCode;
    private MaterialButton btnSignUp, btnGuidToSignIn;
    private LinearLayout linear;
    private LottieAnimationView lottie;

    public static String name, family, code, brand;
    private Context context;

    public SignUpFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findElements(view);
        startHideVisibility();
        crossfade();

        btnGuidToSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    private void findElements(View view){
        lottie = view.findViewById(R.id.su_lottie_bee_loading);
        tilName = view.findViewById(R.id.input_name);
        tilFamily = view.findViewById(R.id.input_family);
        tilBrand = view.findViewById(R.id.input_brand);
        tilCode = view.findViewById(R.id.input_code_melli);
        etName = view.findViewById(R.id.et_name);
        etFamily = view.findViewById(R.id.et_family);
        etBrand = view.findViewById(R.id.et_brand);
        etCode = view.findViewById(R.id.et_su_code_melli);
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        btnGuidToSignIn = view.findViewById(R.id.btn_guid_sign_in);
        linear = view.findViewById(R.id.linear_sign_up);
    }

    private void startHideVisibility(){
        tilName.setVisibility(View.GONE);
        tilFamily.setVisibility(View.GONE);
        tilCode.setVisibility(View.GONE);
        tilBrand.setVisibility(View.GONE);
        btnSignUp.setVisibility(View.GONE);
        linear.setVisibility(View.GONE);
    }

    private void switchToSignIn(){
        Fragment siFragment = new SignInFragment(context);
        FragmentTransaction transaction = getActivity().
                getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.left_in, R.animator.right_out);
        transaction.replace(R.id.login_container, siFragment);
        transaction.commit();
    }

    private void crossfade() {

        tilCode.setAlpha(0f);
        tilBrand.setAlpha(0f);
        tilName.setAlpha(0f);
        tilFamily.setAlpha(0f);
        btnSignUp.setAlpha(0f);
        linear.setAlpha(0f);

        tilCode.setTranslationX(150f);
        tilBrand.setTranslationX(150f);
        tilName.setTranslationX(150f);
        tilFamily.setTranslationX(150f);

        tilCode.setVisibility(View.VISIBLE);
        tilBrand.setVisibility(View.VISIBLE);
        tilName.setVisibility(View.VISIBLE);
        tilFamily.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.VISIBLE);
        linear.setVisibility(View.VISIBLE);

        int shortAnimationDuration = 2400;
        tilName.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 2000)
                .setListener(null);
        tilFamily.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 1600)
                .setListener(null);
        tilCode.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 1200)
                .setListener(null);
        tilBrand.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(shortAnimationDuration - 800)
                .setListener(null);
        btnSignUp.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration - 400)
                .setListener(null);
        linear.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        lottie.playAnimation();
        lottie.animate().alpha(0f).setDuration(shortAnimationDuration - 400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lottie.setVisibility(View.GONE);
                        lottie.cancelAnimation();}});

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_guid_sign_in:
                switchToSignIn();
                break;

            case R.id.btn_sign_up:

                name = etName.getText().toString().trim();
                family = etFamily.getText().toString().trim();
                code = etCode.getText().toString().trim();
                brand = etBrand.getText().toString().trim();

                if (isValidInputs()) {
                    switchToPhoneConf();
                }

                break;

        }


        }

    private boolean isValidInputs(){

        boolean isValid = false;

        if (name.isEmpty()){
            tilName.setError("فیلد نام نباید خالی باشد!");
        }
        if (family.isEmpty()){
            tilFamily.setError("فیلد فامیل نباید خالی باشد!");
        }
        if (brand.isEmpty()){
            tilBrand.setError("فیلد نام فروشگاه نباید خالی باشد!");
        }
        if (code.length() != 10){
            tilCode.setError("کد ملی باید 10 رقم باشد!");
        }

        if (!name.isEmpty() && !family.isEmpty() && !code.isEmpty()
                && !brand.isEmpty() && code.length() == 10){
            isValid = true;
            tilName.setError(null);
            tilFamily.setError(null);
            tilBrand.setError(null);
            tilCode.setError(null);
        }

        return isValid;
    }

    private void switchToPhoneConf(){
        Fragment pcFragment = new PhoneConfirmation(context);
        FragmentTransaction transaction = getActivity().
                getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.left_in, R.animator.right_out);
        transaction.replace(R.id.login_container, pcFragment);
        transaction.commit();
    }

}
