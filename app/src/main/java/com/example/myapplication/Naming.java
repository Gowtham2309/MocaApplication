package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Naming extends AppCompatActivity {
    Button buttonn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Naming");
        } catch (NullPointerException ignored) {}

        buttonn=findViewById(R.id.button39);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Naming.this, Attentation_repeatingDigit.class);
                startActivity(intent);
            }
        });
    }
}