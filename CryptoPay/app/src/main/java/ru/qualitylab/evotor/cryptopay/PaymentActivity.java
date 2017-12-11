package ru.qualitylab.evotor.cryptopay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {


    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
//
        Intent thisintent = getIntent();
        Bundle bund = thisintent.getExtras();

        final TextView textview_adress = (TextView) findViewById(R.id.textview_address);
        final TextView textview_amount = (TextView) findViewById(R.id.textview_amount);

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (bund != null) {
            final String address = getIntent().getExtras().getString("address");
            final String amount = getIntent().getExtras().getString("amount");

            textview_adress.setText(address);
            textview_amount.setText(amount);
            sendRequestAndGetWAVExchageRate();


            image = (ImageView) findViewById(R.id.image);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(address, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                image.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        else
        {
            textview_adress.setText("Error");
        }


    }


    private void sendRequestAndGetWAVExchageRate() {
        try {
            RequestQueue mRequestQueue;
            StringRequest mStringRequest;
            mRequestQueue = Volley.newRequestQueue(this);
            String url = "https://api.cryptonator.com/api/ticker/rub-waves";

            mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Если запрос удалось отправить и ответ получен
                    TextView text = (TextView) findViewById(R.id.textview_amount);
                    String jsonresponse = response.toString();
                    try {
                        final JSONObject obj = new JSONObject(jsonresponse);
                        final JSONObject objj = obj.getJSONObject("ticker");
                        Double something = objj.getDouble("price");

                        text.setText(Double.toString(something *
                                Double.parseDouble(((TextView) findViewById(R.id.textview_amount)).getText().toString())));
                    } catch (Exception e) {
                        text.setText(e.getCause() + e.getMessage());
//                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Если запрос удалось отправить но ответ не положительный (либо не удалось отправить)
                    finish();
                }
            });
            mRequestQueue.add(mStringRequest);
        } catch (Exception e) {
            TextView text = (TextView) findViewById(R.id.textview_amount);
            text.setText(e.getCause() + " " + e.getMessage());
        }
    }

}
