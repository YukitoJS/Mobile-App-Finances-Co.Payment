package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PinActivity extends AppCompatActivity {

    private EditText[] editTexts = new EditText[5];
    private Button createPinButton;

    private String userId;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pin);

        userId = getIntent().getStringExtra("userId");
        accessToken = getIntent().getStringExtra("accessToken");

        if (userId == null || accessToken == null) {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTexts[0] = findViewById(R.id.et_digit1);
        editTexts[1] = findViewById(R.id.et_digit2);
        editTexts[2] = findViewById(R.id.et_digit3);
        editTexts[3] = findViewById(R.id.et_digit4);
        editTexts[4] = findViewById(R.id.et_digit5);

        createPinButton = findViewById(R.id.createPinButton);
        setupEditTexts();

        createPinButton.setOnClickListener(v -> {
            String pin = "";
            for (EditText et : editTexts) {
                String digit = et.getText().toString();
                if (digit.isEmpty()) {
                    Toast.makeText(this, "Введите все цифры PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                pin += digit;
            }
            savePinAndProceed(pin);
        });
    }

    private void setupEditTexts() {
        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;

            // Ограничение длины
            editTexts[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            // Ввод только цифр
            editTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            // Маскировка
            editTexts[i].setTransformationMethod(new PasswordTransformationMethod());

            // Автоматический переход
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

    private void savePinAndProceed(String pin) {
        // Вызов метода сохранения PIN
        new SupabaseClient().savePin(userId, pin, accessToken, new SupabaseClient.SavePinCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(PinActivity.this, message, Toast.LENGTH_SHORT).show();е
                    goToMainScreen();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(PinActivity.this, "Ошибка: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
