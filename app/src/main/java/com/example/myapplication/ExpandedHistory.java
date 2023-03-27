package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class ExpandedHistory extends AppCompatActivity {

    final static String PHONE_NUM = "PHONE_NUMBER", TIMESTAMP = "TIMESTAMP";
    ListView listView;
    TextView txtDate, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_history);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("ACT");
        } catch (NullPointerException ignored) {}

        Intent receivedIntent = getIntent();
        String phoneNumber = receivedIntent.getStringExtra(PHONE_NUM);
        String timestamp = receivedIntent.getStringExtra(TIMESTAMP);
        Date date = new Date(Long.parseLong(timestamp));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        txtDate = findViewById(R.id.expandedHistoryDate);
        txtTime = findViewById(R.id.expandedHistoryTime);
        txtDate.setText(dateFormat.format(date));
        txtTime.setText(timeFormat.format(date));

        listView = findViewById(R.id.expandedHistoryListView);
        Activity activity = this;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(phoneNumber).document(timestamp).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CustomListAdapter listAdapter = new CustomListAdapter(activity, documentSnapshot.getData());
                listView.setAdapter(listAdapter);
            }
        });

    }
}

class CustomListAdapter extends ArrayAdapter<Map<String, Object>> {
    Map<String, Object> data;
    List<String> keyList;
    List<Object> valueList;
    Activity context;

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.expanded_history_list_view, null, true);

        TextView txtKey = rowView.findViewById(R.id.expandedHistoryKey), txtValue = rowView.findViewById(R.id.expandedHistoryValue);

        txtKey.setText(keyList.get(position));
        txtValue.setText(valueList.get(position).toString());

        return rowView;
    }

    public CustomListAdapter(@NonNull Activity context, Map<String, Object> data) {
        super(context, R.layout.expanded_history_list_view);
        this.context = context;
        this.data = data;
        System.out.println(data);
        this.keyList = new ArrayList<>(data.keySet());
        this.valueList = new ArrayList<>(data.values());
    }
}