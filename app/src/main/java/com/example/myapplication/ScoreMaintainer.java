package com.example.myapplication;

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
}
