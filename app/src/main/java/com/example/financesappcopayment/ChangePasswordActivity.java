package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText passwordEdit, passwordConfirmEdit;
    private Button updatePasswordBtn, backBtn;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        passwordEdit = findViewById(R.id.passEt);
        passwordConfirmEdit = findViewById(R.id.passConfirmEt);
        updatePasswordBtn = findViewById(R.id.updatePasswordBtn);
        backBtn = findViewById(R.id.backBtn);

        final boolean[] isPasswordVisible = {false};

        accessToken = PrefsUtils.getAccessToken(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        updatePasswordBtn.setOnClickListener(v -> {
            String newPassword = passwordEdit.getText().toString().trim();
            String confirmPassword = passwordConfirmEdit.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите пароль и подтверждение", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            // Вызов метода для обновления пароля
            new SupabaseClient().updatePassword(accessToken, newPassword, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(ChangePasswordActivity.this, error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });

        passwordEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Проверяем, было ли касание по иконке
                if (event.getRawX() >= (passwordEdit.getRight() - passwordEdit.getCompoundPaddingRight())) {
                    // Переключение состояния
                    if (isPasswordVisible[0]) {
                        // Скрыть пароль
                        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordEdit.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, getResources().getDrawable(R.drawable.ic_eye_off), null);
                        isPasswordVisible[0] = false;
                    } else {
                        // Сделать пароль видимым
                        passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordEdit.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, getResources().getDrawable(R.drawable.ic_eye), null);
                        isPasswordVisible[0] = true;
                    }
                    
                    passwordEdit.setSelection(passwordEdit.getText().length());
                    return true; 
                }
            }
            return false;
        });

        passwordConfirmEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                
                if (event.getRawX() >= (passwordConfirmEdit.getRight() - passwordConfirmEdit.getCompoundPaddingRight())) {
                    // Переключение состояния
                    if (isPasswordVisible[0]) {
                        // Скрыть пароль
                        passwordConfirmEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordConfirmEdit.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, getResources().getDrawable(R.drawable.ic_eye_off), null);
                        isPasswordVisible[0] = false;
                    } else {
                        // Сделать пароль видимым
                        passwordConfirmEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordConfirmEdit.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, getResources().getDrawable(R.drawable.ic_eye), null);
                        isPasswordVisible[0] = true;
                    }
                    
                    passwordConfirmEdit.setSelection(passwordConfirmEdit.getText().length());
                    return true; 
                }
            }
            return false;
        });
    }
}
