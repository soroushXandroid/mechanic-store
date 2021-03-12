package com.soroush.xdtrial.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.soroush.xdtrial.App;
import com.soroush.xdtrial.JsonParser;
import com.soroush.xdtrial.PersonListAdapter;
import com.soroush.xdtrial.PersonModel;
import com.soroush.xdtrial.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment {

    private RecyclerView rcPerson;
    private PersonListAdapter adapter;
    private List<PersonModel> list;
    private SearchView searchPerson;
    private LottieAnimationView lottiePage;
    private SharedPreferences pref;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findElements(view);
        prepareToShow();

        pref = view.getContext().getSharedPreferences("id", Context.MODE_PRIVATE);
        final int mID = pref.getInt("marketer-id", 0);

        rcPerson.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcPerson.setHasFixedSize(true);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                App.BASE_URL + App.GET_PERSONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        list = JsonParser.parsePersons(response);
                        adapter = new PersonListAdapter(list);
                        rcPerson.setAdapter(adapter);

                    }}, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("mid", mID + "");
                return param;
            }};
        App.queue.add(request);

    }

    @Override
    public void onStart() {
        super.onStart();

        searchPerson.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                List<PersonModel> newList = adapter.showSearchList(newText);
                if (newList.isEmpty() || newText.isEmpty()){
                    adapter = new PersonListAdapter(list);
                } else {
                    adapter = new PersonListAdapter(newList);
                }
                rcPerson.setAdapter(adapter);

                return false;
            }});

    }

    private void prepareToShow() {

        rcPerson.setVisibility(View.GONE);

        /*rcPerson.setAlpha(0f);
        rcPerson.setVisibility(View.VISIBLE);

        rcPerson.animate().alpha(1f).setDuration(2000).setListener(null);*/

        lottiePage.setVisibility(View.VISIBLE);
        lottiePage.playAnimation();
        lottiePage.animate().alpha(0.5f).setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lottiePage.setVisibility(View.GONE);
                        rcPerson.setVisibility(View.VISIBLE);
                        lottiePage.cancelAnimation(); }});

    }

    private void findElements(View view) {
        rcPerson = view.findViewById(R.id.recycler_person_list);
        lottiePage = view.findViewById(R.id.lottie_page_loading);
        searchPerson = view.findViewById(R.id.searchview_person);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable 
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    
}
