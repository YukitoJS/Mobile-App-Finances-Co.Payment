package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullNameEdit, emailEdit, passwordEdit;
    private Button signUpButton;
    private SupabaseClient supabaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fullNameEdit = findViewById(R.id.fullNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        signUpButton = findViewById(R.id.signInButton);

        supabaseClient = new SupabaseClient();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRegister();
            }
        });

        findViewById(R.id.signintravel2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
        findViewById(R.id.signintravel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
    }

    private void validateAndRegister() {
        String fullName = fullNameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString();

        boolean isValid = true;

        // Валидация имени
        if (TextUtils.isEmpty(fullName)) {
            fullNameEdit.setError("Пожалуйста, введите ваше имя");
            isValid = false;
        } else if (!isValidName(fullName)) {
            fullNameEdit.setError("Некорректное имя");
            isValid = false;
        } else {
            fullNameEdit.setError(null);
        }

        // Валидация почты
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Пожалуйста, введите email");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.setError("Некорректный email");
            isValid = false;
        } else {
            emailEdit.setError(null);
        }

        // Валидация пароля
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Пожалуйста, введите пароль");
            isValid = false;
        } else if (password.length() < 6) {
            passwordEdit.setError("Пароль должен содержать не менее 6 символов");
            isValid = false;
        } else {
            passwordEdit.setError(null);
        }

        if (isValid) {
            // Выполняем регистрацию через supabase
            supabaseClient.signUp(fullName, email, password, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this, "Регистрация прошла успешно! Не забудьте подтвердить почту.", Toast.LENGTH_SHORT).show();
                        // Можно сохранить данные или перейти дальше
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this, "Ошибка регистрации: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }
    private boolean isValidName(String name) {
        String namePattern = "^[a-zA-Zа-яА-ЯёЁ\\s'-]+$";
        return name.matches(namePattern);
    }

}