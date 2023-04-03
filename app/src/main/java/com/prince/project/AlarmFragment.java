package com.prince.project;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prince.project.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

public class AlarmFragment extends Fragment {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Button setAlarm;
    private Button cancelAlarm;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private TextView textView;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        setAlarm = view.findViewById(R.id.setAlarm);
        cancelAlarm = view.findViewById(R.id.cancelAlarm);
        textView = view.findViewById(R.id.selectTime);

        createNotificationChannel();

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //It is a user
                    textView.setText("Interval has\nbeen set\nfor 20 minutes");
                } else {
                    //It is an organization
                    textView.setText("Check organisation\ntab for details");
                    setAlarm.setEnabled(false);
                    cancelAlarm.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                textView.setText("Error");
                setAlarm.setVisibility(View.GONE);
                cancelAlarm.setVisibility(View.GONE);
            }
        });

        // Set Alarm
        setAlarm.setOnClickListener(view12 -> {
            long intervalMillis = 20 * 60 * 1000;
            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);

            Toast.makeText(getContext(), "Shift has started!", Toast.LENGTH_SHORT).show();
        });

        //Cancel Alarm
        cancelAlarm.setOnClickListener(view13 -> {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getContext().getSystemService(ns);
            nMgr.cancel(123);

            Toast.makeText(getContext(), "Shift has been ended!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "smartsecurity";
            String desc = "Channel for Smart Security";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("smartsecurity", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}