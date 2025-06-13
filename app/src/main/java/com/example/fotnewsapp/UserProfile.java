package com.example.fotnewsapp;

public class UserProfile {
    private String name;
    private String studentNumber;
    private String personalStatement;
    private String releaseVersion;

    // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
    public UserProfile() {}

    // Constructor
    public UserProfile(String name, String studentNumber, String personalStatement, String releaseVersion) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.personalStatement = personalStatement;
        this.releaseVersion = releaseVersion;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPersonalStatement() {
        return personalStatement;
    }

    public void setPersonalStatement(String personalStatement) {
        this.personalStatement = personalStatement;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }
}
