package com.example.financesappcopayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    String accessToken;
    TextView userNameTextView;
    TextView userEmailTextView;
    ImageView backBtn;
    LinearLayout homeTravelBtn, myCardTravelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameTextView=findViewById(R.id.userNameTv);
        userEmailTextView=findViewById(R.id.userEmailTv);
        backBtn = findViewById(R.id.backBtn);
        homeTravelBtn = findViewById(R.id.homeTravelBtn);
        myCardTravelBtn = findViewById(R.id.cardTravelBtn);

        accessToken = PrefsUtils.getAccessToken(ProfileActivity.this);

        ListView listView = findViewById(R.id.listView);
        List<Profile> profiles = new ArrayList<>();
   
        profiles.add(new Profile(R.drawable.account_info, "Account Info", R.drawable.chevron_right));
        profiles.add(new Profile(R.drawable.lock_ico, "Change Password", R.drawable.chevron_right));
        profiles.add(new Profile(R.drawable.scan_ico, "Change Log In PIN", R.drawable.chevron_right));
        profiles.add(new Profile(R.drawable.quastion_ico, "FAQs", R.drawable.chevron_right));

        ProfileAdapter adapter = new ProfileAdapter(this, profiles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        // Переход к экрану "Account Info"
                        intent = new Intent(ProfileActivity.this, AccountInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // Переход к экрану "Change Password"
                        intent = new Intent(ProfileActivity.this, EnterPinForChangePasswordActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        // Переход к экрану "Change Log In PIN"
                        intent = new Intent(ProfileActivity.this, ChangePinActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        // Переход к экрану "FAQs"
                        Toast.makeText(ProfileActivity.this, "В разработке...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        String userId = PrefsUtils.getUserId(ProfileActivity.this);
        if (accessToken != null && userId != null) {
            new SupabaseClient().getUserName(userId, accessToken, new SupabaseClient.CallbackListener() {
                @Override
                public void onSuccess(String name) {
                    runOnUiThread(() -> {
                        userNameTextView = findViewById(R.id.userNameTv);
                        userNameTextView.setText(name);
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
                        userEmailTextView = findViewById(R.id.userEmailTv);
                        userEmailTextView.setText(email);
                    });
                }

                @Override
                public void onError(String error) {
                    // Обработка ошибок
                }
            });
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myCardTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyCardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
