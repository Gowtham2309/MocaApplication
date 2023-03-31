package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Abstraction extends AppCompatActivity {
    Button buttonn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstraction);
        try {
            getSupportActionBar().setTitle("Abstraction");
        } catch (NullPointerException ignored) {}
        buttonn=findViewById(R.id.button37);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(Orientation.class);
                Intent intent = new Intent(Abstraction.this,Orientation.class);
                startActivity(intent);
            }
        });
    }
}