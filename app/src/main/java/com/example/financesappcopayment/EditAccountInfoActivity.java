package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditAccountInfoActivity extends AppCompatActivity {

    EditText userNameTv, userEmailTv;
    Button saveBtn;
    ImageView backBtn;
    String accessToken;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_info);

        userEmailTv = findViewById(R.id.userEmailTv);
        userNameTv = findViewById(R.id.userNameTv);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        accessToken = PrefsUtils.getAccessToken(EditAccountInfoActivity.this); // ваш accessToken
        userId = PrefsUtils.getUserId(EditAccountInfoActivity.this); // ваш id пользователя

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAccountInfoActivity.this, AccountInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = userNameTv.getText().toString().trim();
                String newEmail = userEmailTv.getText().toString().trim();

                final int[] successCount = {0};

                if (accessToken != null && userId != null) {
                    // Обновление базы
                    new SupabaseClient().updateUserInDatabase(userId, newName, newEmail, accessToken, new SupabaseClient.CallbackListener() {
                        @Override
                        public void onSuccess(String message) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditAccountInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                                successCount[0]++;
                                if (successCount[0] == 2) {
                                    // Оба обновления прошли успешно, делаем переход
                                    Intent intent = new Intent(EditAccountInfoActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Log.e("UpdateError", "Ошибка обновления базы: " + error);
                                Toast.makeText(EditAccountInfoActivity.this, "Ошибка обновления базы: " + error, Toast.LENGTH_LONG).show();
                            });
                        }
                    });

                    // Обновление auth
                    new SupabaseClient().updateAuthUser(accessToken, newEmail, new SupabaseClient.CallbackListener() {
                        @Override
                        public void onSuccess(String message) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditAccountInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                                successCount[0]++;
                                if (successCount[0] == 2) {
                                    // Оба обновления прошли успешно, делаем переход
                                    Intent intent = new Intent(EditAccountInfoActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Log.e("UpdateError", "Ошибка обновления базы: " + error);
                                Toast.makeText(EditAccountInfoActivity.this, "Ошибка обновления базы: " + error, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                }
            }
        });

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