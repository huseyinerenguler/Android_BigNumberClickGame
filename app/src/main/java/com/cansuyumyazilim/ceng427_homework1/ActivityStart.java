package com.cansuyumyazilim.ceng427_homework1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class ActivityStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // I get the screen width of the phone in pixels.
        int screenWidth = getScreenWidth();
        // And I'm saving to sharedPreferences to use in other classes.
        editor.putInt("screenWidth", screenWidth).apply();

        // If I have a highScore previously saved to sharedPreferences, I get it, if not, it will set 0 as default.
        int highScore = sharedPreferences.getInt("highScore", 0);

        TextView textView_highScore = findViewById(R.id.textView_highScore);
        TextView textView_tapToStart = findViewById(R.id.textView_tapToStart);
        TextView textView_developerName = findViewById(R.id.textView_developerName);

        // I set the text size according to the screen width.
        textView_highScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 13f);
        textView_tapToStart.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 7f);
        textView_developerName.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 25f);

        // I display the high score on the screen.
        textView_highScore.setText("High Score: " + highScore);

        // If "Tap To Start" is touched, I switch to ActivityGame to start the game.
        textView_tapToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityStart.this, ActivityGame.class));
            }
        });
    }

    // This is for deactivate the back press.
    @Override
    public void onBackPressed() {
        // do nothing
    }

    // This function returns the pixel value of the device's screen width. If the screen width is not received, it returns 500 by default.
    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        if (screenWidth > 0)
            return screenWidth;
        else {
            return 500; // default
        }
    }
}