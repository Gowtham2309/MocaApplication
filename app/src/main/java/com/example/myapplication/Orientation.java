package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Orientation extends AppCompatActivity
{
    SimpleDateFormat ft
            = new SimpleDateFormat("dd-MM-yyyy");

    String str = ft.format(new Date());
    String[] curr_date=str.split("-");
    int Score=0,count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        Spinner spinner_date,spinner_day,spinner_month,spinner_year;
        String [] Day_arr={"Days","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String [] Date_arr={"Date","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        String [] Month_arr={"Month","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November","December"};
        String [] Year_arr={"Year","2022","2023","2024","2025","2026","2027","2028","2029","2030"};
            spinner_date =findViewById(R.id.SpinnerDate);
            spinner_day=findViewById(R.id.SpinnerDay);
            spinner_month=findViewById(R.id.SpinnerMonth);
            spinner_year=findViewById(R.id.SpinnerYear);
            ArrayAdapter<String> day_adapter=new ArrayAdapter<String>(Orientation.this, android.R.layout.simple_spinner_item,Day_arr);
            day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_day.setAdapter(day_adapter);
            spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                    String Day=parent.getItemAtPosition(position).toString();
                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();
                    if(Day.equals(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()))) count++;
                    System.out.println(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
                    System.out.println(count);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter<String> date_adapter=new ArrayAdapter<String>(Orientation.this,android.R.layout.simple_spinner_item,Date_arr);
            date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_date.setAdapter(date_adapter);
            spinner_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String Date=adapterView.getItemAtPosition(i).toString();
                    if(Date.equals(curr_date[0])) count++;

                    System.out.println(count);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter<String> month_adapter=new ArrayAdapter<String>(Orientation.this,android.R.layout.simple_spinner_item,Month_arr);
            month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_month.setAdapter(month_adapter);
            spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String Month=adapterView.getItemAtPosition(i).toString();
                    Integer month=Integer.parseInt(curr_date[1]);
                    if(month.equals(i)) count++;
                    System.out.println(count);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        ArrayAdapter<String> year_adapter=new ArrayAdapter<String>(Orientation.this,android.R.layout.simple_spinner_item,Year_arr);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(year_adapter);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String year=adapterView.getItemAtPosition(i).toString();
                if(year.equals(curr_date[2])) count++;
                System.out.println(count);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
