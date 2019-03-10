package com.cansuyumyazilim.ceng427_homework1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class ActivityFinish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        // If I have a screenWidth previously saved to sharedPreferences, I get it, if not, it will set 1080 as default.
        int screenWidth = sharedPreferences.getInt("screenWidth", 1080);

        // If I have a highScore previously saved to sharedPreferences, I get it, if not, it will set 0 as default.
        int highScore = sharedPreferences.getInt("highScore", 0);

        TextView textView_score = findViewById(R.id.textView_score);
        TextView textView_highScore = findViewById(R.id.textView_highScore);
        TextView textView_gameOverTapToTryAgain = findViewById(R.id.textView_gameOverTapToTryAgain);

        // I set the text size according to the screen width.
        textView_score.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 13f);
        textView_highScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 13f);
        textView_gameOverTapToTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 8f);

        // I display the high score on the screen.
        textView_highScore.setText("High Score: " + highScore);

        // I get the score from the ActivityGame class and display it on the screen. If I don't get the score, I display -1 as default.
        textView_score.setText("Score: " + getIntent().getIntExtra("score", -1));

        // If "Tap To Try Again" is touched, I switch to ActivityGame to restart the game.
        textView_gameOverTapToTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityFinish.this, ActivityGame.class));
            }
        });
    }

    // This is for deactivate the back press.
    @Override
    public void onBackPressed() {
        // do nothing
    }
}