package com.example.retrievision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class reportlostobject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportlostobject);

        Button start = findViewById(R.id.startButton);
        start.setOnClickListener(view -> openLostImage());

    }
    public void openLostImage(){
        Intent intent = new Intent(this, lostimage.class);
        startActivity(intent);
    }
}