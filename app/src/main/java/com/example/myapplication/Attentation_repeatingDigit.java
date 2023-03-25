package com.example.myapplication;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class Attentation_repeatingDigit extends AppCompatActivity
{
    private int MICROPHONE_PERMISSION_CODE = 400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    protected static final int RESULT_SPEECH = 1, RESULT_SPEECH1 = 2;
    public ImageButton btnSpeak, btnSpeak1;
    Button buttonn, btnPlayOne, btnPlayTwo;
    ArrayList<Integer> fwd=new ArrayList<>();
    ArrayList<Integer> rev=new ArrayList<>();
    //TextView tv1;
    //TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attentation_repeating_digit);
        btnSpeak = findViewById(R.id.imageButton4);
        btnSpeak1 = findViewById(R.id.imageButton5);
        btnPlayOne = findViewById(R.id.button4);
        btnPlayTwo = findViewById(R.id.button10);
        //tv1=findViewById(R.id.first);
        //tv2=findViewById(R.id.second);
        /*if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }*/

        /*//
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();
        PyObject pyobj= py.getModule("main");

        PyObject obj2=pyobj.callAttr("startmain",f2);
        //PyObject obj=pyobj.callAttr("func");
        //tv.setText(obj1.toString()); // textview
        System.out.println(obj1.toString());
        System.out.println(obj2.toString());
        */
        //
        btnSpeak.setOnClickListener(new View.OnClickListener() {
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
        btnSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buttonn = findViewById(R.id.button5);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Attentation_repeatingDigit.this, Letter_Identification.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print(requestCode);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                }
                break;
            case RESULT_SPEECH1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                }
                break;
        }
    }

    public ArrayList<Integer> getAudioFiles() {
        ArrayList<Integer> audioFiles = new ArrayList<>();
        audioFiles.add(R.raw.onemoca);
        audioFiles.add(R.raw.twomoca);
        audioFiles.add(R.raw.threemoca);
        audioFiles.add(R.raw.fourmoca);
        audioFiles.add(R.raw.fivemoca);
        audioFiles.add(R.raw.sixmoca);
        audioFiles.add(R.raw.sevenmoca);
        audioFiles.add(R.raw.eightmoca);
        audioFiles.add(R.raw.ninemoca);
        return audioFiles;
    }
    public void play1(View v) {
        btnPlayOne.setEnabled(false);
        btnSpeak.setEnabled(false);
        btnSpeak1.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                rev.clear();
                ArrayList<Integer> audioFiles = getAudioFiles();
                ArrayList<MediaPlayer> mediaPlayers = new ArrayList<>();


                for (int i = 0; i < audioFiles.size(); i++) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), audioFiles.get(i));
                    mediaPlayers.add(mediaPlayer);
                }
                Random random=new Random();
                for(int i=0;i<3;i++)
                {

                    int index = random.nextInt(audioFiles.size());
                    while(rev.contains(index))
                    {
                        index = random.nextInt(audioFiles.size());
                    }
                    rev.add(index);
                }
                for (int i = 0; i < rev.size(); i++) {
                    int x=rev.get(i);
                    mediaPlayers.get(x).start();
                    while(mediaPlayers.get(x).isPlaying())
                    {
                        continue;
                    }
                    try{
                        Thread.sleep(500);

                    }
                    catch (Exception e)
                    {
                        System.out.println(e);
                    }
                    //mediaPlayers.get(x).reset();
                }
                audioFiles.clear();
                mediaPlayers.clear();

                runOnUiThread(() -> {
                    btnPlayOne.setEnabled(true);
                    btnSpeak.setEnabled(true);
                    btnSpeak1.setEnabled(true);
                });
            }
        }).start();
    }
    public void play(View v) {
        btnPlayTwo.setEnabled(false);
        btnSpeak.setEnabled(false);
        btnSpeak1.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                fwd.clear();
                ArrayList<Integer> audioFiles = getAudioFiles();
                ArrayList<MediaPlayer> mediaPlayers = new ArrayList<>();


                for (int i = 0; i < audioFiles.size(); i++) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), audioFiles.get(i));
                    mediaPlayers.add(mediaPlayer);
                }
                Random random=new Random();
                for(int i=0;i<5;i++)
                {

                    int index = random.nextInt(audioFiles.size());
                    while(fwd.contains(index))
                    {
                        index = random.nextInt(audioFiles.size());
                    }
                    fwd.add(index);
                }
                for (int i = 0; i < fwd.size(); i++) {
                    int x=fwd.get(i);

                    mediaPlayers.get(x).start();
                    while(mediaPlayers.get(x).isPlaying())
                    {
                        continue;
                    }
                    try{
                        Thread.sleep(500);

                    }
                    catch (Exception e)
                    {
                        System.out.println(e);
                    }
                    mediaPlayers.get(x).reset();
                }
                audioFiles.clear();
                mediaPlayers.clear();

                runOnUiThread(() -> {
                    btnPlayTwo.setEnabled(true);
                    btnSpeak.setEnabled(true);
                    btnSpeak1.setEnabled(true);
                });
            }
        }).start();

    }
}