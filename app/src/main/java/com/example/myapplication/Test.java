package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // setting the title of the action bar
        getSupportActionBar().setTitle("ACT");

        Button btn=findViewById(R.id.button40);
        Button btnToHistory = findViewById(R.id.buttonToHistory);

        Intent intent_txt=getIntent();
        String str=intent_txt.getStringExtra("Message_Key");
        System.out.println(str);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test.this, StartPage.class);
                startActivity(intent);
            }
        });

        btnToHistory.setOnClickListener((View view) -> {
            Intent intent = new Intent(Test.this, History.class);
            startActivity(intent);
        });
    }

    private void logOut() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedPrefsName), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(getString(R.string.login));
        editor.apply();

        Intent intent = new Intent(Test.this, Log_In.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_log_out) {
            logOut();
            return true;
        }
        return false;
    }
}