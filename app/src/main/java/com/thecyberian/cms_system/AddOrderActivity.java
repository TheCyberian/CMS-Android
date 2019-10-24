package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddOrderActivity extends AppCompatActivity {
    private Spinner spinner2;

    String baseEndPoint = "http://10.0.2.2:5000/getItems";
    List<String> itemsData;

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

        if(getIntent().getStringExtra("address").contains("Address :")){
            nameTextView.setText(getIntent().getStringExtra("name"));
            addressTextView.setText("\n" + getIntent().getStringExtra("address"));
            contactTextView.setText("\n" + getIntent().getStringExtra("phone"));
        }else{
            nameTextView.setText("Name : \n\t"+getIntent().getStringExtra("name") + "\n");
            addressTextView.setText("Address : \n" + getIntent().getStringExtra("address") + "\n");
            contactTextView.setText("Phone : \n" + getIntent().getStringExtra("phone") + "\n");
        }

        GetDataFromApi task = new GetDataFromApi();
        task.execute(baseEndPoint);
        addListenerOnButton();
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

                spinner2 = (Spinner) findViewById(R.id.spinner2);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray subArray = jsonArray.getJSONArray(i);
                    data.add(i, subArray.get(1).toString());
                }
                itemsData = data;
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddOrderActivity.this, android.R.layout.simple_spinner_item, itemsData);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

/*
* Add a button here which will save the data
* Spinners to be created - 5
* spinners to be populated from api - content types - done
* 5 order details to be captured and sent to api */
    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AddOrderActivity.this,
                        "OnItemSelected : " +
                                "\nSpinner 2 : " + String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
