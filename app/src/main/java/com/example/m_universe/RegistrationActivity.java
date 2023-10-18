package com.example.m_universe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

import java.util.HashMap;
public class RegistrationActivity extends AppCompatActivity {
    TextView already_have_acc;
    Button btn_register;
    EditText et_gr_num, et_email, et_username, et_password, confirm_et;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    //FirebaseApp firebaseApp;

    // creating a variable for our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase
        //  firebaseApp.initializeApp(this);

        // Create a DatabaseReference to the "users" node in your database
        // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");


        et_gr_num = findViewById(R.id.gr_number);
        et_email = findViewById(R.id.email_id);
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        confirm_et = findViewById(R.id.confirm_password);
        btn_register = findViewById(R.id.register);
        already_have_acc = findViewById(R.id.login_tv);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //my own db
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("User");
        newUser = new User();

        btn_register.setOnClickListener(v -> performAuth());

        already_have_acc.setOnClickListener(v -> {
            Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", (dialog, which) ->
        {
            //if user pressed "yes", then he is allowed to exit from app
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) ->
        {
            //if user select "No", just cancel this dialog and continue with app
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void performAuth() {
        String gr_num = et_gr_num.getText().toString();
        String email1 = et_email.getText().toString();
        String userid = et_username.getText().toString();
        String pass1 = et_password.getText().toString();
        String confirm = confirm_et.getText().toString();

        // Create a User object with the collected data
        // newUser = new User();

        if (pass1.isEmpty() || pass1.length() < 6) {
            et_password.setError("Enter min 6 character password.");
        } else if (!pass1.equals(confirm)) {
            confirm_et.setError("Passwords Don't Match.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            et_email.setError("Invalid Email");
            et_email.setFocusable(true);
        } else {
            progressDialog.setMessage("Please wait while registrating ...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);

            saveUserDataToDatabase(gr_num, email1, userid, pass1);
        }

    }

    private void saveUserDataToDatabase(String gr_num, String email1, String userid, String pass1) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("gr num", gr_num);
                    hashMap.put("username", userid);
                    hashMap.put("image", "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap);
                    Toast.makeText(RegistrationActivity.this, "Registered User " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}

  /*  @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
        // Save the user data to your database here.
        // You can use Firebase Realtime Database or Firestore to store this data.
        /* Save the user data under a unique identifier (e.g., the user's ID)
        String userId = "unique_user_id"; // Replace with the actual user ID
        User newUser = new User(gr_num, email1, userid); // Create a User object
        databaseReference.child(userId).setValue(newUser);

        newUser.setGrNum(gr_num);
        newUser.setEmail(email1);
        newUser.setUserId(userid);

        mAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Registration successful, now save user data to the database
                saveUserDataToDatabase(gr_num, email1, userid);

                progressDialog.dismiss();
                sendUserToNextActivity();
                Toast.makeText(RegistrationActivity.this, "Registration Successful",Toast.LENGTH_LONG).show();
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this,"" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }
        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(newUser);

                // after adding this data we are showing toast message.
                Toast.makeText(RegistrationActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(RegistrationActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void sendUserToNextActivity() {
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

} */