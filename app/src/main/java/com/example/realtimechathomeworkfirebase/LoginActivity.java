package com.example.realtimechathomeworkfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    /**
     * .
     */
    private MaterialEditText email;
    /**
     * .
     */
    private MaterialEditText password;
    /**
     * .
     */
    private Button btnLogin;
    /**
     * .
     */
    private FirebaseAuth firebaseAuth;
    /**
     * .
     */
    private TextView resetPassword;

    public static boolean login(String mail, String pass, Context context, FirebaseAuth firebaseAuth) {
        final boolean[] state = {false};
        if (!(Validation.validate_email_password(mail, pass))) {
            return false;
        } else {
            firebaseAuth
                    .signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void
                                onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        state[0] = true;
                                    } else {
                                        System.out.println("False Login!!");
                                    }

                                }
                            });
        }
        return state[0];
    }

    /**
     * .
     */


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
                if (login(mail, pass, LoginActivity.this, firebaseAuth)) {
                    Intent intent = new Intent(LoginActivity
                            .this, MainActivity.class);
                    intent.addFlags(Intent
                            .FLAG_ACTIVITY_CLEAR_TASK | Intent
                            .FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }

        });

    }


}
