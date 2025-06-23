package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ListView toDayList = findViewById(R.id.toDayList);
        List<Notification> notifications = new ArrayList<>();
  
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.money_send_ico, "Money Transfer", "25m ago", "You have successfully sent money to Maria of..."));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));
        notifications.add(new Notification(R.drawable.reward_ico, "Rewards", "5m ago", "Loyal user rewards!\uD83D\uDE18"));

        NotificationAdapter adapter = new NotificationAdapter(this, notifications);
        toDayList.setAdapter(adapter);

        ListView thisWeekList = findViewById(R.id.thisWeekList);
        List<Notification> notificationsWeek = new ArrayList<>();
 
        notificationsWeek.add(new Notification(R.drawable.card_ico, "Payment Notification", "June 22", "Successfully paid!\uD83E\uDD11"));
        notificationsWeek.add(new Notification(R.drawable.money_take_ico, "Top Up", "June 22", "Your top up is successfully!"));
        notificationsWeek.add(new Notification(R.drawable.money_send_ico, "Money Transfer", "June 21", "You have successfully sent money to Maria of..."));

        NotificationAdapter adapterWeek = new NotificationAdapter(this, notificationsWeek);
        thisWeekList.setAdapter(adapterWeek);


    }
}
