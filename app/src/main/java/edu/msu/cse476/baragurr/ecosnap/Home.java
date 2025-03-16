package edu.msu.cse476.baragurr.ecosnap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onStartCamera(View view) {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }
    public void onStartLeaderboard(View view) {
        Intent intent = new Intent(this, Leaderboard.class);
        startActivity(intent);
    }
    public void onStartProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
    public void onStartHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}