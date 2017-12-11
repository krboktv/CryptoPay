package ru.qualitylab.evotor.cryptopay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        findViewById(R.id.goLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText email = (EditText) findViewById(R.id.editTextEmailLogin);
                String someaddressofwaves = email.getText().toString();

                Intent ii=new Intent(LoginActivity.this, CreatePaymentActivity.class);
                ii.putExtra("address", someaddressofwaves);
                startActivity(ii);
            }
        });
    }
}
