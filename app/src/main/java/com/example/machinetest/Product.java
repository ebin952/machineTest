package com.example.machinetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Product extends AppCompatActivity implements ProductInterface {

    RecyclerView v_view;
    ImageView addProduct;
    private GoogleSignInClient mGoogleSignInClient;


    VerticalAdapter verticalAdapter;
    List<VerticaLModel> verticaLModelList = new ArrayList<>();
    AlertDialog alertDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SQLiteDatabase db;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Checkout.preload(getApplicationContext());

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        v_view= findViewById(R.id.v_view);
        addProduct = findViewById(R.id.product_add);

        v_view.setHasFixedSize(true);
        v_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //product list
        verticalAdapter = new VerticalAdapter(this,verticaLModelList,this);
        v_view.setAdapter(verticalAdapter);
        LoadVData();

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),product_add.class));
                finish();

            }
        });




    }

    private void LoadVData() {

        Cursor cursor = db.rawQuery("select * from Product",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String p_name = cursor.getString(1);
            String c_name = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String p_price = cursor.getString(4);

            verticaLModelList.add(new VerticaLModel(p_name,c_name,image,p_price));
        }
        verticalAdapter.notifyDataSetChanged();
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });
    }

    @Override
    public void onProductClick(int i) {
        sharedPreferences = getSharedPreferences("Product",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int Price = Integer.parseInt(verticaLModelList.get(i).getPrice());

        editor.putString("product_name",verticaLModelList.get(i).getProduct_name());
        editor.putString("product_type",verticaLModelList.get(i).getCategory_name());
        editor.putInt("product_price",Price);
        editor.apply();

        startActivity(new Intent(getApplicationContext(),buy.class));



    }


}