package com.example.m_universe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        btn_login.setOnClickListener(view -> performLogin());
        btn_clear.setOnClickListener(view -> {
            et_user.setText("");
            et_pass.setText("");
        });
        tv_register.setOnClickListener(v -> sendToRegActivity());
    }
    private void sendToRegActivity() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }
    private void performLogin()
    {
        user = et_user.getText().toString();
        pass = et_pass.getText().toString();
        if (pass.isEmpty() || pass.length() < 6) {
            et_pass.setError("Enter min 6 char password");
        }
        else {
            progressDialog.setMessage("Please Wait while Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(LoginActivity.this,"Welcome " + task.getException(), Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        String uname = user.replace("@gmail.com", "");
        i.putExtra("user",uname);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}