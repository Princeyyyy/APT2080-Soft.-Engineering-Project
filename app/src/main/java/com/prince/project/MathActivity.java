package com.prince.project;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Random;

public class MathActivity extends AppCompatActivity {
    int first;
    int second;
    int op;
    int answer;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        TextView tv = findViewById(R.id.question);
        Random rand = new Random();
        first = rand.nextInt((100 - 10) + 1) + 1;
        second = rand.nextInt((100 - 10) + 1) + 1;
        op = rand.nextInt((3 - 1) + 1) + 1;
        throwAlarm();

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

        final EditText et = findViewById(R.id.editText1);

        Button b1 = findViewById(R.id.button1);
        String answerString = String.valueOf(et.getText());

        if (TextUtils.isEmpty(answerString)) {
            et.setError("Answer is Required");
        }

        b1.setOnClickListener(v -> {
            try {
                if (answer == Integer.parseInt(et.getText().toString())) {
                    Intent intent = new Intent(MathActivity.this, MainActivity.class);
                    ringtone.stop();

                    Toast.makeText(this, "Nicely done!", Toast.LENGTH_SHORT).show();

                    startActivity(intent);
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

    private void throwAlarm() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (alarmSound == null) {
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }

        if (alarmSound != null) {
            ringtone = RingtoneManager.getRingtone(this, (alarmSound));
            ringtone.play();
        }
    }
}