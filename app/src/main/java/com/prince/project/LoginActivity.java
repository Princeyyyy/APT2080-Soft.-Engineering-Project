package com.prince.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText logEmail;
    private EditText logPassword;
    private Button logBtn;
    private TextView gotoRegister;
    private FirebaseAuth mAuth;
    private ImageView shownhide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPassword);
        logBtn = findViewById(R.id.btnLogin);
        gotoRegister = findViewById(R.id.gotoRegister);

        mAuth = FirebaseAuth.getInstance();

        //Show/Hide Password
        shownhide = findViewById(R.id.logshow);
        shownhide.setImageResource(R.drawable.ic_show_pwd);
        shownhide.setOnClickListener(view -> {
            if (logPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                //If password is visible the hide it
                logPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //Change Icon
                shownhide.setImageResource(R.drawable.ic_show_pwd);
            } else {
                //Show password
                logPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                //Change Icon
                shownhide.setImageResource(R.drawable.ic_hide_pwd);
            }
        });

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        logBtn.setOnClickListener(v -> {
            String emailString = logEmail.getText().toString();
            String passwordString = logPassword.getText().toString();

            if (TextUtils.isEmpty(emailString)) {
                logEmail.setError("Email is Required");
            }

            if (TextUtils.isEmpty(passwordString)) {
                logPassword.setError("Password is Required");
            } else {
                logBtn.setEnabled(false);
                logBtn.setText("Logging in...");

                mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        logBtn.setText("Log in");
                        logBtn.setEnabled(true);
                    }
                });
            }
        });

        gotoRegister.setOnClickListener(this::onRegistrationClick);
    }

    public void onRegistrationClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }
}