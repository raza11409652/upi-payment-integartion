package com.hackdroid.upiapptest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText upi;
    Button start;
    String upiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upi = findViewById(R.id.upi);
        start = findViewById(R.id.start);


//  start click
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upiId = upi.getText().toString();
                if (TextUtils.isEmpty(upiId)) {
                    upi.setError("Required");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), UPIPayment.class);
                intent.putExtra("upi", upiId);
                startActivity(intent);
            }
        });


    }
}
