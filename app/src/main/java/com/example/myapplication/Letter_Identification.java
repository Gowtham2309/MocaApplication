package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLOutput;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Letter_Identification extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    TextView textview2;
    Button buttonn, btnHit, btnPlay;
    ArrayList<Character> letter=new ArrayList<>();
    ArrayList<Long> duration_list=new ArrayList<>();
    ArrayList<Long> button_click_time=new ArrayList<>();
    char charToIdentify;
    final static String TEST_NAME = "LETTER IDENTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_identification);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Letter identification");
        } catch (NullPointerException ignored) {}


        textview2=findViewById(R.id.textView2);
        buttonn=findViewById(R.id.button17);
        btnHit = findViewById(R.id.button18);
        btnPlay = findViewById(R.id.button14);

        // Add the available letters to the letter list
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

        // Select the letter to be identified
        Random rnd = new Random();
        int index = rnd.nextInt(letter.size());
        charToIdentify=letter.get(index);
        textview2.setText(String.format("Press the 'Hit' button when letter %s occurs", charToIdentify));

        // Onclick listeners
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Letter_Identification.this, SubtractionMain.class);
                startActivity(intent);
                System.out.println(button_click_time);
                finish();
            }
        });

        btnPlay.setOnClickListener(view -> {
            // disable the button. At the end of the play function will be re-enabled
            btnPlay.setEnabled(false);
            // clearing both lists which tracks the clicks
            duration_list.clear();
            button_click_time.clear();
            // run the play function in a separate thread
            new Thread(() -> play(view)).start();
        });
        btnHit.setOnClickListener(this::press);
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

        int target_letter = charToIdentify % 97;
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

            // add only when the target letter is spelled
            if (x == target_letter) {
                long d = System.currentTimeMillis();
                duration_list.add(d);
            }
            while(mediaPlayers.get(x).isPlaying()) {} // wait until the media player completes
            //mediaPlayers.get(x).reset();

            // sleep to give time before the next sound plays
            try{
               Thread.sleep(500);
            }
            catch (Exception e)
            {
               System.out.println(e);
            }
            System.out.println(i);
        }
        System.out.println("DURATION LIST "+duration_list);
        System.out.println("HIT CLICK LIST "+button_click_time);
        audioFiles.clear();
        play_list.clear();
        mediaPlayers.clear();

        // calculate the score
        int score = calculateScore(duration_list, button_click_time);
        System.out.println("SCORE: "+score);
        // update the score in the ScoreMaintainer
        ScoreMaintainer scoreMaintainer = ScoreMaintainer.getInstance();
        scoreMaintainer.updateScore(TEST_NAME, score);
        System.out.println("SCORE: "+scoreMaintainer.getScore(TEST_NAME));

//        SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedPrefsName), Context.MODE_PRIVATE);
//        scoreMaintainer.uploadToFirebase( prefs.getString(getString(R.string.login), "") );

        runOnUiThread(() -> btnPlay.setEnabled(true));
    }

    private int calculateScore(ArrayList<Long> charList, ArrayList<Long> clickList) {
        // charList - list of time when the target char is spelled
        // clickList - list of time when the hit button is pressed
        float errorOnClickCount = (float) Math.abs(charList.size() - clickList.size()) / charList.size();
        // 1. map the time in charList to time in clickList based on min time difference
        HashMap<Long, Long> pairs = new HashMap<>();
        for (long charSpellTime: charList) {
            if (clickList.size() == 0) break; // when clickList size < charList size
            long pair = clickList.get(0);
            for (long clickTime: clickList) {
                if (Math.abs(charSpellTime - pair) > Math.abs(charSpellTime - clickTime))
                    pair = clickTime;
            }
            pairs.put(charSpellTime, pair);
            clickList.remove(pair); // to avoid duplication
        }
        // 2. based on extras and time difference calculate the score
        float diffAllowedMilliSeconds = 1.3f;
        int correctCount = 0;
        for (Map.Entry<Long, Long> entry: pairs.entrySet()) {
            float diff = (float) Math.abs(new Date(entry.getValue()).getTime() - new Date(entry.getKey()).getTime()) / 1000;
            System.out.println(diff);
            if ( diff <= diffAllowedMilliSeconds)
                correctCount++;
        }
        float errorOnClickTime = 1 - ((float) correctCount / charList.size());

        System.out.println("click time error : "+errorOnClickTime);
        System.out.println("click count error : "+errorOnClickCount);

        float ALLOWED_ERROR_PERCENTAGE = 0.2f;
        if ((errorOnClickTime + errorOnClickCount) <= ALLOWED_ERROR_PERCENTAGE) return 1;

        return 0;
    }
}
