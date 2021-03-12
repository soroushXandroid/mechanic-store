package com.soroush.xdtrial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<String> parseOils(String content){

        List<String> list = new ArrayList<>();
        list.add("روغن موتور استفاده شده");
        try {

            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++){

                JSONObject object = array.getJSONObject(i);
                list.add(object.getString("name"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<PersonModel> parsePersons(String content){

        List<PersonModel> list = new ArrayList<>();

        try {

            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++){

                JSONObject object = array.getJSONObject(i);
                PersonModel model = new PersonModel();

                model.setName(object.getString("name"));
                model.setFamily(object.getString("family"));
                model.setPhone(object.getString("phone"));
                model.setDate(object.getString("date"));
                model.setBrand(object.getString("brand"));
                model.setColor(object.getString("color"));
                model.setModel(object.getString("model"));
                model.setGear(object.getString("gear"));
                model.setOil(object.getString("oilName"));
                model.setfPelak(object.getInt("first"));
                model.setlPelak(object.getInt("last"));
                model.setCh(object.getString("char"));
                model.setIranPelak(object.getInt("iran"));

                list.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> parseNumbers(String content){

        List<String> list = new ArrayList<>();
        try {

            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++){

                JSONObject object = array.getJSONObject(i);
                list.add(object.getString("phone"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
