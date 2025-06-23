package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CardView userCard;
    String accessToken;
    TextView userNameTextView;
    LinearLayout profileTravelBtn, activityTravelBtn, cardTravelBtn;
    ImageView alertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userCard=findViewById(R.id.userCard);
        userCard.setBackgroundResource(R.drawable.card_background);
        profileTravelBtn=findViewById(R.id.profileTravelBtn);
        activityTravelBtn=findViewById(R.id.activityTravelBtn);
        cardTravelBtn=findViewById(R.id.cardTravelBtn);
        alertBtn=findViewById(R.id.alertBtn);

        accessToken = PrefsUtils.getAccessToken(MainActivity.this);

        ListView listView = findViewById(R.id.listView);
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(R.drawable.sports_ico, "Sports", "-15.99", "Payment"));
        transactions.add(new Transaction(R.drawable.money_take_ico, "Bank of America", "+2,045.00", "Deposit"));
        transactions.add(new Transaction(R.drawable.send_money_ico, "To Brody Armando", "-986.00", "Sent"));
        transactions.add(new Transaction(R.drawable.sports_ico, "Sports", "-5.59", "Payment"));
        transactions.add(new Transaction(R.drawable.money_take_ico, "Bank of America", "+1,520.00", "Deposit"));
        transactions.add(new Transaction(R.drawable.send_money_ico, "To Brody Armando", "-300.00", "Sent"));
        transactions.add(new Transaction(R.drawable.sports_ico, "Sports", "-21.99", "Payment"));
        transactions.add(new Transaction(R.drawable.money_take_ico, "Bank of America", "+515.00", "Deposit"));
        transactions.add(new Transaction(R.drawable.send_money_ico, "To Brody Armando", "-750.00", "Sent"));

        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        listView.setAdapter(adapter);

        String userId = PrefsUtils.getUserId(MainActivity.this); // ваш id пользователя
        if (accessToken != null && userId != null) {
            new SupabaseClient().getUserName(userId, accessToken, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String name) {
                    runOnUiThread(() -> {
                        userNameTextView = findViewById(R.id.userNameTv);
                        userNameTextView.setText(name);
                    });
                }

                @Override
                public void onError(String error) {
                    // Обработка ошибок
                }
            });
        }
        if (accessToken != null && userId != null) {
            new SupabaseClient().getUserBalance(userId, accessToken, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        TextView balanceTv = findViewById(R.id.balanceTv);
                        try {
                            double balance = Double.parseDouble(message);
                            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                            formatter.setMinimumFractionDigits(2);
                            formatter.setMaximumFractionDigits(2);
                            String formattedBalance = "$" + formatter.format(balance);
                            balanceTv.setText(formattedBalance);
                        } catch (NumberFormatException e) {
                            balanceTv.setText("$0.00");
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        // Обработка ошибок
                        TextView balanceTv = findViewById(R.id.balanceTv);
                        balanceTv.setText("$0.00");
                    });
                }
            });
        }
        if (userId != null && accessToken != null) {
            new SupabaseClient().getUserCards(userId, accessToken, new SupabaseClient.CardsCallback() {
                @Override
                public void onSuccess(List<CardItem> cards) {
                    runOnUiThread(() -> {
                        if (!cards.isEmpty()) {
                            String cardNumber = cards.get(0).getCardNumber();
                            TextView cardNumberTv = findViewById(R.id.cardNumberTv);
                            cardNumberTv.setText(cardNumber);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Ошибка получения карты: " + error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cardTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        activityTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "В разработке...", Toast.LENGTH_SHORT).show();
            }
        });

        profileTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}