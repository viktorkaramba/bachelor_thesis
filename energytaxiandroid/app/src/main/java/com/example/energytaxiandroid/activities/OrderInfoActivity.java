package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.energytaxiandroid.R;

public class OrderInfoActivity extends AppCompatActivity {

    private TextView orderInfoDateEditText;
    private TextView orderInfoCustomerNameEditText;
    private TextView orderInfoAddressCustomerEditText;
    private TextView orderInfoAddressDeliveryEditText;
    private TextView orderInfoTelephoneCustomerEditText;
    private TextView orderInfoPriceEditText;
    private TextView orderInfoRatingEditText;
    private TextView orderInfoNumberOfKilometersEditText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        orderInfoDateEditText = findViewById(R.id.orderInfoDateEditText);
        orderInfoCustomerNameEditText = findViewById(R.id.orderInfoCustomerNameEditText);
        orderInfoAddressCustomerEditText = findViewById(R.id.orderInfoAddressCustomerEditText);
        orderInfoAddressDeliveryEditText = findViewById(R.id.orderInfoAddressDeliveryEditText);
        orderInfoTelephoneCustomerEditText = findViewById(R.id.orderInfoTelephoneCustomerEditText);
        orderInfoPriceEditText = findViewById(R.id.orderInfoPriceEditText);
        orderInfoRatingEditText = findViewById(R.id.orderInfoRatingEditText);
        orderInfoNumberOfKilometersEditText = findViewById(R.id.orderInfoNumberOfKilometersEditText);
        backButton = findViewById(R.id.orderInfoBackButton);
        backButton.setOnClickListener(view -> goDriverMenu(savedInstanceState));
        setDate(savedInstanceState);
    }

    public void goDriverMenu(Bundle savedInstanceState){
        Intent intent = new Intent(this, DriverMenuActivity.class);
        intent.putExtra("userId", getDate(savedInstanceState, "driverId"));
        intent.putExtra("isOnline", getDate(savedInstanceState, "isOnline"));
        startActivity(intent);
    }

    public void setDate(Bundle savedInstanceState){
        orderInfoDateEditText.setText(getDate(savedInstanceState, "date"));
        orderInfoCustomerNameEditText.setText(getDate(savedInstanceState, "customerName"));
        orderInfoAddressCustomerEditText.setText(getDate(savedInstanceState, "addressCustomer"));
        orderInfoAddressDeliveryEditText.setText(getDate(savedInstanceState, "addressDelivery"));
        orderInfoTelephoneCustomerEditText.setText(getDate(savedInstanceState, "telephoneCustomer"));
        orderInfoPriceEditText.setText(getDate(savedInstanceState, "price"));
        orderInfoRatingEditText.setText(getDate(savedInstanceState, "rating"));
        orderInfoNumberOfKilometersEditText.setText(getDate(savedInstanceState, "numberOfKilometers"));
    }

    public String getDate(Bundle savedInstanceState, String key){
        String result;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                result= null;
            } else {
                result= extras.getString(key);
            }
        } else {
            result= (String) savedInstanceState.getSerializable(key);
        }
        return result;
    }
}