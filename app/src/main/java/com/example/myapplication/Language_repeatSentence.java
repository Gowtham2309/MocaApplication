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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Language_repeatSentence extends AppCompatActivity {
    private int MICROPHONE_PERMISSION_CODE=400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button buttonn;
    Button button1;
    Button button2;
    protected static final int RESULT_SPEECH=1,RESULT_SPEECH1=2;
    public ImageButton btnSpeak,btnSpeak1;
    private TextView tvText;
    List<Task<Uri>> allAudios;


    List<String> audioFileName=new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String set=getAudioSet();
    StorageReference audioRef = storageRef.child(set);

    public void playAllAudio(){
        System.out.println(audioFileName.size());
        StorageReference audioRef1 = storageRef.child(set+"/"+audioFileName.get(0));
        StorageReference audioRef2 = storageRef.child(set+"/"+audioFileName.get(1));
        File localFile = null;
        File localFile1=null;
        try {
            localFile = File.createTempFile("audio", "mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            localFile1 = File.createTempFile("audio1", "mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

// Download the audio file to the local file

        File finalLocalFile = localFile;
        File finalLocalFile1=localFile1;

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                audioRef1.getFile(finalLocalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // File downloaded successfully
                        // Now you can play the audio file from the local file
                        playAudio(finalLocalFile.getAbsolutePath());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors that occur during file download
                        System.out.println("Error downloading audio: " + exception.getMessage());
                    }
                });
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                audioRef2.getFile(finalLocalFile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // File downloaded successfully
                        // Now you can play the audio file from the local file
                        playAudio(finalLocalFile1.getAbsolutePath());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors that occur during file download
                        System.out.println("Error downloading audio: " + exception.getMessage());
                    }
                });
            }
        });

    }

    public void getAudioPath(StorageReference audioRef){
        audioRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        String fileName = item.getName();
                        audioFileName.add(fileName);
                    }
                    playAllAudio();
                })
                .addOnFailureListener(exception -> {
                    System.out.println("Error getting list of files: " + exception.getMessage());
                });
    }

    public String getAudioSet(){
        int min = 1;
        int max = 2;
        Random random = new Random();
        int randomNumber = random.nextInt(max - min + 1) + min;
        System.out.println(String.valueOf(randomNumber));
        return String.valueOf(randomNumber);
    }

    public void playAudio(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            System.out.println("Error playing audio: " + e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_repeat_sentence);
        buttonn=findViewById(R.id.button22);
        btnSpeak=findViewById(R.id.imageButton2);
        btnSpeak1=findViewById(R.id.imageButton3);
        tvText=findViewById(R.id.textView12);

        button1=findViewById(R.id.button16);
        button2=findViewById(R.id.button19);
        getAudioPath(audioRef);

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