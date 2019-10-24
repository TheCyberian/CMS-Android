package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrderActivity extends AppCompatActivity {

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;

    EditText spinner1Weight;
    EditText spinner2Weight;
    EditText spinner3Weight;
    EditText spinner4Weight;

    EditText totalAmountLent1EditText;
    EditText totalAmountLent2EditText;
    EditText totalAmountLent3EditText;
    EditText totalAmountLent4EditText;

    String item1;
    String item2;
    String item3;
    String item4;
    String item1Weight;
    String item2Weight;
    String item3Weight;
    String item4Weight;

    String baseEndPoint = "http://10.0.2.2:5000/getItems";
    List<String> itemsData;
    Map<String, Integer> itemIdList;

    TextView nameTextView;
    TextView addressTextView;
    TextView contactTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        contactTextView = findViewById(R.id.contactTextView);

        if (getIntent().getStringExtra("address").contains("Address :")) {
            nameTextView.setText(getIntent().getStringExtra("name"));
            addressTextView.setText("\n" + getIntent().getStringExtra("address"));
            contactTextView.setText("\n" + getIntent().getStringExtra("phone"));
        } else {
            nameTextView.setText("Name : \n\t" + getIntent().getStringExtra("name") + "\n");
            addressTextView.setText("Address : \n" + getIntent().getStringExtra("address") + "\n");
            contactTextView.setText("Phone : \n" + getIntent().getStringExtra("phone") + "\n");
        }

        spinner1Weight = findViewById(R.id.spinner1WightEditText);
        spinner2Weight = findViewById(R.id.spinner2WightEditText);
        spinner3Weight = findViewById(R.id.spinner3WightEditText);
        spinner4Weight = findViewById(R.id.spinner4WightEditText);

        totalAmountLent1EditText = findViewById(R.id.totalAmountLent1EditText);
        totalAmountLent2EditText = findViewById(R.id.totalAmountLent2EditText);
        totalAmountLent3EditText = findViewById(R.id.totalAmountLent3EditText);
        totalAmountLent4EditText = findViewById(R.id.totalAmountLent4EditText);

        GetDataFromApi task = new GetDataFromApi();
        task.execute(baseEndPoint);
        addListenerOnButton();
    }

    public void saveOrder(View view) {
        Intent intent = new Intent(AddOrderActivity.this, OrderDetailsActivity.class);

        item1Weight = spinner1Weight.getText().toString();
        item2Weight = spinner2Weight.getText().toString();
        item3Weight = spinner3Weight.getText().toString();
        item4Weight = spinner4Weight.getText().toString();

        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("address", getIntent().getStringExtra("address"));
        intent.putExtra("phone", getIntent().getStringExtra("phone"));

        try {
            intent.putExtra("item1", itemIdList.get(item1));
            intent.putExtra("item2", itemIdList.get(item2));
            intent.putExtra("item3", itemIdList.get(item3));
            intent.putExtra("item4", itemIdList.get(item4));
        } catch (Exception e) {
            e.printStackTrace();
        }

        intent.putExtra("item1Weight", item1Weight);
        intent.putExtra("item2Weight", item2Weight);
        intent.putExtra("item3Weight", item3Weight);
        intent.putExtra("item4Weight", item4Weight);

        try {
            intent.putExtra("totalAmount1", Integer.parseInt(totalAmountLent1EditText.getText().toString()));
            intent.putExtra("totalAmount2", Integer.parseInt(totalAmountLent2EditText.getText().toString()));
            intent.putExtra("totalAmount3", Integer.parseInt(totalAmountLent3EditText.getText().toString()));
            intent.putExtra("totalAmount4", Integer.parseInt(totalAmountLent4EditText.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        intent.putExtra("FROM_ACTIVITY", "AddOrderActivity");

        startActivity(intent);
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
                itemIdList = new HashMap<>();
                spinner1 = findViewById(R.id.spinner1);
                spinner2 = findViewById(R.id.spinner2);
                spinner3 = findViewById(R.id.spinner3);
                spinner4 = findViewById(R.id.spinner4);

                data.add(0, "None");

                for (int i = 1; i <= jsonArray.length(); i++) {
                    JSONArray subArray = jsonArray.getJSONArray(i - 1);

                    itemIdList.put(subArray.get(1).toString(), Integer.parseInt(subArray.get(0).toString()));

                    data.add(i, subArray.get(1).toString());
                }

                itemsData = data;
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddOrderActivity.this, android.R.layout.simple_spinner_item, itemsData);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner1.setAdapter(dataAdapter);
                spinner2.setAdapter(dataAdapter);
                spinner3.setAdapter(dataAdapter);
                spinner4.setAdapter(dataAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void addListenerOnButton() {
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item1 = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2 = findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item2 = spinner2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3 = findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item3 = spinner3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner4 = findViewById(R.id.spinner4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item4 = spinner4.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
