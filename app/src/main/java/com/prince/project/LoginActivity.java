package com.prince.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

        // Check if notifications are already enabled
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Notifications are not enabled, show a dialog to ask for permission
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Allow Notifications");
            builder.setMessage("This app would like to send you notifications. Do you want to allow notifications?");

            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                // Open system settings to allow notifications
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationChannel.DEFAULT_CHANNEL_ID);
                startActivity(intent);
            });
            builder.setNegativeButton(android.R.string.no, null);
            builder.show();
        }

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

            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
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