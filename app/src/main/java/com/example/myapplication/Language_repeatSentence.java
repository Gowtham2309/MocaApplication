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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Language_repeatSentence extends AppCompatActivity {
    private int MICROPHONE_PERMISSION_CODE=400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer,mediaPlayer1;
    Button buttonn;
    Button button1;
    Button button2;
    String str_arr1[],str_arr2[];
    protected static final int RESULT_SPEECH=1,RESULT_SPEECH1=2;
    public ImageButton btnSpeak,btnSpeak1;
    private TextView tvText;
    ArrayList<Integer> al1=new ArrayList<>();
    ArrayList<Integer> alc=new ArrayList<>();
    ArrayList<String> string=new ArrayList<>();
    int score=0;
    final static String TEST_NAME="REPEAT SENTENCE";

//    List<Task<Uri>> allAudios;
//
//
//    List<String> audioFileName=new ArrayList<>();
//
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//    String set=getAudioSet();
//    StorageReference audioRef = storageRef.child(set);
//
//    public void playAllAudio(){
//        System.out.println(audioFileName.size());
//        StorageReference audioRef1 = storageRef.child(set+"/"+audioFileName.get(0));
//        StorageReference audioRef2 = storageRef.child(set+"/"+audioFileName.get(1));
//        File localFile = null;
//        File localFile1=null;
//        try {
//            localFile = File.createTempFile("audio", "mp3");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            localFile1 = File.createTempFile("audio1", "mp3");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//// Download the audio file to the local file
//
//        File finalLocalFile = localFile;
//        File finalLocalFile1=localFile1;
//
//        button1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                audioRef1.getFile(finalLocalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // File downloaded successfully
//                        // Now you can play the audio file from the local file
//                        playAudio(finalLocalFile.getAbsolutePath());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any errors that occur during file download
//                        System.out.println("Error downloading audio: " + exception.getMessage());
//                    }
//                });
//            }
//        });
//
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                audioRef2.getFile(finalLocalFile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // File downloaded successfully
//                        // Now you can play the audio file from the local file
//                        playAudio(finalLocalFile1.getAbsolutePath());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any errors that occur during file download
//                        System.out.println("Error downloading audio: " + exception.getMessage());
//                    }
//                });
//            }
//        });
//
//    }
//
//    public void getAudioPath(StorageReference audioRef){
//        audioRef.listAll()
//                .addOnSuccessListener(listResult -> {
//                    for (StorageReference item : listResult.getItems()) {
//                        String fileName = item.getName();
//                        audioFileName.add(fileName);
//                    }
//                    playAllAudio();
//                })
//                .addOnFailureListener(exception -> {
//                    System.out.println("Error getting list of files: " + exception.getMessage());
//                });
//    }
//
//    public String getAudioSet(){
//        int min = 1;
//        int max = 2;
//        Random random = new Random();
//        int randomNumber = random.nextInt(max - min + 1) + min;
//        System.out.println(String.valueOf(randomNumber));
//        return String.valueOf(randomNumber);
//    }
//
//    public void playAudio(String filePath) {
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(filePath);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            System.out.println("Error playing audio: " + e.getMessage());
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            getSupportActionBar().setTitle("Repeat Sentence");
        } catch (NullPointerException ignored) {}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_repeat_sentence);
        buttonn=findViewById(R.id.button22);
        btnSpeak=findViewById(R.id.imageButton2);
        btnSpeak1=findViewById(R.id.imageButton3);
        tvText=findViewById(R.id.textView12);
        al1.add(R.raw.rs1);
        al1.add(R.raw.rs2);
        al1.add(R.raw.rs3);
        al1.add(R.raw.rs4);
        string.add("the bird flies high in the sky");
        string.add("the flowers bloom in springtime");
        string.add("the cat chased a mouse");
        string.add("the sun sets in the west");
        int min = 0;
       int max = 3;
       Random random = new Random();
       int randomNumber = random.nextInt(max - min + 1) + min;
       while (alc.size()<2)
       {
           if(!alc.contains(randomNumber))
           {
               alc.add(randomNumber);
           }
           randomNumber = random.nextInt(max - min + 1) + min;
       }


//        button1=findViewById(R.id.button16);
//        button2=findViewById(R.id.button19);
//        getAudioPath(audioRef);
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference audioRef = storageRef.child("1");
//
//        allAudios=new ArrayList<>();
////        StorageReference tryAud=storageRef.child("1/forward_order.mp3");
//        audioRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        // Get a list of all the items in the folder
//                        List<StorageReference> audios = listResult.getItems();
//                        System.out.println("-----SIZE----- = "+audios.size());
//                        // Do something with the list of items
//                        for (int i=0;i<audios.size();i++) {//StorageReference item : items) {
//                            StorageReference item=audios.get(i);
//                            System.out.println(item.getDownloadUrl());
//                            allAudios.add(item.getDownloadUrl());
//
//                            // Get the download URL of the image
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle any errors
//                        System.out.println("Error listing items in folder: " + e.getMessage());
//                    }
//                });





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
                String ans_arr1[]=string.get(alc.get(0)).split(" ");
                String ans_arr2[]=string.get(alc.get(1)).split(" ");
                HashMap<String,Integer> anshm1=new HashMap<>();
                HashMap<String,Integer> anshm2=new HashMap<>();
                HashMap<String,Integer> strhm1=new HashMap<>();
                HashMap<String,Integer> strhm2=new HashMap<>();
                int count=0;
                for(int i=0;i<ans_arr1.length;i++){
                    if(str_arr1.length>=i+1 && (ans_arr1[i].toLowerCase()).equals(str_arr1[i].toLowerCase())){
                        count++;
                    }
                }

                if(Math.abs(count-ans_arr1.length)<=2){
                    score+=1;
                }
                count=0;
                for(int i=0;i<ans_arr2.length;i++){
                    if(str_arr2.length>=i+1 && (ans_arr2[i].toLowerCase()).equals(str_arr2[i].toLowerCase())){
                        count++;
                    }
                }

                if(Math.abs(count-ans_arr2.length)<=2){
                    score+=1;
                }
                System.out.println("------------------------------------------");
                System.out.println(score);
                Intent intent = new Intent(Language_repeatSentence.this, Attention.class);
                startActivity(intent);
                ScoreMaintainer scoreMaintainer = ScoreMaintainer.getInstance();
                scoreMaintainer.updateScore(TEST_NAME, score);
                finish();
            }
        });
    }

    public void playing(View v)
    {
       //System.out.println(alc);
        if(mediaPlayer==null)
        {
            mediaPlayer=MediaPlayer.create(this,al1.get(alc.get(0)));

        }
        mediaPlayer.start();
    }


    public void playing1(View v)
    {

        if(mediaPlayer1==null)
        {
            mediaPlayer1=MediaPlayer.create(this,al1.get(alc.get(1)) );

        }
        mediaPlayer1.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print(requestCode);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   //hi there hello you -> hi there ... in 0th
                    String a=text.get(0);
                    //tvText.setText(text.get(0));
                    str_arr1=a.split(" ");
                    for(int i=0;i<str_arr1.length;i++)
                        System.out.println(str_arr1[i]);
                    //System.out.print(a);

                }
                break;
            case RESULT_SPEECH1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.print(requestCode);
                    String b=text.get(0);
                    str_arr2=b.split(" ");
                    tvText.setText(text.get(0));

                }
                break;
        }
    }




}