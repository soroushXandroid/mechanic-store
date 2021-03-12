package com.soroush.xdtrial.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.soroush.xdtrial.App;
import com.soroush.xdtrial.R;

import java.util.HashMap;
import java.util.Map;

public class OilFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private FloatingActionButton fabOil;
    private CardView cvOil;
    private TextInputEditText etOil;
    private LottieAnimationView addLottie;

    private String step;
    private SharedPreferences pref;

    public OilFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findElements(view);
        step = "normal";
        cvOil.setVisibility(View.GONE);

        fabOil.setOnClickListener(this);
        etOil.addTextChangedListener(this);

        pref = view.getContext().getSharedPreferences("id", Context.MODE_PRIVATE);
    }

    private void findElements(View view){
        fabOil = view.findViewById(R.id.fab_oil);
        cvOil = view.findViewById(R.id.oil_input_card);
        etOil = view.findViewById(R.id.et_add_oil);
        addLottie = view.findViewById(R.id.oil_added_lottie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_oil, container, false);
    }
    @Override
    public void onClick(View v) {

        if (step.equals("normal")){

            crossfade();

        } else if (step.equals("add")){

            String oilName = etOil.getText().toString().trim();
            addOilToList(oilName);
            cvOil.setVisibility(View.INVISIBLE);
            addLottie.setVisibility(View.VISIBLE);
            addLottie.playAnimation();
            addLottie.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    addLottie.setVisibility(View.GONE);
                    addLottie.cancelAnimation();
                    cvOil.setVisibility(View.VISIBLE);
                }});
            etOil.setText("");
            etOil.requestFocus();

        }

    }

    private void addOilToList(final String oilName) {

        final int marketer = pref.getInt("marketer-id", 0);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.SAVE_OIL_URL,
                null, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("marketerId", marketer + "");
                params.put("name", oilName);
                return params;

            }};
        App.queue.add(request);

    }

    private void crossfade() {

        int animDuration = 1000;
        cvOil.setAlpha(0f);
        cvOil.setVisibility(View.VISIBLE);

        cvOil.animate()
                .alpha(1f)
                .setDuration(animDuration)
                .setListener(null);

        fabOil.animate()
                .alpha(0f)
                .setDuration(animDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fabOil.hide(); }});

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (etOil.length() > 0){

            fabOil.setImageResource(R.drawable.ic_add);
            fabOil.setImageTintList(ColorStateList.
                        valueOf(Color.rgb(232, 202, 7)));
            fabOil.show();
            step = "add";

        } else{
            fabOil.hide();
            step = "normal";
        }

    }
    @Override
    public void afterTextChanged(Editable s) { }

}
