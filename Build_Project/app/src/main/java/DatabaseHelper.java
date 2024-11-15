package com.example.bmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "health_app.db";
    private static final int DATABASE_VERSION = 4; // Incremented version for new table

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_PASSWORD = "password";

    // BMI History table
    private static final String TABLE_BMI_HISTORY = "bmi_history";
    public static final String COLUMN_BMI_ID = "bmi_id";
    public static final String COLUMN_USER_REF_ID = "user_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_BMI = "bmi";
    public static final String COLUMN_BMI_DATE = "date";

// Add a method to retrieve column names if necessary


    // Feedbacks table
    private static final String TABLE_FEEDBACKS = "feedbacks";
    private static final String COLUMN_FEEDBACK_ID = "feedback_id";
    private static final String COLUMN_FEEDBACK = "feedback";
    private static final String COLUMN_FEEDBACK_DATE = "date";

    // Exercises table
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_EXERCISE_ID = "exercise_id";
    public static final String COLUMN_EXERCISE_NAME = "name";
    public static final String COLUMN_EXERCISE_DESCRIPTION = "description";
    public static final String COLUMN_EXERCISE_VIDEO_URI = "video_uri";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // For Toast messages
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_MOBILE + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Create BMI History Table
        String createBMITable = "CREATE TABLE " + TABLE_BMI_HISTORY + " (" +
                COLUMN_BMI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_REF_ID + " INTEGER, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_HEIGHT + " REAL, " +
                COLUMN_BMI + " REAL, " +
                COLUMN_BMI_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_USER_REF_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createBMITable);

        // Create Feedbacks Table
        String createFeedbacksTable = "CREATE TABLE " + TABLE_FEEDBACKS + " (" +
                COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_REF_ID + " INTEGER, " +
                COLUMN_FEEDBACK + " TEXT, " +
                COLUMN_FEEDBACK_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_USER_REF_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createFeedbacksTable);

        // Create Exercises Table
        String createExercisesTable = "CREATE TABLE " + TABLE_EXERCISES + " (" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT, " +
                COLUMN_EXERCISE_DESCRIPTION + " TEXT, " +
                COLUMN_EXERCISE_VIDEO_URI + " TEXT)";
        db.execSQL(createExercisesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
        if (oldVersion < 2) {
            // For version 2, create bmi_history table
            String createBMITable = "CREATE TABLE " + TABLE_BMI_HISTORY + " (" +
                    COLUMN_BMI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_REF_ID + " INTEGER, " +
                    COLUMN_WEIGHT + " REAL, " +
                    COLUMN_HEIGHT + " REAL, " +
                    COLUMN_BMI + " REAL, " +
                    COLUMN_BMI_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COLUMN_USER_REF_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
            db.execSQL(createBMITable);
        }
        if (oldVersion < 3) {
            // For version 3, create feedbacks and exercises tables
            String createFeedbacksTable = "CREATE TABLE " + TABLE_FEEDBACKS + " (" +
                    COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_REF_ID + " INTEGER, " +
                    COLUMN_FEEDBACK + " TEXT, " +
                    COLUMN_FEEDBACK_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COLUMN_USER_REF_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
            db.execSQL(createFeedbacksTable);

            String createExercisesTable = "CREATE TABLE " + TABLE_EXERCISES + " (" +
                    COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EXERCISE_NAME + " TEXT, " +
                    COLUMN_EXERCISE_DESCRIPTION + " TEXT, " +
                    COLUMN_EXERCISE_VIDEO_URI + " TEXT)";
            db.execSQL(createExercisesTable);
        }
    }

    // User Registration
    public boolean registerUser(String name, String email, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_MOBILE, mobile);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // True if insert successful
    }

    // User Login - returns user_id if successful, else -1
    public int loginUserAndGetId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
            db.close();
            return userId;
        } else {
            cursor.close();
            db.close();
            return -1; // Login failed
        }
    }

    // Save BMI record
    public boolean saveBMIDatabase(int userId, double weight, double height, double bmi) {
        if (userId == -1) {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show();
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_REF_ID, userId);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_BMI, bmi);

        long result = db.insert(TABLE_BMI_HISTORY, null, values);
        db.close();
        return result != -1;
    }

    // Retrieve BMI history for a specific user
    public Cursor getBMHistory(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BMI_HISTORY + " WHERE " + COLUMN_USER_REF_ID + " = ? ORDER BY " + COLUMN_BMI_ID + " DESC",
                new String[]{String.valueOf(userId)});
    }

    // Save Feedback
    public boolean saveFeedback(int userId, String feedback) {
        if (userId == -1) {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show();
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put(COLUMN_FEEDBACK, feedback);

        long result = db.insert(TABLE_FEEDBACKS, null, values);
        db.close();
        return result != -1;
    }

    // Retrieve all feedbacks (for admin)
    public Cursor getAllFeedbacks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FEEDBACKS + " ORDER BY " + COLUMN_FEEDBACK_ID + " DESC", null);
    }

    // Exercises CRUD operations as previously implemented
    // Add Exercise
    public boolean addExercise(String name, String description, String videoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, name);
        values.put(COLUMN_EXERCISE_DESCRIPTION, description);
        values.put(COLUMN_EXERCISE_VIDEO_URI, videoUri);
        long result = db.insert(TABLE_EXERCISES, null, values);
        db.close();
        return result != -1;
    }

    // Retrieve all exercises
    public Cursor getAllExercises() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXERCISES, null);
    }

    // Retrieve the latest BMI entry for the user
    public Cursor getLatestBMI(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BMI_HISTORY + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_BMI_ID + " DESC LIMIT 1", new String[]{String.valueOf(userId)});
    }
    // Retrieve a specific exercise by ID
    public Cursor getExerciseById(int exerciseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_ID + " = ?", new String[]{String.valueOf(exerciseId)});
    }

    // Retrieve all users from the users table
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Update an existing exercise
    public boolean updateExercise(int exerciseId, String name, String description, String videoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, name);
        values.put(COLUMN_EXERCISE_DESCRIPTION, description);
        values.put(COLUMN_EXERCISE_VIDEO_URI, videoUri);
        int rows = db.update(TABLE_EXERCISES, values, COLUMN_EXERCISE_ID + " = ?", new String[]{String.valueOf(exerciseId)});
        db.close();
        return rows > 0;
    }

    // Delete an exercise
    public boolean deleteExercise(int exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_EXERCISES, COLUMN_EXERCISE_ID + " = ?", new String[]{String.valueOf(exerciseId)});
        db.close();
        return rows > 0;
    }
}
