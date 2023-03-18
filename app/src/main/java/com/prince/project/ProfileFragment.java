package com.prince.project;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prince.project.models.User;

import java.lang.reflect.Field;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        // Required empty public constructor
    }

    private Button button;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private TextView name;
    private ImageView image;
    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        button = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        image = view.findViewById(R.id.profileIcon);
        button.setOnClickListener(view1 -> {
            // Retrieve the PendingIntent reference from SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String pendingIntentString = prefs.getString("pendingIntent", null);
            if (pendingIntentString != null) {
                try {
                    // Convert the string back to a PendingIntent
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(), 0);
                    Field field = PendingIntent.class.getDeclaredField("mObject");
                    field.setAccessible(true);
                    Object object = field.get(pendingIntent);
                    String objString = object.toString();
                    if (objString.equals(pendingIntentString)) {
                        // The stored PendingIntent matches the one we want to cancel
                        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                        // Remove the stored PendingIntent reference from SharedPreferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("pendingIntent");
                        editor.apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    String fullname = user.getFname() + " " + user.getLname();
                    name.setText("Name: " + fullname);
                    email.setText("Email: " + user.getEmail());
                } else {
                    name.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}