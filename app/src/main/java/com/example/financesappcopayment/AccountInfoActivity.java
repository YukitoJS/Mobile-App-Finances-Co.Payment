package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountInfoActivity extends AppCompatActivity {

    Button editBtn;
    ImageView backBtn;
    TextView userNameTv, userEmailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        userEmailTv = findViewById(R.id.userEmailTv);
        userNameTv = findViewById(R.id.userNameTv);
        editBtn = findViewById(R.id.editBtn);
        backBtn = findViewById(R.id.backBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfoActivity.this, EditAccountInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfoActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String accessToken = PrefsUtils.getAccessToken(AccountInfoActivity.this);
        String userId = PrefsUtils.getUserId(AccountInfoActivity.this);
        if (accessToken != null && userId != null) {
            new SupabaseClient().getUserName(userId, accessToken, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String name) {
                    runOnUiThread(() -> {
                        userNameTv = findViewById(R.id.userNameTv);
                        userNameTv.setText(name);
                    });
                }

                @Override
                public void onError(String error) {
                    // Обработка ошибок
                }
            });
        }

        if (accessToken != null && userId != null) {
            new SupabaseClient().getUserEmail(userId, accessToken, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String email) {
                    runOnUiThread(() -> {
                        userEmailTv = findViewById(R.id.userEmailTv);
                        userEmailTv.setText(email);
                    });
                }

                @Override
                public void onError(String error) {
                    // Обработка ошибок
                }
            });
        }
    }
}
