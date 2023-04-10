package com.example.myapplication;

import static com.example.myapplication.Attentation_repeatingDigit.RESULT_SPEECH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class Memory_Delayedrecall extends AppCompatActivity {

    String arr[]={"please","sorry", "help", "love","friend", "family", "time", "money", "food",
            "water","face","velvet", "church","daisy", "red","blue", "education"};
    String target_arr[]=new String[5];
    String str_final="";
    String final_arr[];
    String str="";
    String delayed_recall="";
    ArrayList<Integer> al=new ArrayList<>();
    ArrayList<String> al1=new ArrayList<>();
    TextView tv_head,tv_test;
    Button button;
    ImageButton imgclickspeak;
    ScoreMaintainer scoreMaintainer;
    int count=0,score_memory=0,score_delayed_recall=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_delayedrecall);

        scoreMaintainer = ScoreMaintainer.getInstance();

        tv_head=findViewById(R.id.textView16);
        tv_test=findViewById(R.id.textView17);
        button=findViewById(R.id.button);
        imgclickspeak=findViewById(R.id.imageButton);

        // check if first time and load the necessary string
        setTestStrings();

        imgclickspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scoreMaintainer.isFirstDelayedRecall())
                {
                    // next time will be second
                    Intent intent=new Intent(Memory_Delayedrecall.this,Attentation_repeatingDigit.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(Memory_Delayedrecall.this,Orientation.class);
                    startActivity(intent);
                }
                evalvate();
            }
            
        });
        
        

    }

    private void setTestStrings() {
        /*Check for first or second time and return the words to be used*/
        if (scoreMaintainer.isFirstDelayedRecall()) {
            // first time getting into this page
            // generate new words
            try {
                getSupportActionBar().setTitle("MEMORY");
            } catch (NullPointerException ignored) {}
            Random r=new Random();

            for(int i=0;i<5;i++)
            {
                int index = r.nextInt(arr.length-1);
                if(al.contains(index))
                {
                    i--;
                    continue;
                }
                al.add(index);
                target_arr[i]=arr[index];
                al1.add(arr[index]);
                str=str+target_arr[i]+" ";
            }
            str.trim();
            tv_test.setText(str);
            tv_head.setText("Spell the words present below, try to remember them");

            //TODO: save the string when generated
            scoreMaintainer.setDelayedRecallString(str);
        } else {
            // second time - delayed recall
            try {
                getSupportActionBar().setTitle("DELAYED RECALL");
            } catch (NullPointerException ignored) {}
            tv_test.setText("Recall the words that displayed during the memory");

            // string from first time
            //String string = scoreMaintainer.getDelayedRecallString();
            //TODO: load to the correct variable
        }
    }

    public void evalvate() 
    {
        str_final.trim();
        System.out.println(str_final);
        delayed_recall=scoreMaintainer.getDelayedRecallString();
        final_arr=str_final.split(" ");
        System.out.println("-----------------------------------------------------------");

        System.out.println(final_arr);
//        System.out.println(al1);
//        for(int i=0;i<final_arr.length;i++)
//        {
//            System.out.println(final_arr[i]);
//
//            if(al1.contains(final_arr[i].toLowerCase()))
//            {
//               count++;
//            }
//        }
        //TODO: correct it
        if(scoreMaintainer.isFirstDelayedRecall())
        {
            System.out.println(al1);
            for(int i=0;i<final_arr.length;i++)
            {
                System.out.println(final_arr[i]);

                if(al1.contains(final_arr[i].toLowerCase()))
                {
                    count++;
                }
            }
            if(count==5) score_memory=2;
            else if(count>=3) score_memory=1;
            else score_memory=0;
            scoreMaintainer.setFirstDelayedRecall(false);
            scoreMaintainer.updateScore("MEMORY", score_memory);
            System.out.println("SCORE: "+scoreMaintainer.getScore("MEMORY"));
            System.out.println(score_memory);
        }
        else
        {
            ArrayList<String> rl=new ArrayList<>();
            String ra[]=delayed_recall.split(" ");
            scoreMaintainer.setFirstDelayedRecall(true);
            for(int i=0;i<ra.length;i++)
            {
                rl.add(ra[i]);
            }
            System.out.println(rl);
            for(int i=0;i<final_arr.length;i++)
            {
                System.out.println(final_arr[i]);

                if(rl.contains(final_arr[i].toLowerCase()))
                {
                    count++;
                }
            }

          if(count>=5) score_delayed_recall=5;
          else score_delayed_recall=count;

          scoreMaintainer.updateScore("DELAYED RECALL",score_delayed_recall);
          System.out.println("SCORE: "+scoreMaintainer.getScore("DELAYED RECALL"));
          System.out.println(score_delayed_recall);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print(requestCode);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.println(text);
                    str_final=str_final+text.get(0)+" ";

                }
                break;

        }
    }
}