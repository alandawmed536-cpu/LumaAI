package com.example.ollamachat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.input);
        Button send = findViewById(R.id.send);
        TextView output = findViewById(R.id.output);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String prompt = input.getText().toString();
                output.setText("...جاري الإرسال") ;
                new Thread(() -> {
                    String res = callOllama(prompt);
                    mainHandler.post(() -> output.setText(res));
                }).start();
            }
        });
    }

    private String callOllama(String prompt) {
        try {
            String ollamaApi = "http://your-ollama-host:11434"; // instruct user to set proper host
            String model = "phi-3";

            String json = "{\"model\": \"" + model + "\", \"prompt\": \"" + escapeJson(prompt) + "\"}";
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(ollamaApi + "/api/generate")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) return "خطأ من الخادم: " + response.code();
                String text = response.body() != null ? response.body().string() : "";
                return text;
            }
        } catch (Exception e) {
            return "خطأ: " + e.getMessage();
        }
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
