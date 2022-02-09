package com.example.machinetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;

public class product_add extends AppCompatActivity {

    ImageView img_product;
    EditText name_product,price_product;
    Button add_product;
    Spinner category_of_product;

    SharedPreferences sharedPreferences;


    public static final int REQUEST_CODE_GALLERY = 999;

    SQLiteDatabase db;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        img_product= findViewById(R.id.image_of_product);
        name_product = findViewById(R.id.name_of_product);
        price_product = findViewById(R.id.price_of_product);
        add_product = findViewById(R.id.add_product);
        category_of_product = findViewById(R.id.category_of_product);



        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        loadSpinnerData();



        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(product_add.this,new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

       add_product.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {





                   dbHelper.dbInsertProduct(db, name_product.getText().toString(),category_of_product.getSelectedItem().toString(), imageViewToByte(img_product),price_product.getText().toString());
                   Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(),Product.class));
                   finish();
           }

       });
    }

    private void loadSpinnerData() {

        dbHelper = new DBHelper(this);
        List<String> category_list = dbHelper.getAllCategory();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, category_list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_of_product.setAdapter(dataAdapter);

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_product.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}