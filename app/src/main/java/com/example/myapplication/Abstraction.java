package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Abstraction extends AppCompatActivity {
    Button buttonn;
    EditText edittext1,edittext2;
    int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstraction);
        edittext1=findViewById(R.id.editTextTextPersonName2);
        edittext2=findViewById(R.id.editTextTextPersonName4);
        try {
            getSupportActionBar().setTitle("Abstraction");
        } catch (NullPointerException ignored) {}
        buttonn=findViewById(R.id.button37);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1=edittext1.getText().toString();
                String str2=edittext2.getText().toString();
                str1.toLowerCase();
                if(str1.equals("vehicle") || str1.equals("transportation") || str1.equals("transport"))
                    score++;
                if(str2.equals("measurement") || str2.equals("measure") || str3.equals("measuring instrument"))
                    score++;
                System.out.println(score);
                Intent intent = new Intent(Abstraction.this,Orientation.class);
                startActivity(intent);
                finish();
            }
        });
    }
}