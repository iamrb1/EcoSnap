package edu.msu.cse476.baragurr.ecosnap;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
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

    private LinearLayout leaderboardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // the container in the layout to add rows
        leaderboardContainer = findViewById(R.id.leaderboardContainer);

        // reference to "user_clicks" node in Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_clicks");

        // Retrieve data
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // collect user data into a list
                List<UserClick> users = new ArrayList<>();

                // each child (someUserId), get email + buttonClicks
                for (DataSnapshot snap : task.getResult().getChildren()) {
                    String email = snap.child("email").getValue(String.class);
                    Long count = snap.child("buttonClicks").getValue(Long.class);

                    if (email != null && count != null) {
                        users.add(new UserClick(email, count));
                    }
                }

                // Sort by descending clicks
                Collections.sort(users, (u1, u2) -> Long.compare(u2.buttonClicks, u1.buttonClicks));

                // display top 10
                int limit = Math.min(10, users.size());
                for (int i = 0; i < limit; i++) {
                    int rank = i + 1;  // rank starts at 1
                    String userEmail = users.get(i).email;
                    long userClicks = users.get(i).buttonClicks;

                    // Dynamically create a row matching your desired design
                    addLeaderboardRow(rank, userEmail, userClicks);
                }
            }
        });
    }

    private void addLeaderboardRow(int rank, String email, long clicks) {
        // create the row's LinearLayout
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // layout_width="match_parent", layout_height="wrap_content"
        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        // marginVertical="4dp" => top & bottom margins 4dp
        int margin4dp = dpToPx(4);
        rowParams.setMargins(0, margin4dp, 0, margin4dp);

        rowLayout.setLayoutParams(rowParams);
        rowLayout.setBackgroundColor(Color.parseColor("#2B7F4E")); // the same green you had
        rowLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12)); // padding="12dp"

        // Rank TextView (#1, #2, etc.)
        TextView rankText = new TextView(this);
        rankText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        rankText.setText("#" + rank);
        rankText.setTextColor(Color.WHITE);
        rankText.setTextSize(16);
        rankText.setTypeface(rankText.getTypeface(), Typeface.BOLD);

        // Spacer View (like <View android:layout_width="16dp" android:layout_height="0dp" />)
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(16), 0));

        // Email TextView (layout_weight="1")
        TextView emailText = new TextView(this);
        LinearLayout.LayoutParams emailParams =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        emailText.setLayoutParams(emailParams);
        emailText.setText(email);
        emailText.setTextColor(Color.WHITE);
        emailText.setTextSize(16);

        // Clicks TextView
        TextView clicksText = new TextView(this);
        clicksText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        clicksText.setText(String.valueOf(clicks));
        clicksText.setTextColor(Color.WHITE);
        clicksText.setTextSize(16);

        // Add in the same order
        rowLayout.addView(rankText);
        rowLayout.addView(spacer);
        rowLayout.addView(emailText);
        rowLayout.addView(clicksText);

        leaderboardContainer.addView(rowLayout);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    public void onBackClick(View view) {
        finish();
    }

    /**
     * model class for storing user data
     */
    private static class UserClick {
        public String email;
        public long buttonClicks;

        public UserClick(String email, long buttonClicks) {
            this.email = email;
            this.buttonClicks = buttonClicks;
        }
    }
}