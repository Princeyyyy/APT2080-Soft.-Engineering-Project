package com.prince.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prince.project.models.Activity;
import com.prince.project.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MathActivity extends AppCompatActivity {
    int first;
    int second;
    int op;
    int answer;
    Ringtone ringtone;
    private FirebaseAuth auth;
    private String user_id;
    private String org_id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth = FirebaseAuth.getInstance();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getUid());

        TextView tv = findViewById(R.id.question);
        Random rand = new Random();
        first = rand.nextInt((100 - 10) + 1) + 1;
        second = rand.nextInt((100 - 10) + 1) + 1;
        op = rand.nextInt((3 - 1) + 1) + 1;

        if (op == 1) {
            tv.setText(first + " + " + second + " = ");
            answer = first + second;
        } else if (op == 2) {
            if (first != second) {
                tv.setText(first + " - " + second + " = ");
                answer = first - second;
            } else {
                tv.setText(first + " - " + (second + 1) + " = ");
                answer = first - (second + 1);
            }
        } else if (op == 3) {
            tv.setText(first + " x " + second + " = ");
            answer = first * second;
        }

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String fname = user.getFname();
                String lname = user.getLname();
                String u_id = user.getUser_id();
                String o_id = user.getOrg_id();
                name = fname + " " + lname;
                user_id = u_id;
                org_id = o_id;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Date currentTime = Calendar.getInstance().getTime();
        String timeSent = currentTime.toString();

        final EditText et = findViewById(R.id.editText1);

        Button b1 = findViewById(R.id.button1);

        b1.setOnClickListener(v -> {
            String answerString = String.valueOf(et.getText());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("activity").child(org_id);
            String id = reference.push().getKey();

            if (TextUtils.isEmpty(answerString)) {
                et.setError("Answer is Required");
            }

            try {
                if (answer == Integer.parseInt(et.getText().toString())) {
                    Intent intent = new Intent(MathActivity.this, MainActivity.class);

                    Toast.makeText(this, "Nicely done!", Toast.LENGTH_SHORT).show();

                    Activity activity = new Activity(user_id,org_id,name,timeSent);

                    reference.child(id).setValue(activity).addOnCompleteListener(task -> startActivity(intent));
                } else {
                    Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Log.d("NumInput", e.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }
}