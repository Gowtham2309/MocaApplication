package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView txtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        txtNoData = findViewById(R.id.historyNoData);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("History");
        } catch (NullPointerException ignored) {}

        load();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        load();
    }

    private void load() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedPrefsName), Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString(getString(R.string.login), "");

        ArrayList<HistoryListData> historyData = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Activity activity = this;

        // Changing the firebase backend
        db.collection("MoCA").whereEqualTo("phoneNumber", phoneNumber).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                            HistoryInstance instance = snapshot.toObject(HistoryInstance.class);
                            System.out.println(instance);
                            historyData.add(new HistoryListData(
                                    snapshot.getId(),
                                    instance
                            ));
                        }

                        if (historyData.isEmpty()) {
                            txtNoData.setText("No history data found, try taking the test");
                        }

                        System.out.println("Linking the data to the listview");
                        listView = findViewById(R.id.historyListView);
                        HistoryListAdapter listAdapter = new HistoryListAdapter(activity, historyData);
                        listView.setAdapter(listAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Intent intent = new Intent(activity, ExpandedHistory.class);
                                intent.putExtra(ExpandedHistory.DOCUMENT_ID, historyData.get(position).id);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}