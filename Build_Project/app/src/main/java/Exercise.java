package com.example.bmi;

public class Exercise {
    private String name;
    private String videoUrl;

    public Exercise(String name, String videoUrl) {
        this.name = name;
        this.videoUrl = videoUrl;
    }

    public String getName() {
        return name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String toString() {
        return name; // This is important for the ArrayAdapter to display the exercise name
    }
}
