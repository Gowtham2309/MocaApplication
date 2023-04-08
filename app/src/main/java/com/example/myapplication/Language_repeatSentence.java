package com.example.myapplication;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Language_repeatSentence extends AppCompatActivity {
    private int MICROPHONE_PERMISSION_CODE=400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button buttonn;
    protected static final int RESULT_SPEECH=1,RESULT_SPEECH1=2;
    public ImageButton btnSpeak,btnSpeak1;
    private TextView tvText;
    List<Task<Uri>> allAudios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_repeat_sentence);
        buttonn=findViewById(R.id.button22);
        btnSpeak=findViewById(R.id.imageButton2);
        btnSpeak1=findViewById(R.id.imageButton3);
        tvText=findViewById(R.id.textView12);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference audioRef = storageRef.child("1");

        allAudios=new ArrayList<>();
//        StorageReference tryAud=storageRef.child("1/forward_order.mp3");
        audioRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        // Get a list of all the items in the folder
                        List<StorageReference> audios = listResult.getItems();
                        System.out.println("-----SIZE----- = "+audios.size());
                        // Do something with the list of items
                        for (int i=0;i<audios.size();i++) {//StorageReference item : items) {
                            StorageReference item=audios.get(i);
                            System.out.println(item.getDownloadUrl());
                            allAudios.add(item.getDownloadUrl());

                            // Get the download URL of the image
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        System.out.println("Error listing items in folder: " + e.getMessage());
                    }
                });





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
                finish();
            }
        });
    }

    public void playing(View v)
    {

        if(mediaPlayer==null)
        {
//            mediaPlayer=MediaPlayer.create(, );
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(String.valueOf(allAudios.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaPlayer.start();
    }


    public void playing1(View v)
    {

        if(mediaPlayer==null)
        {
//            mediaPlayer=MediaPlayer.create(, );
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(String.valueOf(allAudios.get(1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
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