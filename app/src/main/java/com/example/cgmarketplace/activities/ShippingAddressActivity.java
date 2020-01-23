package com.example.cgmarketplace.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cgmarketplace.R;

public class ShippingAddressActivity extends AppCompatActivity {

    private TextView tvTitle;
    private EditText etFull_name, etAddress, etCity, etRegion, etZip_Code, etCountry, etPhone_Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.shipping_address);

        etFull_name = findViewById(R.id.etFull_name);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etRegion = findViewById(R.id.etRegion);
        etZip_Code = findViewById(R.id.etZip_Code);
        etCountry = findViewById(R.id.etCountry);
        etPhone_Number = findViewById(R.id.etPhone_Number);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
