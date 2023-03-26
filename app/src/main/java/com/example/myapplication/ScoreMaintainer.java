package com.example.myapplication;

import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/*  ScoreMaintainer is a singleton class
    This class object is used to maintains the score over the entire application */
public class ScoreMaintainer {

    private static ScoreMaintainer object;
    private static HashMap<String, Integer> scores;

    // Maintain hashmap for each different page
    // Inside the each activity java class has a string TEST_NAME which identifies it

    private ScoreMaintainer() {
        scores = new HashMap<>();
    }

    public static synchronized ScoreMaintainer getInstance() {
        if (object == null) {
            object = new ScoreMaintainer();
        }
        return object;
    }

    public int getTotalScore() {
        int totalScore = 0;
        for(Map.Entry<String, Integer> entry:scores.entrySet()) {
            totalScore += entry.getValue();
        }

        return totalScore;
    }

    public void updateScore(String testName, int score) {
        scores.put(testName, score);
    }

    public int getScore(String testName) {
        if (!scores.containsKey(testName)) {
            scores.put(testName, 0);
        }
        return scores.get(testName);
    }

    public void uploadToFirebase(String phoneNumber) {
        /* Uploads all the score to the firebase realtime database under the History -> phone number branch */
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReferenceFromUrl("https://moca-test-5bfbb-default-rtdb.firebaseio.com/");

        long time = System.currentTimeMillis();
        DatabaseReference userHistoryRef = reference.child("History").child(phoneNumber).child(Long.toString(time));

        // setting the values
        for(Map.Entry<String, Integer> entry:scores.entrySet()) {
            userHistoryRef.child(entry.getKey()).setValue(entry.getValue());
        }
        userHistoryRef.child("totalScore").setValue(getTotalScore());
    }
}
