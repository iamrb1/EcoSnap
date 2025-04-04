package edu.msu.cse476.baragurr.ecosnap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    TextView user_text;
    TextView recycle_text;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Startup.class );
            startActivity(intent);
            finish();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String username = user.getEmail();
//        int num_recycled = currentAccount.getRecycleCount();
//
        user_text = findViewById(R.id.username_welcome_text);
//
        String username_formatted = getString(R.string.username_welcome, username);
        user_text.setText(username_formatted);

//        recycle_text = findViewById(R.id.recycle_count_text);
//
//        String recycle_formatted = getString(R.string.recycle_count, num_recycled);
//        recycle_text.setText(recycle_formatted);

    }

    public void GetUserData(){

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