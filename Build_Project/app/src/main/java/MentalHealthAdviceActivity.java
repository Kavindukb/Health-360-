package com.example.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MentalHealthAdviceActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton, connectProfessionalsButton;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private DatabaseHelper dbHelper;
    private int userId;

    // Chatbot states using enum
    private ChatState currentState = ChatState.INITIAL_GREETING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health_advice);

        // Initialize Views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        connectProfessionalsButton = findViewById(R.id.connectProfessionalsButton);

        // Initialize DatabaseHelper and get user ID
        dbHelper = new DatabaseHelper(this);
        userId = getUserId(); // Implement your logic to get the current user's ID

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Start the conversation
        addBotMessage("Hello! I'm here to help you with your mental wellness. Let's start with some questions.");

        sendButton.setOnClickListener(v -> sendMessage());

        connectProfessionalsButton.setOnClickListener(v -> connectWithProfessionals());
    }

    private void sendMessage() {
        String userMessage = messageInput.getText().toString().trim();
        if (userMessage.isEmpty()) {
            Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add user message to chat
        addUserMessage(userMessage);
        messageInput.setText("");

        // Handle chatbot response based on current state
        handleChatbotResponse(userMessage.toLowerCase());
    }

    private void handleChatbotResponse(String userMessage) {
        switch (currentState) {
            case INITIAL_GREETING:
                // State 0: Ask about the main issue
                addBotMessage("What is your main concern today? (e.g., anxiety, depression, stress, insomnia, panic attacks, low self-esteem)");
                currentState = ChatState.ASK_MAIN_CONCERN;
                break;
            case ASK_MAIN_CONCERN:
                // State 1: Acknowledge the issue and ask for more details
                addBotMessage("I'm sorry you're experiencing " + userMessage + ". Can you tell me more about it?");
                currentState = ChatState.ASK_DETAILS;
                break;
            case ASK_DETAILS:
                // State 2: Provide a general solution based on the issue
                String solution = generateSolution(userMessage);
                addBotMessage(solution);
                currentState = ChatState.PROVIDE_SOLUTION;
                break;
            case PROVIDE_SOLUTION:
                // State 3: End of conversation, offer to connect with professionals
                addBotMessage("I hope this helps! If you need more support, you can connect with a professional.");
                showConnectButton();
                currentState = ChatState.OFFER_SUPPORT;
                break;
            case OFFER_SUPPORT:
                // State 4: End of conversation
                addBotMessage("If you have more concerns, feel free to reach out anytime.");
                currentState = ChatState.END_CONVERSATION;
                break;
            case END_CONVERSATION:
                // Already ended conversation
                break;
            default:
                addBotMessage("If you have more concerns, feel free to reach out anytime.");
                break;
        }
    }

    private String generateSolution(String issue) {
        switch (issue) {
            case "anxiety":
                return "Managing anxiety can be challenging. Here are some tips:\n" +
                        "1. Practice deep breathing exercises.\n" +
                        "2. Maintain a regular exercise routine.\n" +
                        "3. Limit caffeine and alcohol intake.";
            case "depression":
                return "Dealing with depression requires care. Consider the following:\n" +
                        "1. Stay connected with friends and family.\n" +
                        "2. Set small, achievable goals.\n" +
                        "3. Seek professional help if needed.";
            case "stress":
                return "Reducing stress is important for your well-being. Try these methods:\n" +
                        "1. Practice mindfulness and meditation.\n" +
                        "2. Organize your tasks and prioritize them.\n" +
                        "3. Take regular breaks and ensure adequate sleep.";
            case "insomnia":
                return "Improving sleep can significantly enhance your mental health. Here are some strategies:\n" +
                        "1. Maintain a consistent sleep schedule.\n" +
                        "2. Create a relaxing bedtime routine.\n" +
                        "3. Limit screen time before bed.";
            case "panic attacks":
                return "Experiencing panic attacks can be frightening. Here are some ways to cope:\n" +
                        "1. Focus on your breathing.\n" +
                        "2. Ground yourself by noticing your surroundings.\n" +
                        "3. Seek support from a mental health professional.";
            case "low self-esteem":
                return "Boosting self-esteem involves positive changes. Consider the following:\n" +
                        "1. Challenge negative self-talk.\n" +
                        "2. Set and achieve small goals.\n" +
                        "3. Surround yourself with supportive people.";
            case "bullying":
                return "Dealing with bullying requires support and strategies. Here are some tips:\n" +
                        "1. Talk to someone you trust about what's happening.\n" +
                        "2. Develop assertive communication skills.\n" +
                        "3. Seek professional help if needed.";
            case "relationship issues":
                return "Improving relationships involves understanding and communication. Consider the following:\n" +
                        "1. Communicate openly and honestly.\n" +
                        "2. Practice active listening.\n" +
                        "3. Seek couples counseling if necessary.";
            case "addiction":
                return "Overcoming addiction is a challenging journey. Here are some steps:\n" +
                        "1. Acknowledge the problem and seek help.\n" +
                        "2. Join support groups or therapy sessions.\n" +
                        "3. Develop healthy coping mechanisms.";
            case "trauma":
                return "Healing from trauma requires time and support. Consider the following:\n" +
                        "1. Seek professional therapy.\n" +
                        "2. Practice self-care and mindfulness.\n" +
                        "3. Connect with supportive communities.";
            case "grief":
                return "Coping with grief involves understanding your emotions and seeking support. Here are some tips:\n" +
                        "1. Allow yourself to feel and express emotions.\n" +
                        "2. Connect with supportive friends and family.\n" +
                        "3. Consider joining a grief support group.";
            case "burnout":
                return "Recovering from burnout requires prioritizing self-care. Try the following:\n" +
                        "1. Take regular breaks and time off.\n" +
                        "2. Set clear boundaries between work and personal life.\n" +
                        "3. Engage in activities that you enjoy and that relax you.";
            default:
                return "Thank you for sharing. Remember, taking small steps can make a big difference in your mental wellness.";
        }
    }

    private void addUserMessage(String message) {
        messageList.add(new Message(message, true));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void addBotMessage(String message) {
        messageList.add(new Message(message, false));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void showConnectButton() {
        connectProfessionalsButton.setVisibility(View.VISIBLE);
    }

    private void connectWithProfessionals() {
        // Implement the logic to connect with professionals
        // This could be opening a new activity, a web page, or initiating a call/email
        Toast.makeText(this, "Connecting you with professionals...", Toast.LENGTH_SHORT).show();
        // Example: Open a web page
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yourprofessionalservice.com"));
        startActivity(browserIntent);
    }

    private int getUserId() {
        // Retrieve the current user's ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1); // -1 as default if not found
    }
}
