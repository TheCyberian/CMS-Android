package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

        if (getIntent().getStringExtra("FROM_ACTIVITY").equals("CustomerDetails")) {
            String orderId = getIntent().getStringExtra("orderIdEditText");
            String itemName = getIntent().getStringExtra("itemNameEditText");
            String weight = getIntent().getStringExtra("weight");
            String amountPaid = getIntent().getStringExtra("amountPaid");
            String totalAmount = getIntent().getStringExtra("totalAmount");
            String amountDue = Integer.toString(Integer.parseInt(totalAmount) - Integer.parseInt(amountPaid));

            orderIdEditText.setText("Order ID : " + orderId);
            itemNameEditText.setText("Item Name : " + itemName);
            weightEditText.setText("Weight : " + weight);
            amountPaidEditText.setText("Amount Paid : " + amountPaid);
            amountDueEditText.setText("Amount Due : " + amountDue);
            totalAmountEditText.setText("Total Amount : " + totalAmount);

        } else {
            // Post to Api Here
            Toast.makeText(OrderDetailsActivity.this, "Posting to server.", Toast.LENGTH_SHORT).show();

            String name = getIntent().getStringExtra("name");
            String address = getIntent().getStringExtra("address");
            String phone = getIntent().getStringExtra("phone");

            Log.i("Name", name);
            Log.i("Address", address);
            Log.i("Phone", phone);

            Log.i("item1", getIntent().getStringExtra("item1"));
            Log.i("item2", getIntent().getStringExtra("item2"));
            Log.i("item3", getIntent().getStringExtra("item3"));
            Log.i("item4", getIntent().getStringExtra("item4"));

            /*
             * Split the name get first name
             * Split the phone number and get the number
             * Pass them as URL queries in GET request
             * create a back end method to capture unique CustId based on name and phone numbers
             * With the custId, and ItemNumber for each item name
             * create orders using below post request format
             *
             *  "custId": request.json['custId'],
                "itemNumber": request.json['itemNumber'],
                "amountPaid": request.json['amountPaid'],
                "weight": request.json['weight'],
                "totalAmount": request.json['totalAmount']
             * */
        }
    }
}
