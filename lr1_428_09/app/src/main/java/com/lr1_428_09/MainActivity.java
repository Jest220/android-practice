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

public class MainActivity extends AppCompatActivity {

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

                if (eps <= 0) {
                    Toast.makeText(this, R.string.error_input, Toast.LENGTH_SHORT).show();
                    return;
                }

                BisectionResult result = findRootByBisection(a0, a1, a2, fx, left, right, eps, 10_000);
                if (!result.success) {
                    Toast.makeText(this, R.string.error_no_root, Toast.LENGTH_SHORT).show();
                    return;
                }

                tvX.setText(getString(R.string.result_x) + " " + String.format("%.6f", result.x));
                tvErr.setText(getString(R.string.result_err) + " " + String.format("%.6g", result.error));
            } catch (Exception e) {
                Toast.makeText(this, R.string.error_input, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double parseOrThrow(CharSequence s) {
        if (s == null || TextUtils.isEmpty(s.toString().trim())) throw new IllegalArgumentException();
        return Double.parseDouble(s.toString().trim());
    }

    private double polynomial(double a0, double a1, double a2, double x) {
        return a0 + a1 * x + a2 * x * x;
    }

    private BisectionResult findRootByBisection(double a0, double a1, double a2, double fValue,
                                                double left, double right, double eps, int maxIter) {
        double fLeft = polynomial(a0, a1, a2, left) - fValue;
        double fRight = polynomial(a0, a1, a2, right) - fValue;
        if (Double.isNaN(fLeft) || Double.isNaN(fRight) || fLeft * fRight > 0) {
            return new BisectionResult(false, Double.NaN, Double.NaN);
        }

        double mid = left;
        double err = Math.abs(right - left);
        int iter = 0;
        while (err > eps && iter < maxIter) {
            mid = 0.5 * (left + right);
            double fMid = polynomial(a0, a1, a2, mid) - fValue;

            if (Math.abs(fMid) < eps) {
                err = Math.abs(fMid);
                break;
            }

            if (fLeft * fMid <= 0) {
                right = mid;
                fRight = fMid;
            } else {
                left = mid;
                fLeft = fMid;
            }
            err = Math.abs(right - left);
            iter++;
        }
        return new BisectionResult(true, mid, err);
    }

    private static class BisectionResult {
        final boolean success;
        final double x;
        final double error;

        BisectionResult(boolean success, double x, double error) {
            this.success = success;
            this.x = x;
            this.error = error;
        }
    }
}