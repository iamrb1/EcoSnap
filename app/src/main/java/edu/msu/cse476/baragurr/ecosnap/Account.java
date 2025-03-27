package edu.msu.cse476.baragurr.ecosnap;

import android.content.ClipData;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Account extends AppCompatActivity {

    private String Username;
    public String getUsername(){
        return Username;
    }
    public void setUsername(String user){
        Username = user;
    }

    private String Password;
    public String getPassword(){
        return Password;
    }
    public void setPassword(String pass){
        Password = pass;
    }

    private int Items_recycled;
    public int getRecycleCount(){
        return Items_recycled;
    }
    public void setRecycleCount(int items){
        Items_recycled = items;
    }

    public Account(String username, String password){
        setPassword(password);
        setUsername(username);
        setRecycleCount(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addItem(){
        Items_recycled += 1;
    }


}