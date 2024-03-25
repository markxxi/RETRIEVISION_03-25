package com.example.retrievision;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call button from xml
        Button signUp = findViewById(R.id.signup);
        Button signIn = findViewById(R.id.signin);

        // add function to button
        signUp.setOnClickListener(view->openHomeFragment());
        signIn.setOnClickListener(view->openHomeFragment());
    }
public void openHomeFragment(){
        //intent = calling the next page
    Intent intent = new Intent(this, home.class);
    startActivity(intent);
}
}