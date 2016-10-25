package com.od.hrdf.loginregistration;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.od.hrdf.BOs.User;
import com.od.hrdf.R;
import com.od.hrdf.Utils.Util;
import com.od.hrdf.landingtab.TabbarActivity;

import io.realm.Realm;

public class LoginRegistrationActivity extends AppCompatActivity implements LoginRegActivityInterface {

    FragmentManager fragmentManager;
    private Toolbar toolbar;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        realm = Realm.getDefaultInstance();
        user = User.getCurrentUser(realm);
        if (user != null) {
            gotoMainTabbarActivity();
            finish();
        } else {
            initViews();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment, LoginFragment.newInstance("", ""))
                    .addToBackStack("LoginFragment")
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count == 1) {
            finish();
        } else {
            toolbar.setVisibility(View.GONE);
            fm.popBackStack();
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment, fragment)
                .addToBackStack("LoginFragment")
                .commit();
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onFragmentNav(Fragment fragment, Util.Navigate navigate) {

        switch (navigate) {
            case FORWARD:
                break;
            case BACKWARD:
                break;
            case SIGNUP:
                if (fragment instanceof LoginFragment) {
                    addFragment(RegistrationFragment.newInstance("", ""));
                    toolbar.setVisibility(View.VISIBLE);
                }
                break;
            case LOGIN:
                gotoMainTabbarActivity();
                finish();
                break;
            default:
                break;
        }
    }

    private void gotoMainTabbarActivity() {
        Intent intent = new Intent(this, TabbarActivity.class);
        startActivity(intent);
    }
}
