package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView orderIdEditText;
    TextView itemNameEditText;
    TextView weightEditText;
    TextView amountPaidEditText;
    TextView amountDueEditText;
    TextView totalAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderIdEditText = findViewById(R.id.orderId);
        itemNameEditText = findViewById(R.id.itemName);
        weightEditText = findViewById(R.id.weight);
        amountPaidEditText = findViewById(R.id.amountPaid);
        amountDueEditText = findViewById(R.id.amountDue);
        totalAmountEditText = findViewById(R.id.totalAmount);

        String orderId = getIntent().getStringExtra("orderIdEditText");
        String itemName = getIntent().getStringExtra("itemNameEditText");
        String weight = getIntent().getStringExtra("weight");
        String amountPaid = getIntent().getStringExtra("amountPaid");
        String totalAmount = getIntent().getStringExtra("totalAmount");
        String amountDue = Integer.toString(Integer.parseInt(totalAmount)-Integer.parseInt(amountPaid));


        orderIdEditText.setText("Order ID : " + orderId);
        itemNameEditText.setText("Item Name : " + itemName);
        weightEditText.setText("Weight : " + weight);
        amountPaidEditText.setText("Amount Paid : " + orderId);
        amountDueEditText.setText("Amount Due : " + orderId);
        totalAmountEditText.setText("Total Amount : " + orderId);

    }
}
