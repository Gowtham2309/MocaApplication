package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class History extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedPrefsName), Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString(getString(R.string.login), "");

        ArrayList<HistoryPair> historyData = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Activity activity = this;
        db.collection(phoneNumber).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();
                            historyData.add(new HistoryPair(snapshot.getId(), data));
                        }
                        Collections.reverse(historyData); // reverse chronological order
                        System.out.println("----------------");
                        System.out.println(historyData);

                        System.out.println("Linking the data to the listview");
                        listView = findViewById(R.id.historyListView);
                        HistoryListAdapter listAdapter = new HistoryListAdapter(activity, historyData);
                        listView.setAdapter(listAdapter);
                    }
                });
    }
}