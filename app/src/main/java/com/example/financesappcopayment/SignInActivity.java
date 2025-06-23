package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private SupabaseClient supabaseClient;
    private TextView forgotPassTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        supabaseClient = new SupabaseClient();

        emailEditText = findViewById(R.id.EmailEdit);
        passwordEditText = findViewById(R.id.PasswordEdit);
        forgotPassTv = findViewById(R.id.forgotPass);

        forgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);

                if (isValidEmail(email)) {
                    intent.putExtra("email", email);
                } else {
                    intent.putExtra("email", "");
                }
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Пожалуйста, введите почту и пароль", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Вызов метода signIn
                supabaseClient.signIn("", email, password, new SupabaseClient.CallbackListener() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            try {
                                Log.d("SignIn", "Response message: " + message);
                                JsonObject responseJson = new Gson().fromJson(message, JsonObject.class);
                                String accessToken = responseJson.get("access_token").getAsString();
                                String userId = responseJson.getAsJsonObject("user").get("id").getAsString();

                                if (accessToken == null || userId == null) {
                                    Toast.makeText(SignInActivity.this, "Некорректный ответ от сервера", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // Сохраняем в Preferences
                                PrefsUtils.saveAccessToken(SignInActivity.this, accessToken);
                                PrefsUtils.saveUserId(SignInActivity.this, userId);

                                // Проверка PIN и дальнейшие действия...
                                new SupabaseClient().checkUserPin(userId, accessToken, new SupabaseClient.CallbackListener() {
                                    @Override
                                    public void onSuccess(String pin) {
                                        Intent intent;
                                        if (pin != null && !pin.isEmpty()) {
                                            intent = new Intent(SignInActivity.this, EnterPinActivity.class);
                                        } else {
                                            runOnUiThread(() -> {
                                                Toast.makeText(SignInActivity.this, "Упс! У вас ещё нет PIN", Toast.LENGTH_SHORT).show();
                                            });
                                            intent = new Intent(SignInActivity.this, PinActivity.class);
                                        }
                                        intent.putExtra("userId", userId);
                                        intent.putExtra("accessToken", accessToken);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        runOnUiThread(() -> {
                                            Toast.makeText(SignInActivity.this, "Ошибка: " + error, Toast.LENGTH_LONG).show();
                                        });
                                    }
                                });
                            } catch (Exception e) {
                                Log.e("SignIn", "Ошибка обработки ответа", e);
                                Toast.makeText(SignInActivity.this, "Произошла ошибка при обработке данных входа", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(SignInActivity.this, "Ошибка: " + error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            }
        });

        findViewById(R.id.signuptravel2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
        findViewById(R.id.signuptravel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }
    private boolean isValidEmail(CharSequence email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
