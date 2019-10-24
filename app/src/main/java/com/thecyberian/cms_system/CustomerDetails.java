package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CustomerDetails extends AppCompatActivity {

    TextView nameTextView;
    TextView addressTextView;
    TextView contactTextView;

    ListView orderList;
    ArrayAdapter orderListAdapter;
    String baseEndPoint = "http://10.0.2.2:5000/getCustomerOrder/";

    ArrayList<String[]> orderData;

    public void createOrder(View view) {
        Intent intent = new Intent(CustomerDetails.this, AddOrderActivity.class);

        intent.putExtra("name", nameTextView.getText().toString());
        intent.putExtra("address", addressTextView.getText().toString());
        intent.putExtra("phone", contactTextView.getText().toString());

        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        GetDataFromApi task = new GetDataFromApi();

        String custId = getIntent().getStringExtra("custId");
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");

        Log.i("CustID from Intent", custId);
        Log.i("name from Intent", name);
        Log.i("address from Intent", address);
        Log.i("phone from Intent", phone);

        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        contactTextView = findViewById(R.id.contactTextView);
        orderList = findViewById(R.id.custOrdersListView);

        nameTextView.setText("Name : \n\t" + name);
        addressTextView.setText("Address : \n" + address);
        contactTextView.setText("Phone : \n" + phone);

        task.execute(baseEndPoint + custId);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(CustomerDetails.this, "Tapped", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CustomerDetails.this, OrderDetailsActivity.class);
                intent.putExtra("orderIdEditText", orderData.get(i)[0]);
                intent.putExtra("itemNameEditText", orderData.get(i)[1]);
                intent.putExtra("weight", orderData.get(i)[2]);
                intent.putExtra("amountPaid", orderData.get(i)[3]);
                intent.putExtra("totalAmount", orderData.get(i)[3]);
                startActivity(intent);
            }
        });
    }

    public class GetDataFromApi extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;

            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                ArrayList<String> data = new ArrayList<>();
                orderData = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray subArray = jsonArray.getJSONArray(i);
                    data.add(i, subArray.get(1).toString());

                    String[] order = new String[5];

                    order[0] = subArray.get(0).toString();
                    order[1] = subArray.get(1).toString();
                    order[2] = subArray.get(2).toString();
                    order[3] = subArray.get(3).toString();
                    order[4] = subArray.get(4).toString();

                    orderData.add(i, order);
                }

                orderListAdapter = new ArrayAdapter(CustomerDetails.this, android.R.layout.simple_list_item_1, data);
                orderList.setAdapter(orderListAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
