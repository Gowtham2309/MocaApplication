package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class gaming extends AppCompatActivity {
    ArrayList<String> ans=new ArrayList<>();
    Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button10,buttonn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        button1=findViewById(R.id.button26);
        button2=findViewById(R.id.button27);
        button3=findViewById(R.id.button28);
        button4=findViewById(R.id.button29);
        button5=findViewById(R.id.button30);
        button6=findViewById(R.id.button31);
        button7=findViewById(R.id.button32);
        button8=findViewById(R.id.button34);
        button9=findViewById(R.id.button35);
        button10=findViewById(R.id.button25);
        buttonn=findViewById(R.id.button33);
        ArrayList<Button> buttonlist=new ArrayList<>();

        ArrayList reflist=new ArrayList();
        buttonlist.add(button1);
        buttonlist.add(button2);
        buttonlist.add(button3);
        buttonlist.add(button4);
        buttonlist.add(button5);
        buttonlist.add(button6);
        buttonlist.add(button7);
        buttonlist.add(button8);
        buttonlist.add(button9);
        buttonlist.add(button10);
        int min=1,max=5,count=0;
        while(count<5)
        {
            int val = (int)(Math.random()*(max-min+1)+min);

            String str=Integer.toString(val);
            if(!reflist.contains(val))
            {
               buttonlist.get(buttonlist.size()-1).setText(str);
               buttonlist.remove(buttonlist.size()-1);
               reflist.add(val);
               count++;
            }

        }
        count = 0;
        while(count<5)
        {
            int val = (int)(Math.random()*(max-min+1)+min);
            char c=(char) (val+64);
            String str = Character.toString(c);
            if(!reflist.contains(c))
            {
                System.out.println(count+" "+buttonlist.size());
                buttonlist.get(buttonlist.size()-1).setText(str);
                buttonlist.remove(buttonlist.size()-1);
                reflist.add(c);
                count++;
            }

        }
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gaming.this, Naming.class);
                startActivity(intent);
            }
        });
    }
    public void btnPrssed1(View view)
    {
        String str=button1.getText().toString();
        ans.add(str);
    }
    public void btnPrssed2(View view)
    {
        String str=button2.getText().toString();
        ans.add(str);
    }
    public void btnPrssed3(View view)
    {
        String str=button3.getText().toString();
        ans.add(str);
    }
    public void btnPrssed4(View view)
    {
        String str=button4.getText().toString();
        ans.add(str);
    }
    public void btnPrssed5(View view)
    {
        String str=button5.getText().toString();
        ans.add(str);
    }
    public void btnPrssed6(View view)
    {
        String str=button6.getText().toString();
        ans.add(str);
    }
    public void btnPrssed7(View view)
    {
        String str=button7.getText().toString();
        ans.add(str);
    }
    public void btnPrssed8(View view)
    {
        String str=button8.getText().toString();
        ans.add(str);
    }
    public void btnPrssed9(View view)
    {
        String str=button9.getText().toString();
        ans.add(str);
    }
    public void btnPrssed10(View view)
    {
        String str=button10.getText().toString();
        ans.add(str);
    }
    public void display(View view)
    {
        System.out.println(ans);
    }
}