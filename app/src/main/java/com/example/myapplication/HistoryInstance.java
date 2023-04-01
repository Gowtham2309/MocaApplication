package com.example.myapplication;

import java.util.Map;

public class HistoryInstance {
    private String physicianName, patientName, timeStamp;
    private int totalScore, patientAge;
    private Map<String, Integer> scores;

    public HistoryInstance() {
    }

    public HistoryInstance(String physicianName, String patientName, String timeStamp,
                           int totalScore, int patientAge, Map<String, Integer> scores) {
        this.physicianName = physicianName;
        this.patientName = patientName;
        this.timeStamp = timeStamp;
        this.totalScore = totalScore;
        this.patientAge = patientAge;
        this.scores = scores;
    }

    public String getPhysicianName() {
        return physicianName;
    }

    public void setPhysicianName(String physicianName) {
        this.physicianName = physicianName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public void clear() {
        scores.clear();
        totalScore = 0;
        physicianName = null;
        patientName = null;
        patientAge = -1;
    }

    public boolean validate() {
        if (physicianName == null || physicianName.equals("")) return false;
        if (patientName == null || patientName.equals("")) return false;
        return patientAge >= 0;
    }

    @Override
    public String toString() {
        return "HistoryInstance{" +
                "physicianName='" + physicianName + '\'' +
                ", patientName='" + patientName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", totalScore=" + totalScore +
                ", patientAge=" + patientAge +
                ", scores=" + scores +
                '}';
    }
}
