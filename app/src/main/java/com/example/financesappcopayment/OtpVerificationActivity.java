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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText[] etDigit = new EditText[6];
    private Button confirmButton, backBtn;
    private TextView resendTv, userEmailTv;
    private SupabaseClient supabaseClient;

    private String emailOrPhone, displayEmail;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        supabaseClient = new SupabaseClient();

        etDigit[0] = findViewById(R.id.et_digit1);
        etDigit[1] = findViewById(R.id.et_digit2);
        etDigit[2] = findViewById(R.id.et_digit3);
        etDigit[3] = findViewById(R.id.et_digit4);
        etDigit[4] = findViewById(R.id.et_digit5);
        etDigit[5] = findViewById(R.id.et_digit6);
        resendTv = findViewById(R.id.resendTv);
        confirmButton = findViewById(R.id.createPinButton);
        backBtn = findViewById(R.id.backBtn);

        setupEditTexts();

        emailOrPhone = getIntent().getStringExtra("email");
        type = "email";

        if (emailOrPhone != null && emailOrPhone.contains("@")) {
            int atIndex = emailOrPhone.indexOf("@");
            String namePart = emailOrPhone.substring(0, atIndex);
            String domainPart = emailOrPhone.substring(atIndex);

            
            int visibleChars = Math.min(2, namePart.length());
            String visiblePart = namePart.substring(0, visibleChars);
            String maskedPart = new String(new char[namePart.length() - visibleChars]).replace("\0", "*");

            displayEmail = visiblePart + maskedPart + domainPart;
        } else {
            
            displayEmail = emailOrPhone != null ? emailOrPhone : "";
        }

        userEmailTv = findViewById(R.id.useremailTv);
        userEmailTv.setText("( " + displayEmail + " ).");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpVerificationActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        resendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SupabaseClient().recoverPassword(emailOrPhone, new SupabaseClient.CallbackListener() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(OtpVerificationActivity.this, message, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(OtpVerificationActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            }
        });

        confirmButton.setOnClickListener(v -> {
            String enteredPin = "";
            for (EditText et : etDigit) {
                String digit = et.getText().toString();
                if (digit.isEmpty()) {
                    Toast.makeText(this, "Введите все цифры PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                enteredPin += digit;
            }

            // Вызов проверки OTP
            supabaseClient.verifyOtpCode(emailOrPhone, type, enteredPin, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String accessToken) {
                    runOnUiThread(() -> {
                        PrefsUtils.saveAccessToken(OtpVerificationActivity.this, accessToken);
                        Toast.makeText(OtpVerificationActivity.this, "Успешно!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OtpVerificationActivity.this, EnterNewPasswordActivity.class);
                        intent.putExtra("ACCESS_TOKEN", accessToken);
                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(OtpVerificationActivity.this, error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }
    private void setupEditTexts() {
        for (int i = 0; i < etDigit.length; i++) {
            final int index = i;
            etDigit[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            etDigit[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            etDigit[i].setTransformationMethod(new PasswordTransformationMethod());

            etDigit[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < etDigit.length - 1) {
                        etDigit[index + 1].requestFocus();
                    }
                }
            });

            // Обработка Backspace
            etDigit[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (etDigit[index].getText().length() == 0 && index > 0) {
                        etDigit[index - 1].requestFocus();
                        etDigit[index - 1].setText("");
                        return true;
                    }
                }
                return false;
            });
        }
    }
}
