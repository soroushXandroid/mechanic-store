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
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.soroush.xdtrial.App;
import com.soroush.xdtrial.JsonParser;
import com.soroush.xdtrial.R;
import com.zigis.materialtextfield.MaterialTextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saman.zamani.persiandate.PersianDate;


public class PersonFragment extends Fragment implements TextWatcher, View.OnClickListener {

    private FloatingActionButton personFab;
    private MaterialTextField mtfName, mtfFamily, mtfColor, mtfPhone,
            mtfFirstPelak, mtfLastPelak, mtfIranPelak, mtfBrand, mtfModel;
    private MaterialSpinner spCharPelak, spGear, spOil;
    private LottieAnimationView confirmLottie;
    private MaterialButton mbPersonal, mbCar, mbPelak;

    private final List<String> gearList = Arrays.asList("نوع دنده", "دستی", "اتوماتیک");
    private final List<String> charList = Arrays.asList("حرف پلاک", "آ", "ب", "پ", "ت", "ث", "ج",
            "چ", "ح", "خ", "د", "ذ", "ر", "ز", "ژ", "س", "ش", "ص", "ض", "ط", "ظ", "ع",
            "غ", "ف", "ق", "ک", "گ", "ل", "م", "ن", "و", "ه", "ی");
    private String step;
    private String name, family, phone, brand, model, color, gear, first, last, ch, iran, oil;
    private int marketer_id;

    private Context context;
    private SharedPreferences pref;
    private List<String> oilList = new ArrayList<>();

    public PersonFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        step = "normal";
        findElements(view);

        spGear.setItems(gearList);
        spCharPelak.setItems(charList);
        spCharPelak.setSelectedIndex(0);
        spGear.setSelectedIndex(0);

        personFab.setOnClickListener(this);
        mbPersonal.setOnClickListener(this);
        mbCar.setOnClickListener(this);
        mbPelak.setOnClickListener(this);
        mtfName.addTextChangedListener(this);
        mtfFamily.addTextChangedListener(this);
        mtfPhone.addTextChangedListener(this);

