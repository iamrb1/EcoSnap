package edu.msu.cse476.baragurr.ecosnap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity {
    FirebaseAuth auth;
    ImageButton logoutButton;
    ImageButton menuButton;
    TextView dropdownText;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        menuButton = findViewById(R.id.menuButton);
        dropdownText = findViewById(R.id.textView2);


        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Startup.class );
            startActivity(intent);
            finish();
        }


        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Startup.class );
            startActivity(intent);
            finish();


        });


        // Load the slide-in and fade-in animation
        Animation slideInFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_fade_in);
        Animation slideOutFadeOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_fade_out);


        menuButton.setOnClickListener(v -> {
            if (dropdownText.getVisibility() == View.GONE) {
                dropdownText.setVisibility(View.VISIBLE);
                dropdownText.startAnimation(slideInFadeIn);  // Apply slide-in and fade-in when shown
            } else {
                dropdownText.startAnimation(slideOutFadeOut); // Apply slide-out and fade-out when hiding
                dropdownText.setVisibility(View.GONE); // After animation is done, hide the view
            }
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




