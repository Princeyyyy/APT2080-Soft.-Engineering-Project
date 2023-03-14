package com.prince.project;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.prince.project.databinding.ActivityMainBinding;

import java.util.Calendar;

public class AlarmFragment extends Fragment {
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView selectTime;
    private Button setAlarm;
    private Button cancelAlarm;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        selectTime = view.findViewById(R.id.selectTime);
        setAlarm = view.findViewById(R.id.setAlarm);
        cancelAlarm = view.findViewById(R.id.cancelAlarm);

        createNotificationChannel();

        //Select Time
        selectTime.setOnClickListener(view1 -> {
            timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Alarm Time")
                    .build();
            timePicker.show(getActivity().getSupportFragmentManager(), "smartsecurity");

            timePicker.addOnPositiveButtonClickListener(view11 -> {
                if (timePicker.getHour() > 12) {
                    selectTime.setText(
                            String.format("%02d", (timePicker.getHour() - 12)) + ":" + String.format("%02d", timePicker.getMinute()) + "PM"
                    );
                } else {
                    selectTime.setText(timePicker.getHour() + ":" + timePicker.getMinute() + "AM");
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            });
        });

        // Set Alarm
        setAlarm.setOnClickListener(view12 -> {
            long intervalMillis = 2 * 60 * 1000;
            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);
            Toast.makeText(getContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
        });

        //Cancel Alarm
        cancelAlarm.setOnClickListener(view13 -> {
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
            if (alarmManager == null) {
                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(pendingIntent);
            Toast.makeText(getContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
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