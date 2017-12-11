package ru.qualitylab.evotor.cryptopay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreatePaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_payment);


        Intent thisintent = getIntent();
        Bundle bund = thisintent.getExtras();

        final TextView textview_email = (TextView) findViewById(R.id.textview_email);

        final String address = getIntent().getExtras().getString("address");
        textview_email.setText(address);

        findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edittext = (EditText) findViewById(R.id.edittext_amountrub);
                edittext.setText("");
            }
        });

        findViewById(R.id.okcheck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String someaddressofwaves = address;
                String amount = ((EditText) findViewById(R.id.edittext_amountrub)).getText().toString();

                Intent ii = new Intent(CreatePaymentActivity.this, PaymentActivity.class);
                ii.putExtra("address", someaddressofwaves);
                ii.putExtra("amount", amount);
                startActivity(ii);
            }
        });
    }
}
