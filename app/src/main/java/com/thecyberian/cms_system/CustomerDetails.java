package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class CustomerDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        String custId = getIntent().getStringExtra("custId");
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");

        Log.i("CustID from Intent", custId);
        Log.i("name from Intent", name);
        Log.i("address from Intent", address);
        Log.i("phone from Intent", phone);
    }
}
