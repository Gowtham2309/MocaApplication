package com.example.myapplication;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public Map<String, Integer> getAllScores() {
        return scores;
    }

    public String uploadToFirebase(String phoneNumber) {
        /* Uploads all the score to the firebase firestore under the History -> phone number branch
        * and returns the time which is used as the id in firestore */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String time = Long.toString(System.currentTimeMillis());
        db.collection(phoneNumber).document(time).set(scores)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed to upload the data to database");
                    }
                });
        return time;
    }
}
