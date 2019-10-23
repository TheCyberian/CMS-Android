package com.thecyberian.cms_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = findViewById(R.id.phoneNumberEditText);
        name = findViewById(R.id.nameEditText);
    }

    public void searchCustomer(View view) {

        if (phoneNumber.getText().toString().isEmpty()) {
            Intent intent = new Intent(MainActivity.this, CustomerSearch.class);
            intent.putExtra("phoneNumber", "");
            intent.putExtra("name", name.getText().toString());

            Log.i("phoneNumber", phoneNumber.getText().toString());
            startActivity(intent);
        } else if (name.getText().toString().isEmpty()) {
            Intent intent = new Intent(MainActivity.this, CustomerSearch.class);
            intent.putExtra("name", "");
            intent.putExtra("phoneNumber", phoneNumber.getText().toString());

            Log.i("name", name.getText().toString());
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "Enter one of the values to perform search operation.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCustomer(View view){
        Intent intent = new Intent(MainActivity.this, AddCustomerActivity.class);
        startActivity(intent);
    }

    public void addItem(View view){
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivity(intent);
    }
}
