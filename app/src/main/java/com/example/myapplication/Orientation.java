
package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Orientation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button buttonn;
        buttonn=findViewById(R.id.button38);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Orientation.this, SampleJava.class);
//                startActivity(intent);
            }
        });
    }
}