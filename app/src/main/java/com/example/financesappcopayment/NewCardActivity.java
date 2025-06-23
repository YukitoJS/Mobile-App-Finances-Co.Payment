package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class NewCardActivity extends AppCompatActivity {

    EditText cardNumberEt, cardHolderNameEt, expiryDateEt, vccEt;
    Button saveBtn;
    CardView cardView;
    SupabaseClient supabaseClient;

    String accessToken, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        cardNumberEt = findViewById(R.id.cardNumberEt);
        cardHolderNameEt = findViewById(R.id.cardHolderNameEt);
        expiryDateEt = findViewById(R.id.expiryDateEt);
        vccEt = findViewById(R.id.vccEt);
        saveBtn = findViewById(R.id.saveBtn);
        cardView = findViewById(R.id.userCard);

        cardView.setBackgroundResource(R.drawable.card_background);

        supabaseClient = new SupabaseClient();

        accessToken = PrefsUtils.getAccessToken(this);
        userId = PrefsUtils.getUserId(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String cardNumberRaw = getCardNumberWithoutSpaces();
                    String expiry = expiryDateEt.getText().toString().trim();
                    String cardHolder = cardHolderNameEt.getText().toString().trim();

                    supabaseClient.addCardToAccounts(userId, cardNumberRaw, expiry, cardHolder, new SupabaseClient.CallbackListener() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(NewCardActivity.this, message, Toast.LENGTH_SHORT).show();
                            showCardReadyPopup();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(NewCardActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        expiryDateEt.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }
                String str = s.toString().replace("/", "").replaceAll("[^\\d]", ""); // Убираем все, кроме цифр
                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < str.length(); i++) {
                    if (i == 2) {
                        formatted.append("/");
                    }
                    formatted.append(str.charAt(i));
                }

                isUpdating = true;
                expiryDateEt.setText(formatted.toString());
                expiryDateEt.setSelection(expiryDateEt.getText().length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cardNumberEt.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }
                String str = s.toString().replace(" ", "");
                StringBuilder formatted = new StringBuilder();

                int index = 0;
                while (index < str.length()) {
                    if (index + 4 <= str.length()) {
                        formatted.append(str.substring(index, index + 4)).append(" ");
                    } else {
                        formatted.append(str.substring(index));
                    }
                    index += 4;
                }

                isUpdating = true;
                cardNumberEt.setText(formatted.toString().trim());
                cardNumberEt.setSelection(cardNumberEt.getText().length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public String getCardNumberWithoutSpaces() {
        String rawInput = cardNumberEt.getText().toString();
        return rawInput.replaceAll(" ", "");
    }

    private void showCardReadyPopup() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_card_ready, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button okButton = dialogView.findViewById(R.id.okButton);

        okButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyCardActivity.class);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }
}