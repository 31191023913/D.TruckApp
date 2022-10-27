package com.example.dtruckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText EmailReset;
    private ProgressDialog loadingBar;
    private TextView btnYs,btnNo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();

        EmailReset = findViewById(R.id.EmailReset);
        btnYs = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        loadingBar = new ProgressDialog(ResetPasswordActivity.this);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BackM = new Intent(ResetPasswordActivity.this, DangNhap.class);
                BackM.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(BackM);
            }
        });
        btnYs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setTitle("Reset Password");
                loadingBar.setMessage("Vui lòng chờ đợi hệ thống phản hồi");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                String EmailResetS = EmailReset.getEditableText().toString();
                if (TextUtils.isEmpty(EmailResetS)){
                    EmailReset.setError("Email là cần thiết để reset password!");
                    EmailReset.requestFocus();
                    return;
                }
                if (Patterns.EMAIL_ADDRESS.matcher(EmailResetS).matches()){
                    EmailReset.setError("Hãy nhập địa chỉ email hợp lệ!");
                    EmailReset.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(EmailResetS).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(ResetPasswordActivity.this,"Xin hãy kiểm tra mail của bạn để nhận link reset password",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(ResetPasswordActivity.this,"Xin hãy kiểm tra mail của bạn để nhận link reset password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}