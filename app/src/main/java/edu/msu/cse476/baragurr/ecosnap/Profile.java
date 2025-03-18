package edu.msu.cse476.baragurr.ecosnap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    String username = "User";

    int num_recycled = 200;
    TextView user_text;
    TextView recycle_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user_text = findViewById(R.id.username_welcome_text);

        String username_formatted = getString(R.string.username_welcome, username);
        user_text.setText(username_formatted);

        recycle_text = findViewById(R.id.recycle_count_text);

        String recycle_formatted = getString(R.string.recycle_count, num_recycled);
        recycle_text.setText(recycle_formatted);

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