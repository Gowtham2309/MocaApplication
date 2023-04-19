package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
            cdt_Drawing.TEST_NAME,
            Cube.TEST_NAME,
            gaming.TEST_NAME,
            Attention.TEST_NAME,
            Letter_Identification.TEST_NAME,
            Naming.TEST_NAME,
            SubtractionMain.TEST_NAME,
            Orientation.TEST_NAME,
            Memory_Delayedrecall.TEST_NAME_MEMORY,
            Memory_Delayedrecall.TEST_NAME_DELAYEDRECALL,
    };

    int[] colors = new int[]{
            R.color.black,
            R.color.red,
            R.color.thistle,
            R.color.davysGray,
            R.color.teal_200,
            R.color.green,
            R.color.purple_500
    };
    Map<String, Integer> colorMap;

    Set<String> visibleTests = new HashSet<>();
    List<HistoryInstance> historyInstances;
    List<Button> btnList;
    TextView txtLoading, txtHighestScore, searchBox;
    Dialog dialog;
    String currentSelectedPatient;

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
        searchBox = findViewById(R.id.patientSearch);
        searchBox.setEnabled(false); // until loading all data need to be disabled
        currentSelectedPatient = "";

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
        colorMap.put(cdt_Drawing.TEST_NAME, R.color.teal_700);
        colorMap.put(Cube.TEST_NAME, R.color.orange);
        colorMap.put(Orientation.TEST_NAME, R.color.bright_green);
        colorMap.put(Memory_Delayedrecall.TEST_NAME_MEMORY, R.color.pink);
        colorMap.put(Memory_Delayedrecall.TEST_NAME_DELAYEDRECALL, R.color.yellow);
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

    private void setHighestScore(String patientName) {
        int highestScore = 0;
        for(HistoryInstance instance: historyInstances) {
            if (instance.getPatientName().equals(patientName))
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
                    System.out.println("===== history instances ======");
                    System.out.println(historyInstances);

                    removeLoading();
                    setupPatientSearch();

                    // now enable all the buttons
                    populateFlexButtonGroup(context);
                    searchBox.setEnabled(true);
                }).addOnFailureListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadChart() {
        if (currentSelectedPatient.equals("")) {
            Toast.makeText(this, "Select a patient", Toast.LENGTH_SHORT).show();
            return;
        }
        // create entries
        Map<String, List<Entry>> entryLists = new HashMap<>();
        for(String test: visibleTests) entryLists.put(test, new ArrayList<>());

        int index = 0;
        ArrayList<String> timeStamps = new ArrayList<>();

        for(HistoryInstance instance: historyInstances) {
            if (!instance.getPatientName().equals(currentSelectedPatient)) continue;
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

        System.out.println("===== timestamps =====");
        System.out.println(timeStamps);

        // format x-axis text to dates
//        ValueFormatter formatter = new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                System.out.println("Xaxis value: "+value);
//                Date date = new Date(Long.parseLong(timeStamps.get((int) value)));
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
//                return dateFormat.format(date);
//            }
//        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(formatter);
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

    private void removeAllVisibleTests() {
        visibleTests.clear();
        for(Button btn: btnList) {
            btn.setBackgroundResource(R.drawable.button_background);
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

    private List<String> getPatientNames() {
        Set<String> set = new HashSet<>();
        for(HistoryInstance instance: historyInstances) {
            set.add(instance.getPatientName());
        }

        return new ArrayList<>(set);
    }

    private void setupPatientSearch() {
        List<String> patients = getPatientNames();
        searchBox.setOnClickListener(view -> {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_searchable_spinner);
            // set custom height and width
            dialog.getWindow().setLayout(650,800);
            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // show dialog
            dialog.show();

            EditText editText=dialog.findViewById(R.id.search_box);
            ListView listView=dialog.findViewById(R.id.patient_list_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, patients
            );

            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                // when item selected from list
                // set selected item on textView
                String patient = adapter.getItem(position);
                searchBox.setText(patient);
                currentSelectedPatient = patient;
                removeAllVisibleTests();
                setHighestScore(patient);
                loadChart();

                // Dismiss dialog
                dialog.dismiss();
            });
        });
    }
}