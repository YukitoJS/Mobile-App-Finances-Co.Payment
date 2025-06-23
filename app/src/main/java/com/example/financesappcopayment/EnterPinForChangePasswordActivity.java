package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EnterPinForChangePasswordActivity extends AppCompatActivity {

    private EditText[] editTexts = new EditText[5];
    private Button enterButton, backBtn;

    private String userId;
    private String accessToken;
    private String correctPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_for_change_password);

        userId = PrefsUtils.getUserId(this);
        accessToken = PrefsUtils.getAccessToken(this);

        editTexts[0] = findViewById(R.id.et_digit1);
        editTexts[1] = findViewById(R.id.et_digit2);
        editTexts[2] = findViewById(R.id.et_digit3);
        editTexts[3] = findViewById(R.id.et_digit4);
        editTexts[4] = findViewById(R.id.et_digit5);
        backBtn = findViewById(R.id.backBtn);
        enterButton = findViewById(R.id.createPinButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterPinForChangePasswordActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setupEditTexts();

        // Получаем правильный PIN из базы
        fetchUserPin();

        enterButton.setOnClickListener(v -> {
            String enteredPin = "";
            for (EditText et : editTexts) {
                String digit = et.getText().toString();
                if (digit.isEmpty()) {
                    Toast.makeText(this, "Введите все цифры PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                enteredPin += digit;
            }
            // Проверяем введенный PIN
            if (enteredPin.equals(correctPin)) {
                // Успешный вход
                Toast.makeText(this, "PIN правильный", Toast.LENGTH_SHORT).show();
                goToChangePassword();
            } else {
                Toast.makeText(this, "Неверный PIN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchUserPin() {
        new SupabaseClient().checkUserPin(userId, accessToken, new SupabaseClient.CallbackListener() {
            @Override
            public void onSuccess(String pin) {
                correctPin = pin; // запоминаем правильный PIN
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EnterPinForChangePasswordActivity.this, "Ошибка получения PIN: " + error, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void setupEditTexts() {
        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;
            editTexts[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            editTexts[i].setTransformationMethod(new PasswordTransformationMethod());

            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    }
                }
            });

            // Обработка Backspace
            editTexts[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (editTexts[index].getText().length() == 0 && index > 0) {
                        editTexts[index - 1].requestFocus();
                        editTexts[index - 1].setText("");
                        return true;
                    }
                }
                return false;
            });
        }
    }

    private void goToChangePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
