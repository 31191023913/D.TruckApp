package com.example.dtruckapp;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.dtruckapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {
    View Editview;
    TextInputEditText NameUserdedit,UserEmailedit,UserPhoneedit,UserDLpedit,UserPasspedit, MoreAboutUseredit;
    ImageView UserIMG;
    ImageButton gobackprofile;
    Button SaveProfileChange;
    TextView textUpdate;
    private ValueEventListener mListener, upImgListener, editProfileListener;
    String fullNameU, EmailU, phoneNumberU, PasswordU, driveLiensceU, BioU, urlImg;

    private FirebaseAuth userAuth;
    private DatabaseReference uDataref;
    private FirebaseDatabase fdatabase;
    String UserID="";
    private Uri imguri;
    private StorageReference mstorageRef;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Editview = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        textUpdate = Editview.findViewById(R.id.btnUpdateImg);

        UserIMG = Editview.findViewById(R.id.imguseredit);
        NameUserdedit = Editview.findViewById(R.id.EditNameUserProfile);
        UserEmailedit = Editview.findViewById(R.id.EditEmailProfile);
        UserPhoneedit = Editview.findViewById(R.id.EditPhoneProfile);
        UserDLpedit = Editview.findViewById(R.id.EditDrivingProfile);
        UserPasspedit = Editview.findViewById(R.id.editPasswordProfile);
        MoreAboutUseredit = Editview.findViewById(R.id.writeaboutyou);

        SaveProfileChange = Editview.findViewById(R.id.btnUpdateEdit);
        gobackprofile = Editview.findViewById(R.id.backtoprofile);

        userAuth = FirebaseAuth.getInstance();

        fdatabase = FirebaseDatabase.getInstance();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=userAuth.getCurrentUser().getUid();
        uDataref = fdatabase.getReference().child("User");

        mstorageRef = FirebaseStorage.getInstance().getReference();

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                    imguri = result.getData().getData();
                    UserIMG.setImageURI(imguri);

                }
            }
        });

        gobackprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileFragment UserProfileSs = new UserProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerFL,UserProfileSs).commit();

            }
        });


        UserIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intentActivityResultLauncher.launch(intent);

            }
        });

        textUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imguri != null){
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    StorageReference picsRef = mstorageRef.child("images/" + UserID);
                    picsRef.putFile(imguri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    picsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Map<String,Object> map=new HashMap<>();
                                            map.put("urlImage",imguri.toString());

                                            upImgListener = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        uDataref.child(UserID).updateChildren(map);
                                                    }
                                                    else {
                                                        uDataref.child(UserID).setValue(map);
                                                    }

                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            };
                                            uDataref.child(UserID).addListenerForSingleValueEvent(upImgListener);
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Updated Failed",Toast.LENGTH_SHORT);
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    //calculating progress percentage
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    //displaying percentage in progress dialog
                                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });
                }
            }
        });

        SaveProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdataProfile();
            }
        });
        return Editview;
    }

    private void UpdataProfile() {

        ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Vui lòng chờ một chút");
        mDialog.show();

        fullNameU = NameUserdedit.getEditableText().toString().trim();
        EmailU = UserEmailedit.getEditableText().toString().trim();
        phoneNumberU = UserPhoneedit.getEditableText().toString().trim();
        PasswordU= UserPasspedit.getEditableText().toString().trim();
        driveLiensceU = UserDLpedit.getEditableText().toString().trim();
        BioU = MoreAboutUseredit.getEditableText().toString().trim();

        final Map<String,Object> mapText=new HashMap<>();
        mapText.put("biography",BioU);
        mapText.put("driveLicense",driveLiensceU);
        mapText.put("email",EmailU);
        mapText.put("fullName",fullNameU);
        mapText.put("password",PasswordU);
        mapText.put("phoneNumber",phoneNumberU);

        editProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    uDataref.child(UserID).updateChildren(mapText);}
                else{
                    uDataref.child(UserID).setValue(mapText);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uDataref.child(UserID).addListenerForSingleValueEvent(editProfileListener);
        mDialog.dismiss();
        Toast.makeText(getContext(),"Updated Successfully",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart(){
        super.onStart();
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameUs = snapshot.child("fullName").getValue().toString();
                String EmailUs = snapshot.child("email").getValue().toString();
                String phoneNumberUs = snapshot.child("phoneNumber").getValue().toString();
                String PasswordUs = snapshot.child("password").getValue().toString();
                String driveLiensceUs = snapshot.child("driveLicense").getValue().toString();
                String bioUs = snapshot.child("biography").getValue().toString();
                String urlImgUs = snapshot.child("urlImage").getValue().toString();

                User userprofile = new User(fullNameUs,EmailUs,phoneNumberUs,PasswordUs,driveLiensceUs,bioUs, urlImgUs);

                if (snapshot.exists() || snapshot.hasChild(urlImgUs)){
                    NameUserdedit.setText(fullNameUs);
                    UserEmailedit.setText(EmailUs);
                    UserPhoneedit.setText(phoneNumberUs);
                    UserPasspedit.setText(PasswordUs);
                    UserDLpedit.setText(driveLiensceUs);
                    MoreAboutUseredit.setText(bioUs);
                    Glide.with(UserIMG.getContext())
                            .load(urlImgUs)
                            .placeholder(R.drawable.ic_account)
                            .into(UserIMG);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uDataref.child(UserID).addValueEventListener(mListener);
    }

}