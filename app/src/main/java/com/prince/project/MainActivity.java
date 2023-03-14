package com.prince.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private boolean viewIsAtHome;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.alarm_page);

        displayView(R.id.alarm_page);
    }

    public void displayView(int viewId) {
        Fragment fragment = null;

        switch (viewId) {
            case R.id.info_page:
                fragment = new OrganisationFragment();
                viewIsAtHome = false;
                break;

            case R.id.alarm_page:
                fragment = new AlarmFragment();
                viewIsAtHome = true;
                break;

            case R.id.profile_page:
                fragment = new ProfileFragment();
                viewIsAtHome = false;
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!viewIsAtHome) {
            //if the current view is not the Alarm fragment, display the Alarm fragment
            displayView(R.id.alarm_page);
            navigation.setId(R.id.alarm_page);
        } else {
            //If view is in Alarm fragment, exit application
            moveTaskToBack(true);
        }
    }
}