package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MyCardActivity extends AppCompatActivity {

    ImageView backBtn;
    Button addCardBtn;
    ListView listView;
    List<CardItem> cardItems;
    CardAdapter adapter;

    LinearLayout homeTravelBtn, profileTravelBtn;

    String userId, accessToken;
    SupabaseClient supabaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

        backBtn = findViewById(R.id.backBtn);
        addCardBtn = findViewById(R.id.addCardBtn);
        listView = findViewById(R.id.listView);
        homeTravelBtn = findViewById(R.id.homeTravelBtn);
        profileTravelBtn = findViewById(R.id.profileTravelBtn);

        supabaseClient = new SupabaseClient();

        userId = PrefsUtils.getUserId(this);
        accessToken = PrefsUtils.getAccessToken(this);

        cardItems = new ArrayList<>();
        adapter = new CardAdapter(this, cardItems);
        listView.setAdapter(adapter);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(MyCardActivity.this, MainActivity.class));
            finish();
        });

        addCardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MyCardActivity.this, NewCardActivity.class);
            startActivity(intent);
            finish();
        });

        checkUserAccountAndLoadCards();

        profileTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCardActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkUserAccountAndLoadCards() {
        supabaseClient.hasUserAccount(userId, accessToken, new SupabaseClient.CallbackListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    if ("true".equals(message)) {
                        loadUserCards();
                    } else {
                        // аккаунта нет, очищаем список
                        cardItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MyCardActivity.this, "Ошибка: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadUserCards() {
        supabaseClient.getUserCards(userId, accessToken, new SupabaseClient.CardsCallback() {
            @Override
            public void onSuccess(List<CardItem> cards) {
                runOnUiThread(() -> {
                    cardItems.clear();
                    cardItems.addAll(cards);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MyCardActivity.this, "Ошибка при получении карт: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}