package com.soroush.xdtrial;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import saman.zamani.persiandate.PersianDate;

class ListViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView tvName, tvPhone, tvCar,
            tvOil, tvfPelak, tvlPelak, tviPelak, chPelak, tvDate;
    private AppCompatImageView ivAdd;

    private String name, family, phone,
            brand, cModel, color,
            gear, first, last, ch,
            iran, oil, date;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name_person);
        tvPhone = itemView.findViewById(R.id.tv_phone_person);
        tvCar = itemView.findViewById(R.id.tv_car_person);
        tvOil = itemView.findViewById(R.id.tv_oil_person);
        tvfPelak = itemView.findViewById(R.id.tv_first_pelak);
        chPelak = itemView.findViewById(R.id.tv_char_pelak);
        tvlPelak = itemView.findViewById(R.id.tv_last_pelak);
        tviPelak = itemView.findViewById(R.id.tv_iran_pelak);
        ivAdd = itemView.findViewById(R.id.iv_circle_add);
        tvDate = itemView.findViewById(R.id.tv_person_date);
    }

    void initViews(PersonModel model){

        name = model.getName();
        family = model.getFamily();
        phone = model.getPhone();
        date = model.getDate();
        brand = model.getBrand();
        cModel = model.getModel();
        color = model.getColor();
        gear = model.getGear();
        first = model.getfPelak() + "";
        last = model.getlPelak() + "";
        ch = model.getCh();
        iran = model.getIranPelak() + "";
        oil = model.getOil();

        tvName.setText("    " + name + " " + family);
        tvPhone.setText(phone + "    ");
        tvDate.setText(date);
        tvCar.setText("    " + brand + " " + cModel + " " + color + " " + gear);
        tvfPelak.setText(first);
        tvlPelak.setText(last);
        tviPelak.setText(iran);
        chPelak.setText(ch);
        tvOil.setText("    " + oil);

        ivAdd.setColorFilter(R.color.colorPrimary,android.graphics.PorterDuff.Mode.MULTIPLY);
        final RotateAnimation rotate = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivAdd.startAnimation(rotate);
                SharedPreferences pref = v.getContext().getSharedPreferences("id", Context.MODE_PRIVATE);
                int mid = pref.getInt("marketer-id", 0);
                updateAccount(mid);

            }});

    }

    private void updateAccount(final int mId) {

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
                params.put("model", cModel);
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
