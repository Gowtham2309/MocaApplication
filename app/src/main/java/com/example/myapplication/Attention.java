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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Attention extends AppCompatActivity {
    private int MICROPHONE_PERMISSION_CODE=400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    TextView textview3;
    protected static final int RESULT_SPEECH=1;
    public ImageButton btnSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        Button buttonn;
        textview3=findViewById(R.id.textView);
        btnSpeak=findViewById(R.id.imageButton6);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try{
                    startActivityForResult(intent,RESULT_SPEECH);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        Random rnd = new Random();
        char c = (char) ('a' + rnd.nextInt(26));
        textview3.setText("Name maximum number of words that begins with "+c);
        buttonn=findViewById(R.id.button12);
        JsonArrayRequest jsonArrayRequest;
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Attention.this, Abstraction.class);
                startActivity(intent);
                evalvate();
//                String URL="https://api.dictionaryapi.dev/api/v2/entries/en/="+"Venom";
//                JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            JSONObject jsonObject=response.getJSONObject(0);
//                            String word=jsonObject.getString("word");
//                            System.out.println(word);
//                            }
//                        catch (Exception e)
//                        {
//                            System.out.println(e);
//                        }
//
//                    }
//                },new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//               });

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

        }
    }

    public void evalvate()
    {

        String URL="https://api.dictionaryapi.dev/api/v2/entries/en/"+"venom";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println("_____________________1________________________________________");
                    JSONObject jsonObject=response.getJSONObject(0);
                    String word=jsonObject.getString("word");
                   // String origin=jsonObject.getString("origin");
                    System.out.println("______________________2_______________________________________");
                    System.out.println(word);
                    //System.out.println(origin);
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