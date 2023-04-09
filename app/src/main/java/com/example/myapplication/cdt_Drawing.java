package com.example.myapplication;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class cdt_Drawing extends AppCompatActivity {
    private ImageView imageView,imageView_clr;

    private float float_startX = -1, float_startY = -1,
            float_endX = -1, float_endY = -1;

    private Bitmap bit_map;
    private Canvas canva;
    private Paint paint = new Paint();
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdt_drawing);
        ActivityCompat.requestPermissions(this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Clock Drawing Test");
        } catch (NullPointerException ignored) {}

        imageView = findViewById(R.id.imageView5);
        imageView_clr=findViewById(R.id.imageView4);
        imageView_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canva.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        });
    }

    private void drawPaintSketchImage(){

        if (bit_map == null){
            bit_map = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
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
        imageView.setImageBitmap(bit_map);
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
//        File fileSaveImage = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                Calendar.getInstance().getTime().toString() + ".jpg");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(fileSaveImage);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            Toast.makeText(this,
//                    "File Saved Successfully",
//                    Toast.LENGTH_LONG).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Calendar.getInstance().getTime().toString() + ".png");
        try
        {
            FileOutputStream stream = new FileOutputStream(file);
            bit_map.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Intent intent=new Intent(cdt_Drawing.this,gaming.class);
        startActivity(intent);
        finish();
    }

}