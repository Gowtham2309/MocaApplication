package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandedHistory extends AppCompatActivity {

    final static String PHONE_NUM = "PHONE_NUMBER", DOCUMENT_ID = "DOCUMENT ID";
    ListView listView;
    TextView txtDate, txtTime, txtPhysician, txtPatient, txtDuration, txtAge, txtUserFeedback;
    String docId;

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
        docId = receivedIntent.getStringExtra(DOCUMENT_ID);

        txtDate = findViewById(R.id.expandedHistoryDate);
        txtTime = findViewById(R.id.expandedHistoryTime);
        txtPhysician = findViewById(R.id.expandedHistoryPhysician);
        txtPatient = findViewById(R.id.expandedHistoryPatient);
        txtAge = findViewById(R.id.expandedHistoryAge);
        txtDuration = findViewById(R.id.expandedHistoryDuration);
        txtUserFeedback = findViewById(R.id.expandedHistoryUserFeedback);

        listView = findViewById(R.id.expandedHistoryListView);
        Activity activity = this;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MoCA").document(docId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HistoryInstance instance = documentSnapshot.toObject(HistoryInstance.class);
                System.out.println(instance);
                setData(instance, phoneNumber);
                CustomListAdapter listAdapter = new CustomListAdapter(activity, instance);
                listView.setAdapter(listAdapter);
            }
        });

    }

    private void deleteDoc(String docId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MoCA").document(docId).delete()
                .addOnFailureListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Failed to delete the instance", Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(unused -> {
                    onBackPressed();
                });
    }

    private void setUserFeedback(int totalScore, boolean display) {
        /*Evaluate the final score and tell user feedback regarding cognitive abilities*/
        if (!display) {
            // don't display the feedback
            txtUserFeedback.setVisibility(View.GONE);
            return;
        }

        String msg = "";
        int color;
        if (totalScore < 26) {
            // need to take elaborate cognitive tests
            msg = "Score is low! Please consult a physician";
            color = R.color.red;
        } else {
            msg = "Nice score! Cognitive functions are fine";
            color = R.color.green;
        }
        txtUserFeedback.setText(msg);
        txtUserFeedback.setTextColor(getResources().getColor(color));
    }

    public void setData(HistoryInstance instance, String phoneNumber) {
        String timestamp = instance.getTimeStamp();
        Date date = new Date(Long.parseLong(timestamp));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        txtDate.setText(dateFormat.format(date));
        txtTime.setText(timeFormat.format(date));

        txtPhysician.setText(instance.getPhysicianName());
        txtPatient.setText(instance.getPatientName());

        txtDuration.setText(String.format("%s min", instance.getDurationMin()));
        txtAge.setText(Integer.toString(instance.getPatientAge()));

        // only the orientation page sends the phoneNumber
        setUserFeedback(instance.getTotalScore(), phoneNumber != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.expanded_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuDelete) {
            deleteDoc(docId);
            return true;
        }
        return false;
    }
}

class CustomListAdapter extends ArrayAdapter<HistoryInstance> {
    HistoryInstance data;
    List<String> keyList;
    List<Object> valueList;
    Activity context;

    @Override
    public int getCount() {
        return data.getScores().size();
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

    public CustomListAdapter(@NonNull Activity context, HistoryInstance data) {
        super(context, R.layout.expanded_history_list_view);
        this.context = context;
        this.data = data;
        System.out.println(data);
        this.keyList = new ArrayList<>(data.getScores().keySet());
        this.valueList = new ArrayList<>(data.getScores().values());
    }
}