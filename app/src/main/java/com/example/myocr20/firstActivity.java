package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.InternalTokenResult;

import java.util.Arrays;
import java.util.List;

public class firstActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =123 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

       TextView textView = findViewById(R.id.textView);
       textView.setText("Optical Character recognition is the app which gets text from the given photo");


    }
}