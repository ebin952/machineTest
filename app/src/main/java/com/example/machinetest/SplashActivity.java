package com.example.machinetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(() -> {



                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SplashActivity.this);
                    if (acct != null) {

                        startActivity(new Intent(getApplicationContext(),category.class));
                        finish();


                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }



        },1200);
    }
}