package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView orderIdEditText;
    TextView itemNameEditText;
    TextView weightEditText;
    TextView amountPaidEditText;
    TextView amountDueEditText;
    TextView totalAmountEditText;

    Integer itemNumber1;
    Integer itemNumber2;
    Integer itemNumber3;
    Integer itemNumber4;

    Integer totalAmount1;
    Integer totalAmount2;
    Integer totalAmount3;
    Integer totalAmount4;

    String baseEndPoint = "http://10.0.2.2:5000/getCustomerData";
    String baseUrl = "http://10.0.2.2:5000/addOrder";
    String custId;

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

            itemNumber1 = getIntent().getIntExtra("item1", -1);
            itemNumber2 = getIntent().getIntExtra("item2", -1);
            itemNumber3 = getIntent().getIntExtra("item3", -1);
            itemNumber4 = getIntent().getIntExtra("item4", -1);

            totalAmount1 = getIntent().getIntExtra("totalAmount1", -1);
            totalAmount2 = getIntent().getIntExtra("totalAmount2", -1);
            totalAmount3 = getIntent().getIntExtra("totalAmount3", -1);
            totalAmount4 = getIntent().getIntExtra("totalAmount4", -1);


            Log.i("Name", name.split(" ")[3]);
            Log.i("Phone", phone.split("\n")[1]);

            String url = baseEndPoint + "?name=%27" + name.split(" ")[3] + "%27&phone=" + phone.split("\n")[1];
            GetDataFromApi task = new GetDataFromApi();
            task.execute(url);

            /*
             * Add the Updates to UI and finish up :)
             * */
        }
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
            if (s != null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    custId = jsonArray.get(0).toString();
                    Log.i("CustId", custId);

                saveCustomer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveCustomer() {
        Map<String, String> postData;
        HttpPostAsyncTask task;

        if (!itemNumber1.equals(-1)) {
            postData = new HashMap<>();

            postData.put("custId", custId);
            postData.put("itemNumber", itemNumber1.toString());
            postData.put("amountPaid", "0");
            postData.put("weight", getIntent().getStringExtra("item1Weight"));
            postData.put("totalAmount", totalAmount1.toString());

            task = new HttpPostAsyncTask(postData);
            task.execute(baseUrl);
        }


        if (!itemNumber2.equals(-1)) {
            postData = new HashMap<>();

            postData.put("custId", custId);
            postData.put("itemNumber", itemNumber2.toString());
            postData.put("amountPaid", "0");
            postData.put("weight", getIntent().getStringExtra("item2Weight"));
            postData.put("totalAmount", totalAmount2.toString());

            task = new HttpPostAsyncTask(postData);
            task.execute(baseUrl);
        }


        if (!itemNumber3.equals(-1)) {
            postData = new HashMap<>();

            postData.put("custId", custId);
            postData.put("itemNumber", itemNumber3.toString());
            postData.put("amountPaid", "0");
            postData.put("weight", getIntent().getStringExtra("item3Weight"));
            postData.put("totalAmount", totalAmount3.toString());

            task = new HttpPostAsyncTask(postData);
            task.execute(baseUrl);
        }


        if (!itemNumber4.equals(-1)) {
            postData = new HashMap<>();

            postData.put("custId", custId);
            postData.put("itemNumber", itemNumber4.toString());
            postData.put("amountPaid", "0");
            postData.put("weight", getIntent().getStringExtra("item4Weight"));
            postData.put("totalAmount", totalAmount4.toString());

            task = new HttpPostAsyncTask(postData);
            task.execute(baseUrl);
        }


    }


    public class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {

        JSONObject postData;

        public HttpPostAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setRequestMethod("POST");

//                urlConnection.setRequestProperty("Authorization", "someAuthString");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200 || statusCode == 201) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    String response = convertInputStreamToString(inputStream);
                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong. Couldn't save data to server.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(OrderDetailsActivity.this, "Saved Customer Successfully.", Toast.LENGTH_LONG).show();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
