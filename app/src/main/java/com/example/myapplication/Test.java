package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn=findViewById(R.id.button40);
        Button btnLogOut = findViewById(R.id.buttonLogOut);

        Intent intent_txt=getIntent();
        String str=intent_txt.getStringExtra("Message_Key");
        System.out.println(str);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test.this, cdt_Drawing.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener((View view) -> {
            SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedPrefsName), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(getString(R.string.login));
            editor.apply();

            Intent intent = new Intent(Test.this, Log_In.class);
            startActivity(intent);
            finish();
        });
    }

}