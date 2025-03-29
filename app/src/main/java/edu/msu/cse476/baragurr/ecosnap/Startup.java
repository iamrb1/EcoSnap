package edu.msu.cse476.baragurr.ecosnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.msu.cse476.baragurr.ecosnap.R;

public class Startup extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    TextInputEditText editTextUsername, editTextPassword;
    FirebaseAuth mAuth;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        textView = findViewById(R.id.signupText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Account.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void onClick(View view) {
        String username, password;
        username = String.valueOf(editTextUsername.getText());
        password = String.valueOf(editTextPassword.getText());

        if (TextUtils.isEmpty(username)) {

            Toast.makeText(Startup.this, "Enter username", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(password)) {

            Toast.makeText(Startup.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Startup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void onStartHome(View view) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }

}