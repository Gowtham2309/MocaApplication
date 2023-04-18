package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Cube extends AppCompatActivity {
    ImageView clear, canvas;
    private float float_startX = -1, float_startY = -1, float_endX = -1, float_endY = -1;
    private Paint paint = new Paint();
    private Bitmap bit_map;
    Canvas canva;

    final static String CUBE_URL = "http://kamalrajTen.pythonanywhere.com/cube/";
    final static String TEST_NAME = "CUBE DRAWING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube);

        ActivityCompat.requestPermissions(this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Cube Drawing Test");
        } catch (NullPointerException ignored) {}

        canvas = findViewById(R.id.cubeCanvas);
        clear =findViewById(R.id.cubeClear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canva.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        });
    }

    private void drawPaintSketchImage(){

        if (bit_map == null){
            bit_map = Bitmap.createBitmap(canvas.getWidth(),
                    canvas.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canva = new Canvas(bit_map);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
        }
        canva.drawLine(float_startX,
                float_startY-220,
                float_endX,
                float_endY-220,
                paint);
        canvas.setImageBitmap(bit_map);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float_startX = event.getX();
            float_startY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            float_endX = event.getX();
            float_endY = event.getY();
            drawPaintSketchImage();
            float_startX = event.getX();
            float_startY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            float_endX = event.getX();
            float_endY = event.getY();
            drawPaintSketchImage();
        }
        return super.onTouchEvent(event);
    }

    public void clearCanvas() {
        // Clear the canvas

        canva.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
    public void buttonSaveImage(View view)
    {
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Calendar.getInstance().getTime().toString() + ".png");
        try
        {
            FileOutputStream stream = new FileOutputStream(file);
            bit_map.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
            Utils.uploadImageToStorageAndScore(file, getApplicationContext(), "cube", TEST_NAME, CUBE_URL);
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Intent intent=new Intent(this,gaming.class);
        startActivity(intent);
        finish();
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
            Utils.showInstruction(canvas, "Draw a cube on the screen\n"+
            "Use 'X' button to clear the screen");
            return true;
        }
        return false;
    }
}