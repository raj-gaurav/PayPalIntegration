package com.example.paymentgatewayintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView id,status,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        id=findViewById(R.id.payID);
        amount=findViewById(R.id.payAmount);
        status=findViewById(R.id.payStatus);

        Intent i=getIntent();

        try{
            JSONObject jsonObject=new JSONObject(getIntent().getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),getIntent().getStringExtra("PaymentAmount"));
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try{
            id.setText(response.getString("id"));
            amount.setText("Rs."+paymentAmount);
            status.setText(response.getString("state"));
        }catch(JSONException e){
            e.printStackTrace();
        }

    }
}
