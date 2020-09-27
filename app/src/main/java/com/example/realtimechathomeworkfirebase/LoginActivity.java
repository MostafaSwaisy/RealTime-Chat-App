package com.example.realtimechathomeworkfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    /**
     *.
     */
    private MaterialEditText email;
    /**
     *.
     */
    private MaterialEditText password;
    /**
     *.
     */
  private   Button btnLogin;
    /**
     *.
     */
   private FirebaseAuth firebaseAuth;
    /**
     *.
     */
   private TextView resetPassword;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        resetPassword = findViewById(R.id.forget_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(new Intent(LoginActivity
                        .this, ResetPasswordActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(LoginActivity
                            .this, "there is a field is empty", Toast
                            .LENGTH_SHORT)
                            .show();
                } else {
                    firebaseAuth
                            .signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void
                        onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity
                                        .this, MainActivity.class);
                                intent.addFlags(Intent
                                        .FLAG_ACTIVITY_CLEAR_TASK | Intent
                                        .FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity
                                        .this, "Auth operation is failed!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            }
        });

    }
}
