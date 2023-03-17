package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sample extends AppCompatActivity {
    //MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    public ArrayList<Integer> getAudioFiles(){
        ArrayList<Integer> audioFiles=new ArrayList<>();
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
    public void play(View view)
    {
//        ArrayList<Integer> audioFiles=getAudioFiles();
//        ArrayList<MediaPlayer> mediaPlayers=new ArrayList<>();
//
//        for(int i=0;i<audioFiles.size();i++){
//            MediaPlayer mediaPlayer= MediaPlayer.create(this,audioFiles.get(i));
//            mediaPlayers.add(mediaPlayer);
//        }
//
//        for(int i=0;i<mediaPlayers.size()-1;i++){
//            mediaPlayers.get(i).start(); // start playing the first audio
//            final int j=i+1;
//            mediaPlayers.get(i).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            mediaPlayers.get(j).start(); // start playing the second audio after the delay
//                        }
//                    }, 0); // 10000 milliseconds = 10 seconds
//
//                }
//            });
//            while(mediaPlayers.get(i).isPlaying())
//            {
//                continue;
//            }

//
//        for(int i=0;i<audioFiles.size();i++) {
//            final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio1);
//            final MediaPlayer mediaPlayer2 = MediaPlayer.create(this, R.raw.audio);
//            int duration=mediaPlayer.getDuration();
//            mediaPlayer.start(); // start playing the first audio
//
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            mediaPlayer2.start(); // start playing the second audio after the delay
//                        }
//                    }, ); // 10000 milliseconds = 10 seconds
//                }
//            });
//        }

//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio1);
//
//        int duration1 = mediaPlayer.getDuration(); // duration in milliseconds
//        mediaPlayer.start();
//        new CountDownTimer(duration1, 1000) {
//            public void onFinish() {
//                // When timer is finished
//                mediaPlayer.reset();
//                // Execute your code here
//            }
//
//            public void onTick(long millisUntilFinished) {
//                // millisUntilFinished    The amount of time until finished.
//            }
//        }.start();
//        while(duration1>=0){
//            duration1--;
//        }
//        mediaPlayer.reset();
//        for(int i=0;i<2;i++)
//        {
////            if(mediaPlayer==null)
////            {
////                mediaPlayer=MediaPlayer.create(this,R.raw.audio);
////            }
////            mediaPlayer.start();
////            mediaPlayer.reset();
//        }
//        for(int i=0;i<audioFiles.size();i++) {
//            MediaPlayer mediaPlayer = MediaPlayer.create(this, audioFiles.get(i));
//
//            int duration1 = mediaPlayer.getDuration(); // duration in milliseconds
//            mediaPlayer.start();
//            while(duration1>=0){
//                duration1--;
//            }
//            mediaPlayer.reset();
//        }



//        if(mediaPlayer1==null)
//        {
//            mediaPlayer1=MediaPlayer.create(this,R.raw.audio1);
//        }
//        mediaPlayer1.start();

    }
    public void playing(View v)
    {

        String URL="https://api.dictionaryapi.dev/api/v2/entries/en/"+"venom";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println("_____________________1________________________________________");
                    JSONObject jsonObject=response.getJSONObject(0);
                    String word=jsonObject.getString("word");
                    System.out.println("______________________2_______________________________________");
                    System.out.println(word);
                }
                catch (Exception e)
                {
                    System.out.println("_____________________________________________________________");
                    System.out.println(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}