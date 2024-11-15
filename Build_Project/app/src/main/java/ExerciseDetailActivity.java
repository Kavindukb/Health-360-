package com.example.bmi;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseDetailActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_detail);

        webView = findViewById(R.id.webView);

        // Retrieve the exercise name and video URL from intent
        String exerciseName = getIntent().getStringExtra("exerciseName");
        String videoUrl = getIntent().getStringExtra("exerciseVideoUrl");

        // Set the title as the exercise name
        if (exerciseName != null) {
            setTitle(exerciseName);
        }

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript for video playback
        webView.setWebViewClient(new WebViewClient()); // To open the video inside the app instead of a browser

        // Load the video URL
        if (videoUrl != null) {
            webView.loadUrl(videoUrl);
        }
    }
}
