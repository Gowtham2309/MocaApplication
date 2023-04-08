package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Naming extends AppCompatActivity {
    Button buttonn;
    ImageView img1,img2,img3;
    Map<Integer, Integer> map= new HashMap<Integer, Integer>();
    List<String> names= new ArrayList<>();
    ArrayList<Integer> sets=new ArrayList<>();
    int min=1,max=10,rand=0;
    EditText name1,name2,name3;
    private int count=0;
    final static String TEST_NAME="NAMING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Naming");
        } catch (NullPointerException ignored) {}

        buttonn=findViewById(R.id.button39);
        img1=findViewById(R.id.imageView);
        img2=findViewById(R.id.imageView2);
        img3=findViewById(R.id.imageView3);
        name1=findViewById(R.id.editTextTextPersonName16);
        name2=findViewById(R.id.editTextTextPersonName15);
        name3=findViewById(R.id.editTextTextPersonName17);

        names.add("cat");
        names.add("lion");
        names.add("camel");
        names.add("rhinoceros");
        names.add("dog");
        names.add("horse");
        names.add("goat");
        names.add("tiger");
        names.add("cow");
        names.add("pig");


        map.put(1,R.drawable.cat);
        map.put(2,R.drawable.lion);
        map.put(3,R.drawable.camel);
        map.put(4,R.drawable.rhinoceros);
        map.put(5,R.drawable.dog);
        map.put(6,R.drawable.horse);
        map.put(7,R.drawable.goat);
        map.put(8,R.drawable.tiger);
        map.put(9,R.drawable.cow);
        map.put(10,R.drawable.pig);
        Random r=new Random();

        while(sets.size()<3)
        {
            rand = r.nextInt(max - min + 1) + min;
            if(!sets.contains(rand)) sets.add(rand);
        }
        img1.setImageResource(map.get(sets.get(0)));
        img2.setImageResource(map.get(sets.get(1)));
        img3.setImageResource(map.get(sets.get(2)));

//        String ans1=name1.getText().toString();
//        String ans2=name2.getText().toString();
//        String ans3=name3.getText().toString();

        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans1=name2.getText().toString();
                String ans2=name1.getText().toString();
                String ans3=name3.getText().toString();
                ans1.toLowerCase();
                ans2.toLowerCase();
                ans3.toLowerCase();
                if(ans1.equals(names.get(sets.get(0)-1))) count++;
                if(ans2.equals(names.get(sets.get(1)-1))) count++;
                if(ans3.equals(names.get(sets.get(2)-1))) count++;
                System.out.println(ans1+" "+sets.get(0));
                System.out.println(ans2+" "+sets.get(1));
                System.out.println(ans3+" "+sets.get(2));
                System.out.println(count);
                ScoreMaintainer scoreMaintainer = ScoreMaintainer.getInstance();
                scoreMaintainer.updateScore(TEST_NAME, count);
                System.out.println("SCORE: "+scoreMaintainer.getScore(TEST_NAME));
                Intent intent = new Intent(Naming.this, Memory_Delayedrecall.class);
                startActivity(intent);
                finish();
            }
        });
    }
}