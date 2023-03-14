package com.prince.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prince.project.models.Organization;
import com.prince.project.models.User;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    //Individual
    private EditText regFname;
    private EditText regLname;
    private EditText regEmail;
    private EditText regPassword;
    private ImageView shownhide;
    private TextView gotoLogin;
    private TextView textView7;

    //Organisation
    private TextView orggotoLogin;
    private TextView orgtextView7;
    private EditText orgName;
    private EditText orgEmail;
    private EditText orgPassword;
    private Button regBtn;
    private Button orgBtn;
    private ImageView orgshownhide;
    private Spinner spinner;
    private FirebaseAuth mAuth;
    private DatabaseReference users, reference;
    private RadioGroup radioGroup;
    private String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("organization-details");

        //Spinner
        spinner = findViewById(R.id.spinner);
        refreshSpinner();

        //Organisation
        orgName = findViewById(R.id.orgName);
        orgEmail = findViewById(R.id.orgEmail);
        orgPassword = findViewById(R.id.orgPassword);
        orgBtn = findViewById(R.id.btnOrg);
        orggotoLogin = findViewById(R.id.orggotoLogin);
        orgtextView7 = findViewById(R.id.orgtextView7);
        orgshownhide = findViewById(R.id.orgshownhide);

        //Individual
        regFname = findViewById(R.id.regFname);
        regLname = findViewById(R.id.regLname);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.btnReg);
        gotoLogin = findViewById(R.id.gotoLogin);
        textView7 = findViewById(R.id.textView7);
        shownhide = findViewById(R.id.regshow);

        radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton checkedRadioButton = findViewById(checkedId);
            String checkedRadioButtonText = checkedRadioButton.getText().toString();
            if (checkedRadioButtonText.equals("Individual")) {
                // Execute code for Individual
                orgName.setVisibility(View.GONE);
                orgEmail.setVisibility(View.GONE);
                orgPassword.setVisibility(View.GONE);
                orgBtn.setVisibility(View.GONE);
                orgtextView7.setVisibility(View.GONE);
                orggotoLogin.setVisibility(View.GONE);
                orgshownhide.setVisibility(View.GONE);
                //------------------------------------
                regFname.setVisibility(View.VISIBLE);
                regLname.setVisibility(View.VISIBLE);
                regEmail.setVisibility(View.VISIBLE);
                regPassword.setVisibility(View.VISIBLE);
                regBtn.setVisibility(View.VISIBLE);
                textView7.setVisibility(View.VISIBLE);
                gotoLogin.setVisibility(View.VISIBLE);
                shownhide.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                refreshSpinner();
            } else if (checkedRadioButtonText.equals("Organization")) {
                // Execute code for Organisation
                regFname.setVisibility(View.GONE);
                regLname.setVisibility(View.GONE);
                regEmail.setVisibility(View.GONE);
                regPassword.setVisibility(View.GONE);
                regBtn.setVisibility(View.GONE);
                textView7.setVisibility(View.GONE);
                gotoLogin.setVisibility(View.GONE);
                shownhide.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                //------------------------------------
                orgName.setVisibility(View.VISIBLE);
                orgEmail.setVisibility(View.VISIBLE);
                orgPassword.setVisibility(View.VISIBLE);
                orgBtn.setVisibility(View.VISIBLE);
                orgtextView7.setVisibility(View.VISIBLE);
                orggotoLogin.setVisibility(View.VISIBLE);
                orgshownhide.setVisibility(View.VISIBLE);
            }
        });

        //Show/Hide Password for Organisation
        orgshownhide.setImageResource(R.drawable.ic_show_pwd);
        orgshownhide.setOnClickListener(view -> {
            if (orgPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                //If password is visible the hide it
                orgPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //Change Icon
                orgshownhide.setImageResource(R.drawable.ic_show_pwd);
            } else {
                //Show password
                orgPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                //Change Icon
                orgshownhide.setImageResource(R.drawable.ic_hide_pwd);
            }
        });

        //Show/Hide Password for Individual
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

        //Individual Registration
        regBtn.setOnClickListener(view -> {
            String fnameString = regFname.getText().toString();
            String lnameString = regLname.getText().toString();
            String emailString = regEmail.getText().toString();
            String passwordString = regPassword.getText().toString();

            String selectedOrg = spinner.getSelectedItem().toString();
            if (selectedOrg.equals("Choose your Organization")) {
                // Display an error message to the user
                Toast.makeText(RegistrationActivity.this, "Please select an organization", Toast.LENGTH_SHORT).show();
            }

            if (TextUtils.isEmpty(fnameString)) {
                regFname.setError("First Name is Required");
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

                DatabaseReference orgRef = FirebaseDatabase.getInstance().getReference("organization-details");
                Query query = orgRef.orderByChild("org_name").equalTo(selectedOrg);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String orgId = dataSnapshot.getChildren().iterator().next().getKey();
                            // Do something with the orgId, such as save it to a variable for later use
                            str = orgId;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                    }
                });

                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = mAuth.getCurrentUser().getUid();
                        User user = new User(id, str, fnameString, lnameString, emailString);

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

        //Organization Registration
        orgBtn.setOnClickListener(view -> {
            String nameString = orgName.getText().toString();
            String emailString = orgEmail.getText().toString();
            String passwordString = orgPassword.getText().toString();

            if (TextUtils.isEmpty(nameString)) {
                orgName.setError("Name is Required");
            }

            if (TextUtils.isEmpty(emailString)) {
                orgEmail.setError("Email is Required");
            }

            if (TextUtils.isEmpty(passwordString)) {
                orgPassword.setError("Password is Required");
            } else {
                orgBtn.setEnabled(false);
                orgBtn.setText("Creating your Account...");

                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = mAuth.getCurrentUser().getUid();
                        Organization organization = new Organization(id, nameString, emailString);

                        users = FirebaseDatabase.getInstance().getReference().child("organization-details").child(mAuth.getCurrentUser().getUid());

                        users.setValue(organization).addOnCompleteListener(task1 -> {
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        orgBtn.setText("Create Account");
                        orgBtn.setEnabled(true);
                    }
                });
            }


        });

        gotoLogin.setOnClickListener(this::onLoginClick);

        orggotoLogin.setOnClickListener(this::onLoginClick);
    }

    private void refreshSpinner() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("organization-details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> dataList = new ArrayList<>();
                dataList.add("Choose your Organization");

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Organization data = child.getValue(Organization.class);
                    dataList.add(data.getOrg_name());
                }

                // Set up spinner with retrieved data
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, dataList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }
}