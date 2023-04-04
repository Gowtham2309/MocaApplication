package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalyticsPage extends AppCompatActivity {
    LineChart lineChart;
    LinearLayout flexButtonGroup;
    Context context;

    // TODO: add the remaining test names
    String[] testNames = new String[]{
            "totalScore",
            //cdt_Drawing.TEST_NAME,
            gaming.TEST_NAME,
            Attention.TEST_NAME,
            Letter_Identification.TEST_NAME,
            Naming.TEST_NAME,
            SubtractionMain.TEST_NAME,
    };

    int[] colors = new int[]{
            R.color.black,
            R.color.red,
            R.color.thistle,
            R.color.davysGray,
            R.color.teal_200
    };
    Map<String, Integer> colorMap;

    Set<String> visibleTests = new HashSet<>();
    List<HistoryInstance> historyInstances;
    List<Button> btnList;
    TextView txtLoading, txtHighestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_page);
        // setting title in the action bar
        try {
            getSupportActionBar().setTitle("Analytics");
        } catch (NullPointerException ignored) {}

        lineChart = findViewById(R.id.lineChart);
        flexButtonGroup = findViewById(R.id.horizontalButtonBar);
        txtHighestScore = findViewById(R.id.analyticsHighestScore);
        context = this;
        btnList = new ArrayList<>();

        setColorMapping();

        historyInstances = new ArrayList<>();

        lineChart.setDrawGridBackground(false);
        setToLoading();
        populateHistoryInstances();
    }

    private void setColorMapping() {
        colorMap = new HashMap<>();
        colorMap.put(gaming.TEST_NAME, R.color.red);
        colorMap.put(Attention.TEST_NAME, R.color.black);
        colorMap.put(Letter_Identification.TEST_NAME, R.color.purple_500);
        colorMap.put(Naming.TEST_NAME, R.color.jetStream);
        colorMap.put(SubtractionMain.TEST_NAME, R.color.teal_200);
        colorMap.put("totalScore", R.color.green);
    }

    private void setToLoading() {
        txtLoading = new TextView(context);
        txtLoading.setText("Loading ... ");
        txtLoading.setTextAppearance(context, R.style.contentText);
        flexButtonGroup.addView(txtLoading);
    }

    private void removeLoading() {
        txtLoading.setVisibility(View.INVISIBLE);
        flexButtonGroup.removeView(txtLoading);
    }

    private void setHighestScore() {
        int highestScore = 0;
        for(HistoryInstance instance: historyInstances) {
            highestScore = Math.max( highestScore, instance.getTotalScore() );
        }
        txtHighestScore.setText(Integer.toString(highestScore));
    }

    private void populateHistoryInstances() {
        // get phone number
        String phoneNumber = getSharedPreferences( getString(R.string.sharedPrefsName), Context.MODE_PRIVATE )
                .getString( getString(R.string.login), "" );

        // get data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MoCA")
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // add data to historyInstances
                    for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                        historyInstances.add(snapshot.toObject(HistoryInstance.class));
                    }

                    removeLoading();

                    // now enable all the buttons
                    populateFlexButtonGroup(context);
                    setHighestScore();
                }).addOnFailureListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadChart() {
        // create entries
        Map<String, List<Entry>> entryLists = new HashMap<>();
        for(String test: visibleTests) entryLists.put(test, new ArrayList<>());

        int index = 0;
        ArrayList<String> timeStamps = new ArrayList<>();

        for(HistoryInstance instance: historyInstances) {
            Map<String, Integer> scores = instance.getScores();
            scores.put("totalScore", instance.getTotalScore());
            timeStamps.add(instance.getTimeStamp());
            for(String test: visibleTests) {
                int score;
                if (scores.containsKey(test)) score = scores.get(test);
                else score = 0;

                entryLists.get(test).add(new Entry(index, score));
            }
            index++;
        }

        // convert to LineDataSet
        List<ILineDataSet> dataSets = new ArrayList<>();
        int colorIndex = 0;
        for(Map.Entry<String, List<Entry>> entry: entryLists.entrySet()) {
            LineDataSet tmpLineDataset = new LineDataSet(entry.getValue(), entry.getKey());
            tmpLineDataset.setColors(new int[]{colorMap.get(entry.getKey())}, context);
            tmpLineDataset.setCircleColors(new int[]{colorMap.get(entry.getKey())}, context);
            tmpLineDataset.setLineWidth(3f);
            tmpLineDataset.setDrawValues(false);
            //System.out.printf("Setting color %d to %s%n", colors[colorIndex], entry.getKey());
            tmpLineDataset.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSets.add(tmpLineDataset);
        }

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);

        // format x-axis text to dates
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                Date date = new Date(Long.parseLong(timeStamps.get((int) value)));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
                return dateFormat.format(date);
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawGridLines(false);

        lineChart.getAxisLeft().setGranularity(1f);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.setDrawGridBackground(false);
        lineChart.invalidate(); // refresh
    }

    private void updateVisibleTests(String test, Button btn) {
        if (visibleTests.contains(test)) {
            visibleTests.remove(test);
            btn.setBackgroundResource(R.drawable.button_background);
        } else {
            visibleTests.add(test);
            btn.setBackgroundResource(R.drawable.button_background_semi_rounded);
        }
    }

    private void populateFlexButtonGroup(Context context) {
        for(String test: testNames) {
            Button btnTemp = new Button(context);
            btnTemp.setText(test);
            btnTemp.setBackgroundResource(R.drawable.button_background);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10,10,10,20);
            btnTemp.setLayoutParams(layoutParams);
            btnList.add(btnTemp);

            btnTemp.setOnClickListener((View v) -> {
                updateVisibleTests(test, (Button) v);
                loadChart();
            });

            flexButtonGroup.addView(
                    btnTemp,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
        }
    }
}