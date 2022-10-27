package com.example.dtruckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dtruckapp.Common.Common;
import com.example.dtruckapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity {
    private EditText editEmails, editPassword;
    private TextView resetpass;
    private Button btnDangNhap, btnDangKy;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        editPassword = findViewById(R.id.editPassword);
        editEmails = findViewById(R.id.editEmailDN);
        btnDangNhap = findViewById(R.id.btnDangNhapA);
        btnDangKy = findViewById(R.id.btnGoToDangKy);
        resetpass = findViewById(R.id.resetpassword);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangNhapA();
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(DangNhap.this, DangKy.class);
                startActivity(signUp);
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ResetPassword(); }
        });
    }

    private void ResetPassword() {
        Intent resetpass = new Intent(DangNhap.this,ResetPasswordActivity.class);
        //phong truong hop nhan back lai dang nhap page
        startActivity(resetpass);
    }

    private void DangNhapA() {

        ProgressDialog mDialog = new ProgressDialog(DangNhap.this);
        mDialog.setMessage("Vui lòng chờ một chút");
        mDialog.show();
        String EM = editEmails.getText().toString().trim();
        String PW = editPassword.getText().toString().trim();

        if(TextUtils.isEmpty(EM)){
            editEmails.setError("Bạn chưa nhập Email!");
            editEmails.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(PW)){
            editPassword.setError("Bạn chưa nhập mật khẩu!");
            editPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(EM,PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mDialog.dismiss();
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Common.currentUser = snapshot.getValue(User.class);

                                    Toast.makeText(DangNhap.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                    Intent sucess = new Intent(DangNhap.this,Direction.class);
                                    //phong truong hop nhan back lai dang nhap page
                                    sucess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(sucess);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(DangNhap.this,"Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}