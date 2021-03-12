package com.soroush.xdtrial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fManager;
    private FragmentTransaction fTransaction;

    private int step;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        step = pref.getInt("step", 0);
        initMainFragment(step);
    }

    private void hideStatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initMainFragment(int step){

        fragment = null;

        switch (step){
            case 1:
                Intent intent = new Intent();
                intent.setClass(this, MainPanel.class);
                startActivity(intent);
                break;

            case 0:
                fragment = new SignUpFragment(this);
                fManager = getSupportFragmentManager();
                fTransaction = fManager.beginTransaction();
                fTransaction.replace(R.id.login_container, fragment);
                fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fTransaction.commit();
                break;

        }

    }

}
