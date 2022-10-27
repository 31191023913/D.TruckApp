package com.example.dtruckapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dtruckapp.Common.Common;
import com.example.dtruckapp.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserProfileFragment extends Fragment {

    View Pview;
    TextView UserName, NameUserdShow,UserEmailp,UserPhonep,UserDLp,UserPassp, MoreAboutUser, EdítSave;;
    ImageView UserIMGS;
    Button btnSignOut;
    String fullNameUs, EmailUs, phoneNumberUs, PasswordUs, driveLiensceUs, bioUs, urlImgUs;
    private FirebaseAuth userAuth;
    private DatabaseReference uDataref;
    private FirebaseDatabase fdatabase;
    private ValueEventListener showInfo;
    String UserID="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Pview =  inflater.inflate(R.layout.fragment_user_profile, container, false);
        initUI();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        uDataref = FirebaseDatabase.getInstance().getReference().child("User");
        userAuth = FirebaseAuth.getInstance();

        fdatabase = FirebaseDatabase.getInstance();
        UserID=userAuth.getCurrentUser().getUid();
        uDataref = fdatabase.getReference().child("User");

        EdítSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToEditPage();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignoutApp();
            }
        });
        return Pview;
    }



    private void initUI() {
        UserName = Pview.findViewById(R.id.UsernameP);        //Name
        UserIMGS = Pview.findViewById(R.id.imguser);        //ImageUser

        NameUserdShow = Pview.findViewById(R.id.NameShows);
        UserEmailp = Pview.findViewById(R.id.EmailShows);
        UserPhonep = Pview.findViewById(R.id.PhoneShows);
        UserDLp = Pview.findViewById(R.id.DriveShows);
        UserPassp = Pview.findViewById(R.id.PasswordShow);
        MoreAboutUser = Pview.findViewById(R.id.BioShow);

        EdítSave = Pview.findViewById(R.id.EditProfileS);
        btnSignOut = Pview.findViewById(R.id.btnDangXuat);
    }

    private void SignoutApp() {
        FirebaseAuth.getInstance().signOut();
        Intent Loggingout = new Intent(getContext(),MainActivity.class);
        Loggingout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Loggingout);
    }

    private void GoToEditPage() {
        EditProfileFragment edituserProfileSs = new EditProfileFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerFL,edituserProfileSs).commit();
    }

    @Override
    public void onStart(){
        super.onStart();
        showInfo = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullNameUs = snapshot.child("fullName").getValue().toString();
                EmailUs = snapshot.child("email").getValue().toString();
                phoneNumberUs = snapshot.child("phoneNumber").getValue().toString();
                PasswordUs = snapshot.child("password").getValue().toString();
                driveLiensceUs = snapshot.child("driveLicense").getValue().toString();
                bioUs = snapshot.child("biography").getValue().toString();
                urlImgUs = snapshot.child("urlImage").getValue().toString();

                User userprofile = new User(fullNameUs,EmailUs,phoneNumberUs,PasswordUs,driveLiensceUs,bioUs, urlImgUs);

                if(snapshot.exists() || snapshot.hasChild(urlImgUs)) {
                    UserName.setText(fullNameUs);
                    NameUserdShow.setText(fullNameUs);
                    UserEmailp.setText(EmailUs);
                    UserPhonep.setText(phoneNumberUs);
                    UserPassp.setText(PasswordUs);
                    UserDLp.setText(driveLiensceUs);
                    MoreAboutUser.setText(bioUs);
                    Glide.with(UserIMGS.getContext())
                            .load(urlImgUs)
                            .placeholder(R.drawable.ic_account)
                            .into(UserIMGS);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uDataref.child(UserID).addValueEventListener(showInfo);

        }


}