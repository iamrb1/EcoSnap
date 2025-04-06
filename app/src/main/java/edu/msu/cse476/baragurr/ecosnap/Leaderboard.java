package edu.msu.cse476.baragurr.ecosnap;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);


        TextView leaderboardText = findViewById(R.id.leaderboardText);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_clicks");


        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                List<UserClick> users = new ArrayList<>();


                for (DataSnapshot snap : task.getResult().getChildren()) {
                    String email = snap.child("email").getValue(String.class);
                    Long count = snap.child("buttonClicks").getValue(Long.class);


                    if (email != null && count != null) {
                        users.add(new UserClick(email, count));
                    }
                }


                // Sort descending
                Collections.sort(users, (u1, u2) -> Long.compare(u2.buttonClicks, u1.buttonClicks));


                // Display top 5
                StringBuilder leaderboard = new StringBuilder("Top 5 Clicks:\n");
                for (int i = 0; i < Math.min(5, users.size()); i++) {
                    leaderboard.append((i + 1)).append(". ")
                            .append(users.get(i).email)
                            .append(" - ")
                            .append(users.get(i).buttonClicks).append(" clicks\n");
                }


                leaderboardText.setText(leaderboard.toString());
            }
        });
    }
}
