package com.lr1_428_09;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String API_URL = "https://bisection-api.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputEditText etLeft = findViewById(R.id.etLeft);
        TextInputEditText etRight = findViewById(R.id.etRight);
        TextInputEditText etA0 = findViewById(R.id.etA0);
        TextInputEditText etA1 = findViewById(R.id.etA1);
        TextInputEditText etA2 = findViewById(R.id.etA2);
        TextInputEditText etFx = findViewById(R.id.etFx);
        TextInputEditText etEps = findViewById(R.id.etEps);

        Button btnCompute = findViewById(R.id.btnCompute);
        TextView tvX = findViewById(R.id.tvX);
        TextView tvErr = findViewById(R.id.tvErr);

        btnCompute.setOnClickListener(v -> {
            try {
                double left = parseOrThrow(etLeft.getText());
                double right = parseOrThrow(etRight.getText());
                double a0 = parseOrThrow(etA0.getText());
                double a1 = parseOrThrow(etA1.getText());
                double a2 = parseOrThrow(etA2.getText());
                double fx = parseOrThrow(etFx.getText());
                double eps = parseOrThrow(etEps.getText());

                if (left > right) {
                    Toast.makeText(this, getString(R.string.error_input_interval), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (eps < 0.000001 || eps >= 1) {
                    Toast.makeText(this, getString(R.string.error_input_eps), Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject json = new JSONObject();
                json.put("left", left);
                json.put("right", right);
                json.put("a0", a0);
                json.put("a1", a1);
                json.put("a2", a2);
                json.put("fValue", fx);
                json.put("eps", eps);
                json.put("maxIter", 10000);

                sendApiRequest(json, tvX, tvErr);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double parseOrThrow(CharSequence s) {
        if (s == null || TextUtils.isEmpty(s.toString().trim()))
            throw new IllegalArgumentException();
        return Double.parseDouble(s.toString().trim());
    }

    private void sendApiRequest(JSONObject json, TextView tvX, TextView tvErr) {
        new Thread(() -> {
            try {
                RequestBody body = RequestBody.create(json.toString(), JSON);
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String respStr = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    final String finalRespStr = respStr;
                    runOnUiThread(() -> Toast.makeText(this,
                            getString(R.string.error_http) + ": " + response.code() + "\n" + finalRespStr,
                            Toast.LENGTH_LONG).show());
                    return;
                }

                if (respStr.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this,
                            getString(R.string.error_empty_response),
                            Toast.LENGTH_LONG).show());
                    return;
                }

                try {
                    JSONObject respJson = new JSONObject(respStr);
                    boolean success = respJson.optBoolean("success", false);
                    if (success) {
                        double root = respJson.optDouble("root", Double.NaN);
                        double error = respJson.optDouble("error", Double.NaN);
                        runOnUiThread(() -> {
                            tvX.setText(getString(R.string.result_x) + " " + String.format("%.6f", root));
                            tvErr.setText(getString(R.string.result_err) + " " + String.format("%.6g", error));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this,
                                getString(R.string.error_no_root) + "\nJSON: " + respStr,
                                Toast.LENGTH_LONG).show());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    final String finalRespStr = respStr;
                    runOnUiThread(() -> Toast.makeText(this,
                            getString(R.string.error_json_parse) + "\nСервер вернул:\n" + finalRespStr,
                            Toast.LENGTH_LONG).show());
                }

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this,
                        getString(R.string.error_network),
                        Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this,
                        getString(R.string.error_json_parse) + ": " + e.getMessage(),
                        Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
