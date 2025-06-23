package com.example.financesappcopayment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Handler;
import android.content.Intent;

public class LoadScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 секунды

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Переход к главной активности
                Intent mainIntent = new Intent(LoadScreenActivity.this, OnboardingActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        //boolean isUserAuthorized = checkUserAuthorization();

        //new Handler().postDelayed(() -> {
            //if (isUserAuthorized) {
                // Пользователь авторизован, переходим к основной активности
               //startActivity(new Intent(LoadScreenActivity.this, MainActivity.class));
            //} else {
                // Пользователь не авторизован, показываем приветственный экран
                //startActivity(new Intent(LoadScreenActivity.this, OnboardingActivity.class));
           // }
            //finish();
        //}, SPLASH_DISPLAY_LENGTH);
    }

    //private boolean checkUserAuthorization() {
        //Здесь логика проверки авторизации
        //return false;
    //}
}
