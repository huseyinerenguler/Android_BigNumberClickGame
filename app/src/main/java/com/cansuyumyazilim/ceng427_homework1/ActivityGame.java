package com.cansuyumyazilim.ceng427_homework1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityGame extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView[] numbers;
    private TextView textView_level;
    private TextView textView_score;
    private RatingBar ratingBar_rate;
    private ProgressBar progressBar_time;

    private CountDownTimer timer;

    private int currentMaxID;
    private int currentLevel;
    private int score = 0;
    private int scoreFromTime;
    private int highScore;
    private int timeLimit = 10000; // this is first time limit --> 10 seconds

    private int[][] levelValues = new int[10][3]; // 0,0-->count  -  0,1-->min  -  0,2-->max

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // If I have a highScore previously saved to sharedPreferences, I get it, if not, it will set 0 as default.
        highScore = sharedPreferences.getInt("highScore", 0);

        initializeGUI();
        initializeLevels();
        startGame();
    }

    // This is for deactivate the back press.
    @Override
    public void onBackPressed() {
        // do nothing
    }

    // This function sets the properties of the items on the game screen.
    private void initializeGUI() {
        numbers = new TextView[9];
        numbers[0] = findViewById(R.id.tv_number1);
        numbers[1] = findViewById(R.id.tv_number2);
        numbers[2] = findViewById(R.id.tv_number3);
        numbers[3] = findViewById(R.id.tv_number4);
        numbers[4] = findViewById(R.id.tv_number5);
        numbers[5] = findViewById(R.id.tv_number6);
        numbers[6] = findViewById(R.id.tv_number7);
        numbers[7] = findViewById(R.id.tv_number8);
        numbers[8] = findViewById(R.id.tv_number9);

        textView_level = findViewById(R.id.textView_level);
        textView_score = findViewById(R.id.textView_skor);
        ratingBar_rate = findViewById(R.id.ratingBar_rate);
        progressBar_time = findViewById(R.id.progresBar_time);

        // If I have a screenWidth previously saved to sharedPreferences, I get it, if not, it will set 1080 as default.
        int screenWidth = sharedPreferences.getInt("screenWidth", 1080);

        // I set the text size according to the screen width.
        textView_level.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 7.2f);
        textView_score.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 13.5f);

        // I set the text size and padding value to all number's area according to the screen width.
        for (int i = 0; i < numbers.length; i++) {
            numbers[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 7.2f);
            numbers[i].setPadding((int) (screenWidth / 10f), (int) (screenWidth / 10f), (int) (screenWidth / 10f), (int) (screenWidth / 10f));
        }
    }

    // This function generates levels as specified in the documentation. The first value is how many numbers to ask. The second value is the minimum number and the third is the maximum number.
    private void initializeLevels() {
        levelValues[0] = new int[]{2, 0, 10};
        levelValues[1] = new int[]{3, 0, 20};
        levelValues[2] = new int[]{3, 0, 30};
        levelValues[3] = new int[]{4, 0, 40};
        levelValues[4] = new int[]{4, -40, 40};
        levelValues[5] = new int[]{4, -60, 60};
        levelValues[6] = new int[]{4, -100, 100};
        levelValues[7] = new int[]{5, -100, 100};
        levelValues[8] = new int[]{6, -100, 100};
        levelValues[9] = new int[]{8, -100, 100};
    }

    // This function starts level 1.
    private void startGame() {
        currentLevel = 1;
        initNewLevel(currentLevel);
    }

    // This function works when the game is finished. Sends the score to the ActivityFinish class and run it.
    private void finishGame() {
        timer.cancel();
        Intent intent = new Intent(ActivityGame.this, ActivityFinish.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    // This function works when the player moves to a new level and makes the necessary settings for the new level.
    private void initNewLevel(int level) {

        // If the user has passed the 10th level, the game is over. Because there is no 11th level. :)
        if (level > 10) {
            finishGame();
        } else {
            // I display the current level on the screen.
            textView_level.setText("Level " + level);

            // I reset the star progress, because it is the new level.
            ratingBar_rate.setRating(0f);

            // I set the time limit according to level.
            progressBar_time.setMax(timeLimit);

            // I will be updated the time on the screen every 10 milliseconds.
            timer = new CountDownTimer(timeLimit, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressBar_time.setProgress((int) millisUntilFinished);

                    // Every 10 milliseconds, I am calculating the extra score using the remaining time.
                    scoreFromTime = (int) (millisUntilFinished / 1000);
                }

                // When the time is up, game is finish.
                @Override
                public void onFinish() {
                    finishGame();
                }
            };

            // A new stage is set according to the level.
            initNewStage(levelValues[level - 1][0], levelValues[level - 1][1], levelValues[level - 1][2]);
        }
    }

    // This function generates a random stage according to the incoming parameters. Resets the time and restarts.
    private void initNewStage(int count, int min, int max) {

        // I reset the time.
        timer.cancel();
        progressBar_time.setProgress(progressBar_time.getMax());

        // I display the score and high score on the screen.
        textView_score.setText("Score: " + score + " / High: " + highScore);

        // I've made all the number fields invisible.
        for (int i = 0; i < numbers.length; i++)
            numbers[i].setVisibility(View.GONE); // It doesn't keep memory when it's "GONE".

        ArrayList<Integer> randNumbers = generateRandomNumbers(count, min, max);
        ArrayList<Integer> randTextViewID = generateRandomNumbers(count, 0, 8);

        // I found the largest number and also found ID that who has this number.
        int currentMaxNumber = -101;
        for (int i = 0; i < count; i++) {
            if (currentMaxNumber < randNumbers.get(i)) {
                currentMaxNumber = randNumbers.get(i);
                currentMaxID = randTextViewID.get(i);
            }

            // I've assigned numbers and made it visible.
            numbers[randTextViewID.get(i)].setText(randNumbers.get(i) + "");
            numbers[randTextViewID.get(i)].setVisibility(View.VISIBLE);
        }

        // I start the time.
        timer.start();
    }

    // This function generates as many "unique" random numbers as desired between the specified minimum and maximum values.
    private ArrayList<Integer> generateRandomNumbers(int count, int min, int max) {

        int rand;
        int range = max - min + 1;
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            rand = (int) (Math.random() * range) + min;
            if (!numbers.contains(rand))
                numbers.add(rand);
            else {
                i--;
            }
        }
        return numbers;
    }

    // This function works when we touch a number. And it checks this number. It either starts a new level-stage, or finishes the game.
    public void onClick(View view) {

        // If it touched the correct number, it works. Otherwise, the game is finish.
        if (numbers[currentMaxID].getId() == view.getId()) {
            // Star progress will increment by one.
            ratingBar_rate.setRating(ratingBar_rate.getRating() + 1.0f);

            // Score will increase.
            score = score + 10 + scoreFromTime;

            // If we pass the high score, the high score will be updated and displayed in green.
            if (score >= highScore) {
                highScore = score;
                editor.putInt("highScore", highScore).apply();
                textView_score.setTextColor(Color.GREEN);
            }

            // If the final star progress is completed, a new level is reached. Otherwise a new stage is start.
            if (ratingBar_rate.getRating() == ratingBar_rate.getNumStars()) {
                timeLimit -= 1000; // Time limit is reduced by 1 second.
                timer.cancel(); // I reset the time.
                initNewLevel(++currentLevel);
            } else {
                initNewStage(levelValues[currentLevel - 1][0], levelValues[currentLevel - 1][1], levelValues[currentLevel - 1][2]);
            }

        } else {
            finishGame();
        }
    }
}