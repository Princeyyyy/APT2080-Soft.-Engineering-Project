package com.prince.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prince.project.models.Organization;
import com.prince.project.models.User;

public class OrganisationFragment extends Fragment {

    private TextView orgname;
    private TextView orgemail;
    private ImageView orgimage;
    private FirebaseAuth auth;

    public OrganisationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organisation, container, false);

        orgname = view.findViewById(R.id.orgname);
        orgemail = view.findViewById(R.id.orgemail);
        orgimage = view.findViewById(R.id.orgImage);
        auth = FirebaseAuth.getInstance();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getUid());
        DatabaseReference orgReference = FirebaseDatabase.getInstance().getReference("organization-details").child(auth.getUid());

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // It is a user
                    User user = dataSnapshot.getValue(User.class);
                    String org_id = user.getOrg_id();
                    Log.d("Id", org_id);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("organization-details").child(org_id);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Organization organization = snapshot.getValue(Organization.class);
                            orgname.setText("Organization name: " + organization.getOrg_name());
                            orgemail.setText("Organization email: " + organization.getOrg_email());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    // It is an organization
                    orgimage.setVisibility(View.GONE);
                    orgname.setText("Employee Activity Details will appear here");
                    orgemail.setVisibility(View.GONE);

                    //Display activity after employee have done everything
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        return view;
    }
}