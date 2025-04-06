package edu.msu.cse476.baragurr.ecosnap;

public class UserClick {
    public String email;
    public long buttonClicks;


    public UserClick() {} // Needed for Firebase


    public UserClick(String email, long buttonClicks) {
        this.email = email;
        this.buttonClicks = buttonClicks;
    }

}
