package com.example.qua.quasarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default shit
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Бинды онкликеров на кнопки мейн активити
        Button button1 = (Button) findViewById(R.id.button_first_pair);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              sendRequestAndPrintResponse("https://api.cryptonator.com/api/ticker/rub-eth",
                        R.id.display_first_pair);
            }
        });
        Button button2 = (Button) findViewById(R.id.button_second_pair);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRequestAndPrintResponse("https://api.cryptonator.com/api/ticker/rub-waves",
                        R.id.display_second_pair);
            }
        });
        Button button3 = (Button) findViewById(R.id.button_qr_gen);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QRCodeGenerator.class));
            }
        });

    }

    // Функция для отправки get запроса к api биржи с курсами крипты и обновление textview-сов
    private void sendRequestAndPrintResponse(String url, final int button_id) {
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Если запрос удалось отправить и ответ получен
                TextView text = (TextView) findViewById(button_id);
                text.setText("Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Если запрос удалось отправить но ответ не положительный (либо не удалось отправить)
                TextView text = (TextView) findViewById(button_id);
                text.setText("Error: " + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
