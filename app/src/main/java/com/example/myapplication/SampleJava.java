package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SampleJava extends AppCompatActivity {
    Button button;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_java);

    }

    public void tryRun(View view){
        mediaPlayer= MediaPlayer.create(this,R.raw.audio);
        button=findViewById(R.id.button11);
        try{
            mediaPlayer.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        long audioStartTime = System.currentTimeMillis();
        Log.d("Timestamps", String.valueOf(mediaPlayer.getDuration()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long buttonClickTime = System.currentTimeMillis(); // Record the time when the button was clicked
                long audioTime = mediaPlayer.getCurrentPosition() + audioStartTime; // Calculate the current time of the audiofile
                Log.d("Timestamps", "Audio time: " + audioTime + ", Button click time: " + buttonClickTime+ ", Audio Time: "+ (audioTime-audioStartTime));
            }
        });
    }


}

