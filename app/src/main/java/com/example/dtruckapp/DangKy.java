package com.example.dtruckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dtruckapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangKy extends AppCompatActivity {

    private EditText editPhone, editEmailR ,  editName, editPassword, editPass2;
    private Button btnDangKy,btnCancels;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        editName= findViewById(R.id.editNameDK);
        editEmailR = findViewById(R.id.editEmailDK);
        editPhone= findViewById(R.id.editPhoneDK);
        editPassword= findViewById(R.id.editPasswordDK);
        editPass2 = findViewById(R.id.confirmPassword);
        btnCancels = findViewById(R.id.btnCancels);
        mAuth = FirebaseAuth.getInstance();

        btnDangKy= findViewById(R.id.btnTaoTK);
        btnCancels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BACKmAIN = new Intent(DangKy.this, MainActivity.class);
                startActivity(BACKmAIN);
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangKyTaiKhoan();
            }
        });
    }

    private void DangKyTaiKhoan() {

        String FullName = editName.getText().toString();
        String Email = editEmailR.getText().toString();
        String PhoneNumber = editPhone.getText().toString();
        String Password = editPassword.getText().toString();
        String CPassword = editPass2.getText().toString();
        String DriveLicenseD = "Người dùng chưa bổ sung";
        String Biography = "Chưa bổ sung";
        String urlImage = "";

        if (TextUtils.isEmpty(Email)){
            Toast.makeText(DangKy.this,"Bạn chưa nhập Email!",Toast.LENGTH_SHORT).show();
            editPhone.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(PhoneNumber)){
            Toast.makeText(DangKy.this,"Bạn chưa nhập số điện thoại!",Toast.LENGTH_SHORT).show();
            editName.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(FullName)){
            Toast.makeText(DangKy.this,"Bạn chưa nhập tên!",Toast.LENGTH_SHORT).show();
            editName.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(Password)){
            Toast.makeText(DangKy.this,"Xin hãy nhập mật khẩu!",Toast.LENGTH_SHORT).show();
            editPassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(CPassword)){
            Toast.makeText(DangKy.this,"Xin hãy nhập mật khẩu xác định!",Toast.LENGTH_SHORT).show();
            editPass2.requestFocus();
            return;
        }
        else if (!Password.equals(CPassword)){
            Toast.makeText(DangKy.this,"Mật khẩu xác định và không trùng khớp với nhau!",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            ProgressDialog mDialog = new ProgressDialog(DangKy.this);
            mDialog.setMessage("Vui lòng chờ một chút");
            mDialog.show();
            mAuth.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(FullName, Email,Password,PhoneNumber, DriveLicenseD, urlImage,Biography);
                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mDialog.dismiss();
                                            Toast.makeText(DangKy.this,"Đăng ký thành công", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(DangKy.this,"Email này đã được đăng ký", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });


        }

    }
}