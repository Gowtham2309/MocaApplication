package com.example.myapplication;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class combined_btn_repatingdigit extends AppCompatActivity {
    ImageButton btn;
    private boolean isResume,isResume1;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined_btn_repatingdigit);
        btn=findViewById(R.id.button7);

        if(mediaPlayer==null)
        {
            mediaPlayer=MediaPlayer.create(this,R.raw.audio);
        }
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isResume)
                {
                    mediaPlayer.start();
                    isResume=true;
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
                }
                else
                {
                    mediaPlayer.pause();
                    isResume=false;
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                }
            }
        });
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!isResume1)
//                {
//                    try{
//                        mediaRecorder= new MediaRecorder();
//                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                        mediaRecorder.setOutputFile(RecordingFilePath());
//                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        mediaRecorder.prepare();
//                        mediaRecorder.start();
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                    btn1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_stop_24));
//                }
//                else
//                {
//                    isResume1=false;
//                    mediaRecorder.stop();
//                    mediaRecorder.release();
//                    mediaRecorder=null;
//                    btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_mic_24));
//                }
//            }
//        });
    }
}