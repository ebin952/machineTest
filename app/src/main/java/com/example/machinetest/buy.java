package com.example.machinetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class buy extends AppCompatActivity implements PaymentResultListener {
    TextView p_name,p_type,p_price;
    Button buy;
    //EditText mobile;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);


        p_name = findViewById(R.id.p_name);
        p_type = findViewById(R.id.p_type);
        p_price = findViewById(R.id.p_price);
        buy = findViewById(R.id.buy);
        //mobile = findViewById(R.id.mobile);

        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("Product",MODE_PRIVATE);


        String Username = sharedPreferences.getString("UserName","");
        String Usermail = sharedPreferences.getString("UserMail","");
        String P_name = sharedPreferences.getString("product_name","");
        String P_type = sharedPreferences.getString("product_type","");
        int P_price = sharedPreferences.getInt("product_price",0);

        p_name.setText(P_name);
        p_type.setText(P_type);
        p_price.setText("Rs. "+ P_price);


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment();
            }
        });

    }
        public void makePayment() {

            String P_name = sharedPreferences.getString("product_name","");
            String P_type = sharedPreferences.getString("product_type","");
            int P_price = sharedPreferences.getInt("product_price",0);
            String Username = sharedPreferences.getString("UserName","");
            String Usermail = sharedPreferences.getString("UserMail","");
            //int mobile_no = Integer.parseInt(mobile.getText().toString());



            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_IDEGjUGf0AjrDY");

            checkout.setImage(R.drawable.food_truck);


            final Activity activity = this;


            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */
            try {
                JSONObject options = new JSONObject();



                options.put("name", Username);
                options.put("description", P_name);
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", P_price*100);//pass amount in currency subunits
                options.put("prefill.email", Usermail);
                options.put("prefill.contact","");
                /*JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);*/

                checkout.open(activity, options);

            } catch(Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }


        }



    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Denied"+s, Toast.LENGTH_LONG).show();

    }
}