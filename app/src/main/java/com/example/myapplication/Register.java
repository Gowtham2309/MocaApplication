package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
    Button buttonn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    EditText email,password, age, ph,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        buttonn=findViewById(R.id.RegisterButton);
        email=findViewById(R.id.emailtext);
        password=findViewById(R.id.passwordtext);
        ph=findViewById(R.id.Phonetext);
        name=findViewById(R.id.Nametext);
        age=findViewById(R.id.Agetext);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString();
                String pass=password.getText().toString();
                String ag=age.getText().toString();
                String num=ph.getText().toString();
                ArrayList<Integer> Test_set=new ArrayList<>();
                System.out.println("_______________________________________________");
                String nam=name.getText().toString();
                String str="0";

                rootNode=FirebaseDatabase.getInstance();
                UserRegisterHelper helper=new UserRegisterHelper(nam,num,Email,pass,ag,str);
                reference= rootNode.getReferenceFromUrl("https://moca-test-5bfbb-default-rtdb.firebaseio.com/");
                reference.child("Users").child(num).setValue(helper);
                    Intent intent = new Intent(Register.this, gaming.class);
                    startActivity(intent);

            }
        });
    }
}