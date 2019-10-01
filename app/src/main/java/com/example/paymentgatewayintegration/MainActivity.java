package com.example.paymentgatewayintegration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    Button pay;
    EditText amount;

    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config=new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    String amt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pay=findViewById(R.id.pay);
        amount=findViewById(R.id.amount);
        //PayPal Service Start

        Intent i=new Intent(this,PayPalService.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(i);

        //PayPal Service end



        //On Button Click
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment();
            }
        });


    }

    @Override
    protected void onDestroy() {

        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    //Called on Button CLick PAY_USING_PAYPAL
    public void payment()
    {
        Log.d("Payment function","------------------------> pau button clicked.");
        amt=amount.getText().toString().trim();
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(amt)),"INR","Pay",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent i=new Intent(this,PaymentActivity.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        i.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(i,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PAYPAL_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null)
                {
                    try{
                        String paymentDetails=confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,PaymentDetails.class)
                                .putExtra("PaymentDetails",paymentDetails).putExtra("PaymentAmount",amt));
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            else if(resultCode== Activity.RESULT_CANCELED)
            {
                Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
