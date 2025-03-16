package edu.msu.cse476.baragurr.ecosnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.cse476.baragurr.ecosnap.R;

public class Startup extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("user") && password.getText().toString().equals("1234")) {
                    Toast.makeText(Startup.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Startup.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onStartHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}