package com.example.retrievision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {

    BottomNavigationView navbar;
    private View underlineView;
    ConstraintLayout constraintLayout;

    homefragment home = new homefragment();
    rewardfragment reward = new rewardfragment();
    notificationfragment notification = new notificationfragment();
    profilefragment profile = new profilefragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       navbar = findViewById(R.id.bottom_navigation);
       constraintLayout = findViewById(R.id.constraint_layout);
       underlineView = findViewById(R.id.underline);

       //once navigated to home, begin or show fragment home
       getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, home).commit();
        //when item or icons get selected in the function, replace the screen with fragment
       navbar.setOnItemSelectedListener(item -> {
           underlineSelectedItem(item);
           if(item.getItemId()==R.id.nav_home){
               getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,home).commit();
               return true;
           } else if (item.getItemId()==R.id.nav_rank) {
               getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, reward).commit();
               return true;
           } else if (item.getItemId()==R.id.nav_notification) {
               getSupportFragmentManager().beginTransaction().replace(R.id.nav_notification, notification).commit();
               return true;
           } else if (item.getItemId()==R.id.nav_profile) {
               getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profile).commit();
               return true;
           }
                   return false;
       }
       ); underlineSelectedItem(navbar.getMenu().getItem(0));

    }
    //call the menu navigation from res menu as a parameter
    //items are the menu icons
    public void underlineSelectedItem(MenuItem item){
        //animation inside a constraint layout
        TransitionManager.beginDelayedTransition(constraintLayout);
        //instance for changes and movement of underline inside the layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        //get item home, and set horizontal underline when selected
        if(item.getItemId()==R.id.nav_home){
            //f is for the position of underline
            constraintSet.setHorizontalBias(R.id.underline, 0f);
        } else if(item.getItemId()==R.id.nav_rank){
            constraintSet.setHorizontalBias(R.id.underline, 0.31f);
        } else if (item.getItemId()==R.id.nav_notification) {
            constraintSet.setHorizontalBias(R.id.underline, 0.64f);
        } else if (item.getItemId()==R.id.nav_profile) {
            constraintSet.setHorizontalBias(R.id.underline,0.95f);
        }
        constraintSet.applyTo(constraintLayout);
    }
    //syntax of fragment is different from activity
}