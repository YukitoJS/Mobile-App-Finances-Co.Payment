package com.example.financesappcopayment;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseClient {

    private static final String BASE_URL = "https://gzehzgogrxtkutycvstw.supabase.co";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd6ZWh6Z29ncnh0a3V0eWN2c3R3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDkwMzQ5MTksImV4cCI6MjA2NDYxMDkxOX0.RQGM4ICJWadfsIHqOgpfQqX-zqWpf7m9fgwymS4VceI";
    private static final String API_KEY_ADMIN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd6ZWh6Z29ncnh0a3V0eWN2c3R3Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0OTAzNDkxOSwiZXhwIjoyMDY0NjEwOTE5fQ.ec-ZGF4JwntGrJBkL6vHmLJllah1izIeS1qQYnnmA_4";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private Gson gson;

    public interface CallbackListener {
        void onSuccess(String message);
        void onError(String error);
    }
    public interface SavePinCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    public interface CardsCallback {
        void onSuccess(List<CardItem> cards);
        void onError(String error);
    }
    public interface SignUpCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public SupabaseClient() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void savePin(String userId, String pin, String accessToken, SavePinCallback callback) {
        String url = BASE_URL + "/rest/v1/users"; // таблица users

        // Создаем JSON с данными
        JsonObject json = new JsonObject();
        json.addProperty("id", userId);
        json.addProperty("pin_code", pin);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        // Используем PATCH для обновления по id
        String queryUrl = url + "?id=eq." + userId;

        Request request = new Request.Builder()
                .url(queryUrl)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    callback.onSuccess("PIN успешно сохранен");
                } else {
                    callback.onError("Ошибка сохранения PIN: " + responseBody);
                }
            }
        });
    }

    public void checkUserPin(String userId, String accessToken, SupabaseClient.CallbackListener callback) {
        String url = BASE_URL + "/rest/v1/users?id=eq." + userId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JsonArray jsonArray = new Gson().fromJson(body, JsonArray.class);
                    if (jsonArray.size() > 0) {
                        JsonObject userData = jsonArray.get(0).getAsJsonObject();
                        if (userData.has("pin_code") && !userData.get("pin_code").isJsonNull()) {
                            String pin = userData.get("pin_code").getAsString();
                            callback.onSuccess(pin);
                        } else {
                            callback.onSuccess(null);
                        }
                    } else {
                        callback.onError("Пользователь не найден");
                    }
                } else {
                    callback.onError("Ошибка получения данных: " + response.body().string());
                }
            }
        });
    }

    public void getUserName(String userId, String accessToken, CallbackListener callback) {
        String url = BASE_URL + "/rest/v1/users?id=eq." + userId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JsonArray jsonArray = new Gson().fromJson(body, JsonArray.class);
                    if (jsonArray.size() > 0) {
                        JsonObject userData = jsonArray.get(0).getAsJsonObject();
                        if (userData.has("name")) {
                            String name = userData.get("name").getAsString();
                            callback.onSuccess(name);
                        } else {
                            callback.onError("Имя пользователя не найдено");
                        }
                    } else {
                        callback.onError("Пользователь не найден");
                    }
                } else {
                    callback.onError("Ошибка получения данных");
                }
            }
        });
    }

    public void getUserEmail(String userId, String accessToken, CallbackListener callback) {
        String url = BASE_URL + "/rest/v1/users?id=eq." + userId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JsonArray jsonArray = new Gson().fromJson(body, JsonArray.class);
                    if (jsonArray.size() > 0) {
                        JsonObject userData = jsonArray.get(0).getAsJsonObject();
                        if (userData.has("email")) {
                            String email = userData.get("email").getAsString();
                            callback.onSuccess(email);
                        } else {
                            callback.onError("Почта пользователя не найдено");
                        }
                    } else {
                        callback.onError("Пользователь не найден");
                    }
                } else {
                    callback.onError("Ошибка получения данных");
                }
            }
        });
    }

    public void recoverPassword(String email, CallbackListener callback) {
        String url = BASE_URL + "/auth/v1/recover";

        JsonObject json = new JsonObject();
        json.addProperty("email", email);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    callback.onSuccess("Письмо для восстановления пароля отправлено");
                } else {
                    // Обычно если пользователя нет, возвращается ошибка
                    callback.onError("Ошибка: " + responseBody);
                }
            }
        });
    }

    public void verifyOtpCode(String emailOrPhone, String type, String token, CallbackListener callback) {
        String url = BASE_URL + "/auth/v1/verify";

        JsonObject json = new JsonObject();
        json.addProperty("type", type); // "email" или "sms"
        if (type.equals("email")) {
            json.addProperty("email", emailOrPhone);
        } else if (type.equals("sms")) {
            json.addProperty("phone", emailOrPhone);
        }
        json.addProperty("token", token);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                    String accessToken = jsonResponse.has("access_token")
                            ? jsonResponse.get("access_token").getAsString()
                            : null;
                    if (accessToken != null) {
                        callback.onSuccess(accessToken);
                    } else {
                        callback.onError("Не получен access_token");
                    }
                } else {
                    String errorBody = response.body().string();
                    callback.onError("Ошибка подтверждения: " + errorBody);
                }
            }
        });
    }

    public void updatePassword(String userToken, String newPassword, CallbackListener callback) {
        String url = BASE_URL + "/auth/v1/user";

        JsonObject json = new JsonObject();
        json.addProperty("password", newPassword);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + userToken)
                .addHeader("Content-Type", "application/json")
                .put(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess("Пароль успешно изменен");
                } else {
                    callback.onError("Ошибка обновления пароля: " + response.body().string());
                }
            }
        });
    }

    public void signUp(String fullName, String email, String password, CallbackListener listener) {
        String url = BASE_URL + "/auth/v1/signup";

        JsonObject json = new JsonObject();
        json.addProperty("name", fullName);
        json.addProperty("email", email);
        json.addProperty("password", password);


        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() -> listener.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        if (jsonResponse.has("id")) {
                            String id = jsonResponse.getString("id");
                            // добавляем пользователя в таблицу users
                            String password_hash = null;
                            String pin_code = null;
                            String avatar_url = null;
                            String created_at = null;
                            addUserToDatabase(id, email, password_hash, pin_code, fullName, avatar_url, created_at);
                            runOnMainThread(() -> listener.onSuccess("Регистрация прошла успешно"));
                        } else {
                            runOnMainThread(() -> listener.onError("Нет id в ответе"));
                        }
                    } catch (JSONException e) {
                        runOnMainThread(() -> listener.onError("Ошибка парсинга: " + e.getMessage()));
                    }
                } else {
                    runOnMainThread(() -> listener.onError("Ошибка: " + responseBody));
                }
            }

            private void runOnMainThread(Runnable runnable) {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        });
    }

    public void addCardToAccounts(String userId, String cardNumber, String expiryDate, String cardHolderName, CallbackListener callback) {
        String url = BASE_URL + "/rest/v1/accounts";

        JsonObject json = new JsonObject();
        json.addProperty("user_id", userId);
        json.addProperty("card_number", cardNumber);
        json.addProperty("expiry_date", expiryDate);
        json.addProperty("card_holder_name", cardHolderName);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() -> callback.onError("Ошибка при добавлении карты: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respStr = response.body().string();
                if (response.isSuccessful()) {
                    runOnMainThread(() -> callback.onSuccess("Карта успешно добавлена"));
                } else {
                    runOnMainThread(() -> callback.onError("Ошибка: " + respStr));
                }
            }
        });
    }

    private void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    private void addUserToDatabase(String id, String email, String password_hash, String pin_code, String name, String avatar_url, String created_at) {
        String url = BASE_URL + "/rest/v1/users";

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("email", email);
        json.addProperty("password_hash", password_hash);
        json.addProperty("pin_code", pin_code);
        json.addProperty("name", name);
        json.addProperty("avatar_url", avatar_url);
        json.addProperty("created_at", created_at);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AddUser", "Ошибка при добавлении пользователя", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("AddUser", "Ошибка ответа: " + response.code() + " " + response.body().string());
                }
            }
        });
    }

    public void signIn(String fullName, String email, String password, CallbackListener listener) {
        String url = BASE_URL + "/auth/v1/token?grant_type=password";

        JsonObject json = new JsonObject();
        json.addProperty("name", fullName);
        json.addProperty("email", email);
        json.addProperty("password", password);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        executeRequest(request, listener);
    }

    public void updateUserInDatabase(String userId, String newName, String newEmail, String accessToken, CallbackListener callback) {
        String url = BASE_URL + "/rest/v1/users?id=eq." + userId;

        JsonObject json = new JsonObject();
        json.addProperty("name", newName);
        json.addProperty("email", newEmail);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess("Данные успешно обновлены");
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Пустой ответ";
                    callback.onError("Ошибка обновления: " + errorBody);
                }
            }
        });
    }

    public void updateAuthUser(String userToken, String newEmail, CallbackListener callback) {
        String url = BASE_URL + "/auth/v1/user";

        JsonObject json = new JsonObject();
        json.addProperty("email", newEmail);

        RequestBody body = RequestBody.create(gson.toJson(json), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY_ADMIN)
                .addHeader("Authorization", "Bearer " + userToken)
                .addHeader("Content-Type", "application/json")
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess("Профиль обновлен");
                } else {
                    callback.onError("Ошибка: " + response.body().string());
                }
            }
        });
    }

    public void getUserBalance(String userId, String accessToken, CallbackListener callback) {
        String url = BASE_URL + "/rest/v1/accounts?user_id=eq." + userId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Range", "0-0") // чтобы получить только один элемент
                .addHeader("Accept", "application/json")
                .get()
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JsonArray jsonArray = new Gson().fromJson(body, JsonArray.class);
                    if (jsonArray.size() > 0) {
                        JsonObject accountObj = jsonArray.get(0).getAsJsonObject();
                        if (accountObj.has("balance")) {
                            double balance = accountObj.get("balance").getAsDouble();
                            callback.onSuccess(String.valueOf(balance));
                        } else {
                            callback.onError("Баланс не найден");
                        }
                    } else {
                        callback.onError("Аккаунт не найден");
                    }
                } else {
                    callback.onError("Ошибка: " + response.body().string());
                }
            }
        });
    }

    public void hasUserAccount(String userId, String accessToken, CallbackListener callback) {
        getUserBalance(userId, accessToken, new CallbackListener() {
            @Override
            public void onSuccess(String message) {
                // Если получили баланс, значит счет есть
                callback.onSuccess("true");
            }

            @Override
            public void onError(String error) {
                // Если ошибка, например, аккаунт не найден, возвращаем false
                if (error.contains("Аккаунт не найден")) {
                    callback.onSuccess("false");
                } else {
                    // Другие ошибки — передаём их
                    callback.onError(error);
                }
            }
        });
    }

    public void getUserCards(String userId, String accessToken, CardsCallback callback) {
        String url = BASE_URL + "/rest/v1/accounts?user_id=eq." + userId; // фильтр по userId

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .get()
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        JsonArray jsonArray = new Gson().fromJson(body, JsonArray.class);
                        List<CardItem> cards = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject obj = jsonArray.get(i).getAsJsonObject();
                            String cardNumberRaw = obj.has("card_number") ? obj.get("card_number").getAsString() : "";
                            String formattedCardNumber = formatCardNumber(cardNumberRaw);
                            String expiryDate = obj.has("expiry_date") ? obj.get("expiry_date").getAsString() : "";
                            String cardHolderName = obj.has("card_holder_name") ? obj.get("card_holder_name").getAsString() : "";
                            cards.add(new CardItem(formattedCardNumber, expiryDate, cardHolderName));
                        }
                        callback.onSuccess(cards);
                    } catch (Exception e) {
                        callback.onError("Ошибка парсинга: " + e.getMessage());
                    }
                } else {
                    callback.onError("Ошибка получения карт: " + response.body().string());
                }
            }
        });
    }
    private String formatCardNumber(String cardNumber) {
        // Удаляем все ненужные символы, например, пробелы, если они есть
        String cleaned = cardNumber.replaceAll("\\s+", "");

        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < cleaned.length(); i++) {
            // Добавляем текущий символ
            formatted.append(cleaned.charAt(i));
            // После каждых 4 символов добавляем пробел, кроме последнего
            if ((i + 1) % 4 == 0 && i != cleaned.length() - 1) {
                formatted.append(" ");
            }
        }
        return formatted.toString();
    }


    private void executeRequest(Request request, CallbackListener listener) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обработка ошибок сети
                listener.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Можно обработать тело ответа или вернуть токен
                    String responseBody = response.body().string();
                    listener.onSuccess(responseBody);
                } else {
                    // Обработка ошибок API
                    String errorBody = response.body().string();
                    listener.onError(errorBody);
                }
            }
        });
    }
}
