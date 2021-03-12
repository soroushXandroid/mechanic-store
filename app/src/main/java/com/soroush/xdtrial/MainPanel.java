package com.soroush.xdtrial;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.soroush.xdtrial.ui.main.ListFragment;
import com.soroush.xdtrial.ui.main.OilFragment;
import com.soroush.xdtrial.ui.main.PersonFragment;
import com.soroush.xdtrial.ui.main.SmsFragment;

public class MainPanel extends AppCompatActivity{

    TabLayout tabLayout;
    Fragment fragment = null;
    FragmentTransaction fTransaction;
    FragmentManager fManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main_panel);
        bindElements();

        tabLayout.getTabAt(3).getIcon().setColorFilter(
                ContextCompat.getColor(getApplicationContext(),
                R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).select();
        initMainFragment();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                switch (tab.getPosition()){

                    case 0:
                        fragment = new ListFragment();
                        break;

                    case 1:
                        fragment = new OilFragment();
                        break;

                    case 2:
                        fragment = new SmsFragment(MainPanel.this);
                        break;

                    case 3:
                        fragment = new PersonFragment(MainPanel.this);
                        break;

                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                assert fragment != null;
                ft.replace(R.id.panel_container, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}});

    }

    private void hideStatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void bindElements(){
        tabLayout = findViewById(R.id.tab_layout);
    }

    private void initMainFragment(){
        fManager = getSupportFragmentManager();
        fTransaction = fManager.beginTransaction();
        fragment = new PersonFragment(this);
        fTransaction.replace(R.id.panel_container, fragment);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fTransaction.commit();
    }


}
