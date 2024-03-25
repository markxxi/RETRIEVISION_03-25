package com.example.retrievision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class reportfoundobject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportfoundobject);

        Button start = findViewById(R.id.startButton);
        start.setOnClickListener(view->openFoundImage());
    }
    public void openFoundImage(){
        Intent intent = new Intent(this, foundimage.class);
        startActivity(intent);
    }
}