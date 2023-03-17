package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Language_repeatSentence extends AppCompatActivity {
    private int MICROPHONE_PERMISSION_CODE=400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button buttonn;
    protected static final int RESULT_SPEECH=1,RESULT_SPEECH1=2;
    public ImageButton btnSpeak,btnSpeak1;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_repeat_sentence);
        buttonn=findViewById(R.id.button22);
        btnSpeak=findViewById(R.id.imageButton2);
        btnSpeak1=findViewById(R.id.imageButton3);
        tvText=findViewById(R.id.textView12);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try{
                    startActivityForResult(intent,RESULT_SPEECH);
                    tvText.setText("");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        btnSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try{
                    startActivityForResult(intent,RESULT_SPEECH1);
                    tvText.setText("");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Language_repeatSentence.this, Attention.class);
                startActivity(intent);
            }
        });
    }

    public void playing(View v)
    {

        if(mediaPlayer==null)
        {
            mediaPlayer=MediaPlayer.create(this,R.raw.audio);
        }
        mediaPlayer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print(requestCode);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvText.setText(text.get(0));
                    System.out.print(requestCode);

                }
                break;
            case RESULT_SPEECH1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.print(requestCode);
                    tvText.setText(text.get(0));

                }
                break;
        }
    }

}