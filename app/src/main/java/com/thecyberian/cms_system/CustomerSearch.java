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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CustomerSearch extends AppCompatActivity {

    ListView resultsListView;
    String baseEndPoint = "http://10.0.2.2:5000/getCustomer/";
    ArrayAdapter searchListAdapter;
    ArrayList<String[]> customerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);

        resultsListView = findViewById(R.id.resultsListView);
        GetDataFromApi task = new GetDataFromApi();

        try {
            String phoneNumber = getIntent().getStringExtra("phoneNumber");
            String name = getIntent().getStringExtra("name");


            if (phoneNumber.isEmpty()) {
                Log.i("name", name);
                task.execute(baseEndPoint + name);
            } else if (name.isEmpty()) {
                Log.i("phoneNumber", phoneNumber);
                task.execute(baseEndPoint + phoneNumber);
            } else {
                Toast.makeText(CustomerSearch.this, "Some error occurred", Toast.LENGTH_LONG).show();
                Log.i("error", "Errorrrrr");
            }

//            "9123193213"

        } catch (Exception e) {
            e.printStackTrace();
        }

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CustomerSearch.this, CustomerDetails.class);
                intent.putExtra("custId", customerData.get(i)[0]);
                intent.putExtra("name", customerData.get(i)[1]);
                intent.putExtra("address", customerData.get(i)[2]);
                intent.putExtra("phone", customerData.get(i)[3]);
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

            if (s != null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<String> data = new ArrayList<>();
                    customerData = new ArrayList<>();


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray subArray = jsonArray.getJSONArray(i);
                        data.add(i, subArray.get(1).toString());

                        String[] customer = new String[4];

                        customer[0] = subArray.get(0).toString();
                        customer[1] = subArray.get(2).toString() + " " + subArray.get(1).toString();
                        customer[2] = subArray.get(3).toString() + " \n" + subArray.get(4).toString() + "\n" + subArray.get(5).toString();
                        customer[3] = subArray.get(7).toString() + " \n" + subArray.get(8).toString();
                        /*Using sub array create another string sub array and add to customer data*/
                        customerData.add(i, customer);
                    }

                    searchListAdapter = new ArrayAdapter(CustomerSearch.this, android.R.layout.simple_list_item_1, data);

                    resultsListView.setAdapter(searchListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                finish();
            }
        }
    }
}
