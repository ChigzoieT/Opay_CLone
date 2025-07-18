package com.pay.opay.resolver;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.pay.opay.AccountInfo;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BankResolver {
    private static final String TAG = "BankResolver";
    private static final String BASE_URL = "https://nubapi.com/api/verify";
    private static final String BEARER_TOKEN = "FKJj8DDSmWTQCcS3RlTp5VYWPk8putFvPpL8rItxb45ddce2";
    private final Handler backgroundHandler;
    private final Handler mainHandler;

    public BankResolver() {
        HandlerThread thread = new HandlerThread("BankResolverThread");
        thread.start();
        backgroundHandler = new Handler(thread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void resolveAccountName(String accountNumber, String bankCode) {
        backgroundHandler.post(() -> {
            String result;
            int status;

            try {
                String query = "?account_number=" + URLEncoder.encode(accountNumber, "UTF-8") +
                        "&bank_code=" + URLEncoder.encode(bankCode, "UTF-8");

                URL url = new URL(BASE_URL + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    JSONObject json = new JSONObject(response.toString());

                    result = json.optString("account_name", "Account name not found");
                    status = 1; // Success

                    //Log.d(TAG, "Account Name: " + json.optString("account_name"));
                    //Log.d(TAG, "First Name: " + json.optString("first_name"));
                    //Log.d(TAG, "Last Name: " + json.optString("last_name"));
                    //Log.d(TAG, "Other Name: " + json.optString("other_name"));
                    //Log.d(TAG, "Account Number: " + json.optString("account_number"));
                    //Log.d(TAG, "Bank Code: " + json.optString("bank_code"));
                    //Log.d(TAG, "Bank Name: " + json.optString("Bank_name"));

                } else {
                    result = "Error: " + responseCode;
                    status = 2; // Error
                    Log.e(TAG, "HTTP error: " + responseCode);
                }

                conn.disconnect();
            } catch (Exception e) {
                result = "Exception: " + e.getMessage();
                status = 2;
                Log.e(TAG, "Exception: ", e);
            }

            final String finalResult = result;
            final int finalStatus = status;

            mainHandler.post(() -> {
                AccountInfo info = AccountInfo.getInstance();
                info.setUserAccount(finalResult);
                info.setResponse(finalStatus);
            });
        });
    }
}
