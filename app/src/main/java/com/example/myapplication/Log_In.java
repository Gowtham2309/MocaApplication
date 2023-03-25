package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Log_In extends AppCompatActivity {
    EditText phonelog,passlog;
    Button buttonn, btnToRegister;
    DatabaseReference dbreferance= FirebaseDatabase.getInstance().getReferenceFromUrl("https://moca-test-5bfbb-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        phonelog=findViewById(R.id.Phonelogin);
        passlog=findViewById(R.id.Passlogin);
        buttonn=findViewById(R.id.button24);
        btnToRegister = findViewById(R.id.buttonToRegister);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phtxt=phonelog.getText().toString();
                final String passtxt=passlog.getText().toString();

                // if any fields are empty notify user and don't login
                if (phtxt.isEmpty() || passtxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbreferance.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(phtxt))
                        {
                            final String getpassword=snapshot.child(phtxt).child("password").getValue(String.class);
                            if(getpassword.equals(passtxt))
                            {
                                Toast.makeText(getApplicationContext(),"Log in Success",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(Log_In.this,Test.class);
                                intent.putExtra("Message_Key",phtxt);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Log in Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnToRegister.setOnClickListener((View view) -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });
    }
}