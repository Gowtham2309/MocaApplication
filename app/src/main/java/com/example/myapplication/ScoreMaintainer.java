package com.example.myapplication;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*  ScoreMaintainer is a singleton class
    This class object is used to maintains the score over the entire application */
public class ScoreMaintainer {

    private static ScoreMaintainer object;
    private static HashMap<String, Integer> scores;
    private static HistoryInstance historyInstance;
    private static long startTimestamp;

    // Maintain hashmap for each different page
    // Inside the each activity java class has a string TEST_NAME which identifies it

    private ScoreMaintainer() {
        scores = new HashMap<>();
        historyInstance = new HistoryInstance();
        historyInstance.setScores(scores);
    }

    public static synchronized ScoreMaintainer getInstance() {
        if (object == null) {
            object = new ScoreMaintainer();
        }
        return object;
    }

    public void recordStartTime() {
        startTimestamp = System.currentTimeMillis();
    }

    public void recordEndTime() {
        Date currentDate = new Date(System.currentTimeMillis());
        Date startDate = new Date(startTimestamp);
        long diff = currentDate.getTime() - startDate.getTime();
        historyInstance.setDurationMin(TimeUnit.MILLISECONDS.toMinutes(diff));
    }

    public void clear() {
        scores.clear();
        historyInstance.clear();
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

    public void setPhysicianName(String name) {
        historyInstance.setPhysicianName(name);
    }
    public void setPatientName(String name) {
        historyInstance.setPatientName(name);
    }
    public void setPatientAge(int age) {
        historyInstance.setPatientAge(age);
    }

    public String uploadToFirebase(String phoneNumber) {
        /* Uploads all the score to the firebase firestore under the History -> phone number branch
        * and returns the time which is used as the id in firestore */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String time = Long.toString(System.currentTimeMillis());
        historyInstance.setTotalScore(getTotalScore());
        historyInstance.setTimeStamp(time);
        historyInstance.setPhoneNumber(phoneNumber);
//        db.collection(phoneNumber).document(time).set(scores)
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        System.out.println("Failed to upload the data to database");
//                    }
//                });
        DocumentReference ref = db.collection("MoCA").document();
        ref.set(historyInstance)
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Failed to upload the data to database");
                }
            });
        return ref.getId();
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return historyInstance.toString();
    }
}
