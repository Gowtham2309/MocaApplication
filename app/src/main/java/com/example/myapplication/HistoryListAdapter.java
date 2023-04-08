package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HistoryListAdapter extends ArrayAdapter<HistoryListData> {
    final Activity context;
    private ArrayList<HistoryListData> historyData;

    public HistoryListAdapter(@NonNull Activity context, ArrayList<HistoryListData> data) {
        super(context, R.layout.history_list_view);
        this.context = context;
        historyData = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.history_list_view, null, true);

        TextView txtDate, txtPhysician, txtPatient, txtScore;
        txtDate = rowView.findViewById(R.id.historyDate);
        txtPhysician = rowView.findViewById(R.id.historyPhysician);
        txtPatient = rowView.findViewById(R.id.historyPatient);
        txtScore = rowView.findViewById(R.id.historyScore);

        // set the values
        HistoryListData data = historyData.get(position);
        txtDate.setText(data.getDate());
        txtScore.setText(data.getScore());
        txtPhysician.setText(data.getPhysicianName());
        txtPatient.setText(data.getPatientName());

        return rowView;
    }

    @Override
    public int getCount() {
        return historyData.size();
    }
}

class HistoryListData {
    String timestamp, physicianName, patientName, id;
    Date date;
    Map<String, Integer> data;
    HistoryListData(String id, String timestamp, String physicianName, String patientName, Map<String, Integer> data) {
        this.id = id;
        this.timestamp = timestamp;
        this.data = data;
        this.date = new Date(Long.parseLong(timestamp));
        this.physicianName = physicianName;
        this.patientName = patientName;
    }

    HistoryListData(String id, HistoryInstance instance) {
        this.id = id;
        this.timestamp = instance.getTimeStamp();
        this.data = instance.getScores();
        this.date = new Date(Long.parseLong(timestamp));
        this.physicianName = instance.getPhysicianName();
        this.patientName = instance.getPatientName();
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.date);
    }
    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.date);
    }

    public String getPhysicianName() {
        return physicianName;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getScore() {
        long totalScore = 0;
        for(Map.Entry<String, Integer> entry: data.entrySet()) {
            totalScore += entry.getValue();
        }
        return Long.toString(totalScore);
    }

    @Override
    public String toString() {
        return "HistoryListData{" +
                "timestamp='" + timestamp + '\'' +
                ", physicianName='" + physicianName + '\'' +
                ", patientName='" + patientName + '\'' +
                ", date=" + date +
                ", data=" + data +
                '}';
    }
}