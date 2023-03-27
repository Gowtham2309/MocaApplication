package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HistoryListAdapter extends ArrayAdapter<HistoryPair> {
    final Activity context;
    private ArrayList<HistoryPair> historyData;

    public HistoryListAdapter(@NonNull Activity context, ArrayList<HistoryPair> data) {
        super(context, R.layout.history_list_view);
        this.context = context;
        historyData = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.history_list_view, null, true);

        TextView txtDate, txtTime, txtScore;
        txtDate = rowView.findViewById(R.id.historyDate);
        txtTime = rowView.findViewById(R.id.historyTime);
        txtScore = rowView.findViewById(R.id.historyScore);

        // set the values
        HistoryPair pair = historyData.get(position);
        txtDate.setText(pair.getDate());
        txtTime.setText(pair.getTime());
        txtScore.setText(pair.getScore());

        return rowView;
    }

    @Override
    public int getCount() {
        return historyData.size();
    }
}

class HistoryPair {
    String timestamp;
    Date date;
    Map<String, Object> data;
    HistoryPair(String timestamp, Map<String, Object> data) {
        this.timestamp = timestamp;
        this.data = data;
        this.date = new Date(Long.parseLong(timestamp));
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.date);
    }
    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.date);
    }
    public String getScore() {
        long totalScore = 0;
        for(Map.Entry<String, Object> entry: data.entrySet()) {
            totalScore += (Long) entry.getValue();
        }
        return Long.toString(totalScore);
    }

    @NonNull
    @Override
    public String toString() {
        return "ts: "+timestamp+" data: "+data;
    }
}