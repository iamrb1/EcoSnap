package edu.msu.cse476.baragurr.ecosnap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;


public class Profile extends AppCompatActivity {


    private TextView usernameText;
    private TextView recycleCountText;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private EditText goalEditText;
    private Button saveGoalButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        goalEditText = findViewById(R.id.goalEditText);
        saveGoalButton = findViewById(R.id.saveGoalButton);


        usernameText = findViewById(R.id.username_welcome_text);
        recycleCountText = findViewById(R.id.recycle_count_text);
        ImageView profilePic = findViewById(R.id.imageView); // optional for step 2


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if (user == null) {
            finish(); // shouldn't happen but safety
            return;
        }


        String uid = user.getUid();
        String email = user.getEmail();
        String username = email != null ? email.split("@")[0] : "User";


        // Set welcome message
        usernameText.setText(getString(R.string.username_welcome, username));


        // Get buttonClicks from Firebase
        userRef = FirebaseDatabase.getInstance().getReference("user_clicks").child(uid);


        userRef.child("goal").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String goal = task.getResult().getValue(String.class);
                goalEditText.setText(goal);
            }
        });


        userRef.child("buttonClicks").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Long clicks = task.getResult().getValue(Long.class);
                recycleCountText.setText(getString(R.string.recycle_count, clicks));
            } else {
                recycleCountText.setText(getString(R.string.recycle_count, 0));
            }
        });


        saveGoalButton.setOnClickListener(v -> {
            String goalText = goalEditText.getText().toString().trim();
            if (!goalText.isEmpty()) {
                userRef.child("goal").setValue(goalText)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(Profile.this, "Goal saved!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Profile.this, "Failed to save goal.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(Profile.this, "Please enter a goal first.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onBackClick(View view) {
        finish();
    }
}