        pref = context.getSharedPreferences("id", Context.MODE_PRIVATE);

    }

    @Override
    public void onResume() {
        super.onResume();

        StringRequest request = new StringRequest(
                App.BASE_URL + App.GET_OILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        oilList = JsonParser.parseOils(response);
                        spOil.setItems(oilList);

                    }}, null);
        App.queue.add(request);

    }

    private void confirmation() {

        if (phone.startsWith("09") && phone.length() == 11){

            marketer_id = pref.getInt("marketer-id", 0);
            saveCustomer(marketer_id);
            mtfPhone.setError(null);
            step = "normal";
            removeContents();
            confirmLottie.setVisibility(View.VISIBLE);
            confirmLottie.playAnimation();
            confirmLottie.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    confirmLottie.setVisibility(View.GONE);
                    confirmLottie.cancelAnimation();
                }});

        } else if (!phone.startsWith("09") && phone.length() == 13){

            mtfPhone.setError("شماره موبایل باید با 09 شروع شود!");

        } else if (phone.startsWith("09") && phone.length() < 13){

            mtfPhone.setError("شماره موبایل باید 11 رقم باشد!");

        }

    }

    private void getPersonalId(){

        /*StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.GET_PERSONAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        int id = 0;
                        if (!response.trim().isEmpty())
                            id = Integer.parseInt(response.trim());
                        saveCustomer(marketer_id, ++id);

                    }}, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("marketerId", marketer_id + "");
                return params; }};
        App.queue.add(request);*/

    }

    private void findElements(View view){
        personFab = view.findViewById(R.id.fab_person);
        mbPersonal = view.findViewById(R.id.mb_personal);
        mbCar = view.findViewById(R.id.mb_car);
        mbPelak = view.findViewById(R.id.mb_pelak);
        mtfPhone = view.findViewById(R.id.mtf_personal_phone);
        mtfName = view.findViewById(R.id.mtf_personal_name);
        mtfFamily = view.findViewById(R.id.mtf_personal_family);
        mtfColor = view.findViewById(R.id.mtf_car_color);
        mtfBrand = view.findViewById(R.id.mtf_car_brand);
        mtfModel = view.findViewById(R.id.mtf_car_model);
        mtfFirstPelak = view.findViewById(R.id.mtf_pelak_first);
        mtfLastPelak = view.findViewById(R.id.mtf_pelak_last);
        mtfIranPelak = view.findViewById(R.id.mtf_pelak_iran);
        spCharPelak = view.findViewById(R.id.spinner_pelak_char);
        spGear = view.findViewById(R.id.spinner_car_gear);
        spOil = view.findViewById(R.id.spinner_oil);
        confirmLottie = view.findViewById(R.id.confirmation_lottie);
    }

    private void removeContents(){
        mtfName.setText("");
        mtfFamily.setText("");
        mtfModel.setText("");
        mtfColor.setText("");
        mtfBrand.setText("");
        mtfPhone.setText("");
        mtfIranPelak.setText("");
        mtfFirstPelak.setText("");
        mtfLastPelak.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person, container, false);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mtfPhone.length() == 11 && mtfName.length() > 0 && mtfFamily.length() > 0
            && mtfPhone.getText().toString().trim().startsWith("09")){
            personFab.setImageResource(R.drawable.ic_check_new);
            personFab.setImageTintList(ColorStateList.valueOf
                    (Color.rgb(55, 150, 28)));
            personFab.show();
            step = "confirm";
        } else{
            personFab.hide();
            step = "normal";
        }

    }
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void personalContentShow(boolean show){

        if (show){

            mtfName.setAlpha(0f);
            mtfFamily.setAlpha(0f);
            mtfPhone.setAlpha(0f);
            mtfName.setVisibility(View.VISIBLE);
            mtfFamily.setVisibility(View.VISIBLE);
            mtfPhone.setVisibility(View.VISIBLE);

            mtfName.animate().alpha(1f).setDuration(600).setListener(null);
            mtfFamily.animate().alpha(1f).setDuration(800).setListener(null);
            mtfPhone.animate().alpha(1f).setDuration(1000).setListener(null);

        } else {

            if (mtfName.getVisibility() == View.VISIBLE){

                mtfName.setVisibility(View.GONE);
                mtfFamily.setVisibility(View.GONE);
                mtfPhone.setVisibility(View.GONE);

            }

        }

    }

    private void carContentShow(boolean show){

        if (show){

            mtfBrand.setAlpha(0f);
            mtfModel.setAlpha(0f);
            mtfColor.setAlpha(0f);
            spGear.setAlpha(0f);
            mtfBrand.setVisibility(View.VISIBLE);
            mtfModel.setVisibility(View.VISIBLE);
            mtfColor.setVisibility(View.VISIBLE);
            spGear.setVisibility(View.VISIBLE);

            mtfBrand.animate().alpha(1f).setDuration(400).setListener(null);
            mtfModel.animate().alpha(1f).setDuration(600).setListener(null);
            mtfColor.animate().alpha(1f).setDuration(800).setListener(null);
            spGear.animate().alpha(1f).setDuration(1000).setListener(null);

        } else {

            if (mtfBrand.getVisibility() == View.VISIBLE){

                mtfBrand.setVisibility(View.GONE);
                mtfModel.setVisibility(View.GONE);
                mtfColor.setVisibility(View.GONE);
                spGear.setVisibility(View.GONE);

            }

        }

    }

    private void pelakContentShow(boolean show){

        if (show){

            mtfFirstPelak.setAlpha(0f);
            mtfLastPelak.setAlpha(0f);
            mtfIranPelak.setAlpha(0f);
            spCharPelak.setAlpha(0f);
            mtfFirstPelak.setVisibility(View.VISIBLE);
            mtfLastPelak.setVisibility(View.VISIBLE);
            mtfIranPelak.setVisibility(View.VISIBLE);
            spCharPelak.setVisibility(View.VISIBLE);

            mtfFirstPelak.animate().alpha(1f).setDuration(400).setListener(null);
            mtfLastPelak.animate().alpha(1f).setDuration(600).setListener(null);
            mtfIranPelak.animate().alpha(1f).setDuration(800).setListener(null);
            spCharPelak.animate().alpha(1f).setDuration(1000).setListener(null);

        } else {

            if (mtfFirstPelak.getVisibility() == View.VISIBLE){

                mtfFirstPelak.setVisibility(View.GONE);
                mtfLastPelak.setVisibility(View.GONE);
                mtfIranPelak.setVisibility(View.GONE);
                spCharPelak.setVisibility(View.GONE);

            }

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab_person:

                if(step.equals("confirm")){

                    name = mtfName.getText().toString().trim();
                    family = mtfFamily.getText().toString().trim();
                    phone = mtfPhone.getText().toString().trim();
                    brand = mtfBrand.getText().toString().trim();
                    model = mtfModel.getText().toString().trim();
                    color = mtfColor.getText().toString().trim();

                    if (spGear.getSelectedIndex() == 0)
                        gear = "";
                    else
                        gear = spGear.getText().toString().trim();

                    first = mtfFirstPelak.getText().toString().trim();
                    last = mtfLastPelak.getText().toString().trim();
                    iran = mtfIranPelak.getText().toString().trim();

                    if (spCharPelak.getSelectedIndex() == 0)
                        ch = "";
                    else
                        ch = spCharPelak.getText().toString().trim();

                    if (spOil.getSelectedIndex() == 0)
                        oil = "";
                    else
                        oil = spOil.getText().toString().trim();

                    confirmation();

                }
                break;

            case R.id.mb_personal:
                if (mtfName.getVisibility() != View.VISIBLE) {
                    personalContentShow(true);
                    carContentShow(false);
                    pelakContentShow(false);
                } else {
                    personalContentShow(false);
                }
                break;

            case R.id.mb_car:
                if (mtfBrand.getVisibility() != View.VISIBLE) {
                    personalContentShow(false);
                    carContentShow(true);
                    pelakContentShow(false);
                } else {
                    carContentShow(false);
                }
                break;

            case R.id.mb_pelak:
                if (mtfFirstPelak.getVisibility() != View.VISIBLE) {
                    personalContentShow(false);
                    carContentShow(false);
                    pelakContentShow(true);
                } else {
                    pelakContentShow(false);
                }
                break;

        }

    }

    private void saveCustomer(final int mId) {

        PersianDate pDate = new PersianDate();
        final String pdFormat = pDate.getShYear() + "/" +
                pDate.getShMonth() + "/" + pDate.getShDay();

        StringRequest pRequest = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.SAVE_PERSONAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int pid = Integer.parseInt(response.trim());
                        saveOtherPart(mId, pid);
                    }}, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("marketerId", mId + "");
                params.put("name", name);
                params.put("family", family);
                params.put("phone", phone);
                params.put("date", pdFormat);
                return params;

            }};
        App.queue.add(pRequest);

    }

    private void saveOtherPart(final int mId, final int pId){
        StringRequest cRequest = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.SAVE_CAR_URL,
                null, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("marketerId", mId + "");
                params.put("personalId", pId + "");
                params.put("brand", brand);
                params.put("model", model);
                params.put("color", color);
                params.put("gear", gear);
                return params;

            }};
        App.queue.add(cRequest);

        StringRequest peRequest = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.SAVE_PELAK_URL,
                null, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("marketerId", mId + "");
                params.put("personalId", pId + "");
                params.put("first", first.isEmpty() ? 0 + "" : first + "");
                params.put("char", ch);
                params.put("last", last.isEmpty() ? 0 + "" : last + "");
                params.put("iran", iran.isEmpty() ? 0 + "" : iran + "");
                return params;

            }};
        App.queue.add(peRequest);

        StringRequest oilRequest = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.USED_OIL_URL,
                null, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("marketerId", mId + "");
                params.put("personalId", pId + "");
                params.put("name", oil);
                return params;

            }};
        App.queue.add(oilRequest);
    }

}
