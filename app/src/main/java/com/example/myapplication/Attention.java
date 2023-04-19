package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Attention extends AppCompatActivity {
    private int MICROPHONE_PERMISSION_CODE=400;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    TextView textview3;
    protected static final int RESULT_SPEECH=1;
    public ImageButton btnSpeak;
    int count=0, completedCount, totalCount;
    int Score=0;
    ArrayList<String> text;
    String [] str_arr;
    String str_final="";
    final static String TEST_NAME="FLUENCY";
    char c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);

        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("FLUENCY");
        } catch (NullPointerException ignored) {}

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
        c = (char) ('a' + rnd.nextInt(26));
        textview3.setText(String.format("Name maximum number of words that begins with '%s'", c));
        buttonn=findViewById(R.id.button12);
        JsonArrayRequest jsonArrayRequest;
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Attention.this, Abstraction.class);
                startActivity(intent);
                str_final = str_final.trim();
                System.out.println(str_final);
                str_arr=str_final.split(" ");
                str_arr = removeDuplicate(str_arr);

                for (String s : str_arr) {
                    System.out.println(s);
                    if (s.charAt(0) == c) {
                        count++;
                    }
                }

                if(count>=11) Score=1;
                ScoreMaintainer scoreMaintainer = ScoreMaintainer.getInstance();
                scoreMaintainer.updateScore(TEST_NAME, Score);
                System.out.println("SCORE: "+scoreMaintainer.getScore(TEST_NAME));

                finish();
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
                    str_final=str_final+text.get(0)+" ";
                }
                break;

        }
    }

    private String[] removeDuplicate(String[] words) {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, words);
        return set.toArray(new String[]{});
    }

    public void evalvate(String str) {

        String URL = "https://api.dictionaryapi.dev/api/v2/entries/en/" + str;
        System.out.println(str);
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println("_____________________1________________________________________");
                    JSONObject jsonObject = response.getJSONObject(0);
                    String word = jsonObject.getString("word");
                    // String origin=jsonObject.getString("origin");
                    System.out.println("______________________2_______________________________________");
                    System.out.println(word);
                    count++;
                    System.out.println(count);

                    //System.out.println(origin);
                    completedCount++;
                } catch (Exception e) {
                    System.out.println("_____________________________________________________________");
                    System.out.println(e);
                    System.out.println("________________________________________________________________________");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("________________________________________________________________________");

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help) {
            Utils.showInstruction(
                    btnSpeak,
                    String.format(
                            "Click the mic button and tell the maximum number of words beginning with %s\n\n" +
                                    "Better tell single word for each mic clicks",
                            c
                    )
            );
            return true;
        }
        return false;
    }
}