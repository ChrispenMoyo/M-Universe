package com.example.m_universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText et_user, et_pass;
    Button btn_login, btn_clear;

    TextView tv_register, tv_forgot;
    String user, pass;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        et_user = findViewById(R.id.email_id2);
        et_pass = findViewById(R.id.password2);
        btn_login = findViewById(R.id.logins);
        btn_clear = findViewById(R.id.clear);
        tv_register = findViewById(R.id.register);
        tv_forgot = findViewById(R.id.forgot);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill = et_user.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();

                // if format of email doesn't matches return null
                if (!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()) {
                    et_user.setError("Invalid Email");
                    et_user.setFocusable(true);

                } else {
                    loginUser(emaill, pass);
                }
            }
        });
        //other buttons
        btn_clear.setOnClickListener(view -> {
            et_user.setText("");
            et_pass.setText("");
        });
        tv_register.setOnClickListener(v -> sendToRegActivity());
    }

    private void sendToRegActivity() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    private void loginUser(String email, String password) {
        progressDialog.setMessage("Please Wait while Login...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Sign in with email and password after authenticating
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            String uid = user.getUid();
                            // Now 'uid' contains the UID of the logged-in user
                            // You can use this 'uid' to query specific user data or perform other actions
                            // ...

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error Occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}