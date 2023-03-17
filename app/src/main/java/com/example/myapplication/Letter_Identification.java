package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class Letter_Identification extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    TextView textview2;
    Button buttonn;
    ArrayList<Character> letter=new ArrayList<>();
    ArrayList<Long> duration_list=new ArrayList<>();

    ArrayList<Long> button_click_time=new ArrayList<>();
    char c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_identification);
        textview2=findViewById(R.id.textView2);
        letter.add('a');
        letter.add('b');
        letter.add('c');
        letter.add('d');
        letter.add('e');
        letter.add('f');
        letter.add('g');
        letter.add('h');
        letter.add('i');
        letter.add('j');
        letter.add('k');
        letter.add('l');
        letter.add('m');
        letter.add('n');
        Random rnd = new Random();
        int index = rnd.nextInt(letter.size());
        buttonn=findViewById(R.id.button17);
        c=letter.get(index);
        textview2.setText("Hit the SPACEBAR when letter "+c+" occurs");
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Letter_Identification.this, SubtractionMain.class);
                startActivity(intent);
                System.out.println(button_click_time);
            }
        });
    }
    public ArrayList<Integer> getAudioFiles() {
        ArrayList<Integer> audioFiles = new ArrayList<>();
        audioFiles.add(R.raw.amoca);
        audioFiles.add(R.raw.bmoca);
        audioFiles.add(R.raw.cmoca);
        audioFiles.add(R.raw.dmoca);
        audioFiles.add(R.raw.emoca);
        audioFiles.add(R.raw.fmoca);
        audioFiles.add(R.raw.gmoca);
        audioFiles.add(R.raw.hmoca);
        audioFiles.add(R.raw.imoca);
        audioFiles.add(R.raw.jmoca);
        audioFiles.add(R.raw.kmoca);
        audioFiles.add(R.raw.lmoca);
        audioFiles.add(R.raw.mmoca);
        audioFiles.add(R.raw.nmoca);

        return audioFiles;
    }
    public void press(View v)
    {
        long d=System.currentTimeMillis();
        button_click_time.add(d);
    }
    public void play(View v)
    {
        ArrayList<Integer> audioFiles = getAudioFiles();
        ArrayList<MediaPlayer> mediaPlayers = new ArrayList<>();
        ArrayList<Integer> play_list=new ArrayList<>();

        int target_letter=c%97;
        Random rnd = new Random();
        for(int i=1;i<=15;i++)
        {
            int ind= rnd.nextInt(letter.size());
            if(i%5==0) play_list.add(target_letter);
            else
            {
                play_list.add(ind);
            }
        }
        System.out.println(play_list);
        System.out.println(target_letter);

//        letter.add('a');
//        letter.add('b');
//        letter.add('c');
//        letter.add('d');
//        letter.add('e');
//        letter.add('f');
//        letter.add('g');
//        letter.add('j');
//        letter.add('k');
//        letter.add('l');
//        letter.add('m');
//        letter.add('n');


        for (int i = 0; i < audioFiles.size(); i++) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, audioFiles.get(i));
            mediaPlayers.add(mediaPlayer);
        }
        for(int i=0;i<play_list.size();i++)
        {
            int x=play_list.get(i);
            mediaPlayers.get(x).start();
            long d=System.currentTimeMillis();
            duration_list.add(d);
            while(mediaPlayers.get(x).isPlaying())
            {
                continue;
            }
            //mediaPlayers.get(x).reset();
           try{
              Thread.sleep(500);

          }
          catch (Exception e)
           {
                System.out.println(e);
          }
            System.out.println(i);
        }
        System.out.println(duration_list);
        audioFiles.clear();
        play_list.clear();
        mediaPlayers.clear();

        duration_list.clear();
    }
}
