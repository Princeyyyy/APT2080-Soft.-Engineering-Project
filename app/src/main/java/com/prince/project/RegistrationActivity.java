package com.prince.project;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prince.project.models.User;

public class RegistrationActivity extends AppCompatActivity {

    private TextView gotoLogin;
    private EditText regFname;
    private EditText regLname;
    private EditText regEmail;
    private EditText regPassword;
    private Button regBtn;
    private FirebaseAuth mAuth;

    private ImageView shownhide;
    private DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regFname = findViewById(R.id.regFname);
        regLname = findViewById(R.id.regLname);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.btnReg);
        gotoLogin = findViewById(R.id.gotoLogin);

        mAuth = FirebaseAuth.getInstance();

        //Show/Hide Password
        shownhide = findViewById(R.id.regshow);
        shownhide.setImageResource(R.drawable.ic_show_pwd);
        shownhide.setOnClickListener(view -> {
            if (regPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                //If password is visible the hide it
                regPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //Change Icon
                shownhide.setImageResource(R.drawable.ic_show_pwd);
            } else {
                //Show password
                regPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                //Change Icon
                shownhide.setImageResource(R.drawable.ic_hide_pwd);
            }
        });

        regBtn.setOnClickListener(view -> {
            String fnameString = regFname.getText().toString();
            String lnameString = regLname.getText().toString();
            String emailString = regEmail.getText().toString();
            String passwordString = regPassword.getText().toString();

            if (TextUtils.isEmpty(fnameString)) {
                regLname.setError("First Name is Required");
            }

            if (TextUtils.isEmpty(lnameString)) {
                regLname.setError("Last Name is Required");
            }

            if (TextUtils.isEmpty(emailString)) {
                regEmail.setError("Email is Required");
            }

            if (TextUtils.isEmpty(passwordString)) {
                regPassword.setError("Password is Required");
            } else {
                regBtn.setEnabled(false);
                regBtn.setText("Creating your Account...");

                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = mAuth.getCurrentUser().getUid();
                        User user = new User(id, fnameString, lnameString, emailString);

                        users = FirebaseDatabase.getInstance().getReference().child("user-details").child(mAuth.getCurrentUser().getUid());

                        users.setValue(user).addOnCompleteListener(task1 -> {
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        regBtn.setText("Create Account");
                        regBtn.setEnabled(true);
                    }
                });
            }


        });

        gotoLogin.setOnClickListener(this::onLoginClick);
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }
}