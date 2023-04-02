package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class StartPage extends AppCompatActivity {
    private EditText editPhysicianName, editPatientName, editPatientAge;
    private Button btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        editPhysicianName = findViewById(R.id.editTextPhysicianName);
        editPatientName = findViewById(R.id.editTextPatientName);
        editPatientAge = findViewById(R.id.editTextPatientAge);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener((View v) -> {
            if (!checkEditTextFields()) {
                Toast.makeText(getApplicationContext(), "Enter all the fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // clear the existing scores and details
            ScoreMaintainer scoreMaintainer = ScoreMaintainer.getInstance();
            scoreMaintainer.clear();
            // set required details
            scoreMaintainer.setPhysicianName(editPhysicianName.getText().toString());
            scoreMaintainer.setPatientName(editPatientName.getText().toString());
            scoreMaintainer.setPatientAge(Integer.parseInt(editPatientAge.getText().toString()));
            scoreMaintainer.recordStartTime();
            System.out.println(scoreMaintainer);

            Intent intent = new Intent(this, cdt_Drawing.class);
            startActivity(intent);
            finish();
        });

    }

    private boolean checkEditTextFields() {
        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(editPhysicianName);
        editTexts.add(editPatientName);
        editTexts.add(editPatientAge);

        for(EditText edit: editTexts) {
            if (edit.getText().toString().trim().isEmpty())
                return false;
        }

        return true;
    }
}