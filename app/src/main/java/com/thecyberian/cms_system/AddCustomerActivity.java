package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {

    String baseUrl = "http://10.0.2.2:5000/addCustomer";
    EditText initialEditText;
    EditText nameEditText;
    EditText address1EditText;
    EditText address2EditText;
    EditText address3EditText;
    EditText pincodeEditText;
    EditText phone1EditText;
    EditText phone2EditText;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        initialEditText = findViewById(R.id.initialEditText);
        nameEditText = findViewById(R.id.nameEditText);
        address1EditText = findViewById(R.id.address1EditText);
        address2EditText = findViewById(R.id.address2EditText);
        address3EditText = findViewById(R.id.address3EditText);
        pincodeEditText = findViewById(R.id.pincodeEditText);
        phone1EditText = findViewById(R.id.phone1EditText);
        phone2EditText = findViewById(R.id.phone2EditText);
        emailEditText = findViewById(R.id.emailEditText);
    }

    public boolean validateData() {
        String name = nameEditText.getText().toString();
        String initial = initialEditText.getText().toString();
        String address1 = address1EditText.getText().toString();
        String address2 = address2EditText.getText().toString();

        String pincode = pincodeEditText.getText().toString();
        String phone1 = phone1EditText.getText().toString();
        String phone2 = phone2EditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (name.isEmpty() || initial.isEmpty() || address1.isEmpty() || address2.isEmpty() || pincode.isEmpty() || phone1.isEmpty() || phone2.isEmpty() || email.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    public void saveCustomerAndCreateOrder(View view) {
        if (validateData()) {
            Map<String, String> postData = new HashMap<>();

            postData.put("name", nameEditText.getText().toString());
            postData.put("initial", initialEditText.getText().toString());
            postData.put("address1", address1EditText.getText().toString());
            postData.put("address2", address2EditText.getText().toString());
            postData.put("address3", address3EditText.getText().toString());
            postData.put("pincode", pincodeEditText.getText().toString());
            postData.put("phone1", phone1EditText.getText().toString());
            postData.put("phone2", phone2EditText.getText().toString());
            postData.put("email", emailEditText.getText().toString());

            HttpPostAsyncTask task = new HttpPostAsyncTask(postData);
            task.execute(baseUrl);


            Intent intent = new Intent(AddCustomerActivity.this, AddOrderActivity.class);

            intent.putExtra("name", initialEditText.getText().toString() + " " + nameEditText.getText().toString());
            intent.putExtra("address", address1EditText.getText().toString() + "\n" + address2EditText.getText().toString() + "\n" + address3EditText.getText().toString());
            intent.putExtra("phone", phone1EditText.getText().toString() + "\n" + phone2EditText.getText().toString() + "\n" + emailEditText.getText().toString());

            startActivity(intent);
        } else {
            Toast.makeText(AddCustomerActivity.this, "Please enter all mandatory fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveCustomer(View view) {
        if (validateData()) {
            Map<String, String> postData = new HashMap<>();

            postData.put("name", nameEditText.getText().toString());
            postData.put("initial", initialEditText.getText().toString());
            postData.put("address1", address1EditText.getText().toString());
            postData.put("address2", address2EditText.getText().toString());
            postData.put("address3", address3EditText.getText().toString());
            postData.put("pincode", pincodeEditText.getText().toString());
            postData.put("phone1", phone1EditText.getText().toString());
            postData.put("phone2", phone2EditText.getText().toString());
            postData.put("email", emailEditText.getText().toString());

            HttpPostAsyncTask task = new HttpPostAsyncTask(postData);
            task.execute(baseUrl);

            Intent intent = new Intent(AddCustomerActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(AddCustomerActivity.this, "Please enter all mandatory fields", Toast.LENGTH_SHORT).show();
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

                if (statusCode == 200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    String response = convertInputStreamToString(inputStream);
                } else {
                    Toast.makeText(AddCustomerActivity.this, "Something went wrong. Couldn't save data to server.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(AddCustomerActivity.this, "Saved Customer Successfully.", Toast.LENGTH_LONG).show();
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
