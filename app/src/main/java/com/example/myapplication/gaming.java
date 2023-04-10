package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class gaming extends AppCompatActivity {
    ArrayList<String> ans=new ArrayList<>();
    ArrayList<Button> copyButtonList = new ArrayList<>();
    final static String TEST_NAME = "VISUO-SPATIAL"; // actual category in MoCA
    Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button10,buttonn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Visuospatial");
        } catch (NullPointerException ignored) {}

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
        ArrayList<Button> buttonlist =new ArrayList<>();

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
        copyButtonList.addAll(buttonlist);
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
                System.out.println(ans);
                int score = calculateScore();

                ScoreMaintainer scoreMaintainer = ScoreMaintainer.getInstance();
                scoreMaintainer.updateScore(TEST_NAME, score);
                System.out.println("SCORE : "+scoreMaintainer.getScore(TEST_NAME));

                Intent intent = new Intent(gaming.this, Naming.class);
                startActivity(intent);
                finish();
            }
        });

        // Delay the showing of pop up view, to let the view created first
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Utils.showInstruction(
                        findViewById(R.id.gamingRootLayout),
                        "Press the buttons in the increasing order intertwining number then letters\nExample: 1 -> A -> 2 ..."
                );
            }
        }, 100);
    }

    public void gameButtonPressed(View view) {
        Button btn = (Button) view;
        ans.add(btn.getText().toString());
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
    }

    public void reset(View view) {
        for (Button btn :
                copyButtonList) {
            btn.setVisibility(View.VISIBLE);
            btn.setEnabled(true);
            ans.clear();
        }
    }

    public void reset() {
        for (Button btn :
                copyButtonList) {
            btn.setEnabled(true);
            ans.clear();
        }
    }

    private int calculateScore() {
        String requiredOutput = "1A2B3C4D5E";
        StringBuilder userAnswer = new StringBuilder();
        for (String s:
             ans) {
            userAnswer.append(s);
        }
        System.out.println(requiredOutput);
        System.out.println(userAnswer);
        int correctCount = Utils.lcs(
                requiredOutput, userAnswer.toString(), requiredOutput.length(), userAnswer.length()
        );
        System.out.println(correctCount);
        float allowedErrorPercentage = 0.1f;
        if (((float) correctCount / requiredOutput.length()) + allowedErrorPercentage >= 1.0f) return 1;
        return 0;
    }
}