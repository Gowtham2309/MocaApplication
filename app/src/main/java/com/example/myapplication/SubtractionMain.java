package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class SubtractionMain extends AppCompatActivity {
    TextView textview3;
    Integer minuend;
    Integer subtrahend,min,max;
    EditText txt1,txt2,txt3,txt4,txt5;
    double sub_score=0;
    Button button2,buttonn;
    final static String TEST_NAME = "SUBTRACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtraction_main);
        txt1=findViewById(R.id.ans1);
        txt2=findViewById(R.id.ans2);
        txt3=findViewById(R.id.ans3);
        txt4=findViewById(R.id.ans4);
        txt5=findViewById(R.id.ans5);
        textview3=findViewById(R.id.textView3);
        button2=findViewById(R.id.button2);

        ArrayList<Integer> al=new ArrayList<>();
        for(Integer i=1;i<=9;i++)
        {
            al.add(i*100);
        }
        Random random=new Random();
        int index = random.nextInt(al.size());

        minuend = al.get(index);
        min=6;
        max=9;
        ArrayList<Integer> al1=new ArrayList<>();
        al1.add(6);
        al1.add(7);
        al1.add(8);
        al1.add(9);
        index = random.nextInt(al1.size());
        Integer subtrahend = al1.get(index);
        textview3.setText("Subtract "+subtrahend+ " from "+minuend);
        buttonn=findViewById(R.id.button2);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Text one : " + txt1.getText());
                System.out.println(txt2);
                System.out.println(txt3);
                System.out.println(txt4);
                System.out.println(txt5);
                  int count=0;
                  System.out.println("--------------------------------------------------------");
                  if((txt1!=null) && (txt2!=null) && (txt3!=null) && (txt4!=null) && (txt5!=null))
                  {
                      String num1 = txt1.getText().toString();
                      String num2=txt2.getText().toString();
                      String num3=txt3.getText().toString();
                      String num4=txt4.getText().toString();
                      String num5=txt5.getText().toString();
                      Integer val1=Integer.parseInt(num1);
                      Integer val2=Integer.parseInt(num2);
                      Integer val3=Integer.parseInt(num3);
                      Integer val4=Integer.parseInt(num4);
                      Integer val5=Integer.parseInt(num5);
                      if(val1==minuend-subtrahend) count++;
                      System.out.println(minuend-subtrahend);
                      if(val2==minuend-2*subtrahend) count++;
                      System.out.println(minuend-2*subtrahend);

                      if(val3==minuend-3*subtrahend) count++;
                      System.out.println(minuend-3*subtrahend);
                      if(val4==minuend-4*subtrahend) count++;
                      System.out.println(minuend-4*subtrahend);
                      if(val5==minuend-5*subtrahend) count++;
                      System.out.println(minuend-5*subtrahend);
                  }

                 System.out.println(count);
                  System.out.println("-------------------------------------------------------");
                  if(count>3) sub_score=3;
                  else if(count>=2) sub_score=2;
                  else if(count>=1) sub_score=1;
                  else sub_score=0;
                System.out.println(sub_score12);
                 Intent intent = new Intent(SubtractionMain.this, Language_repeatSentence.class);
                 startActivity(intent);
            }
        });


    }

}