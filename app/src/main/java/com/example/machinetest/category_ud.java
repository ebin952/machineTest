package com.example.machinetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.machinetest.product_add.imageViewToByte;

public class category_ud extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
    EditText editText;
    Button update, delete;

    VerticalAdapter verticalAdapter;
    List<VerticaLModel> verticaLModelList = new ArrayList<>();

    SQLiteDatabase db;
    DBHelper dbHelper;

    SharedPreferences sharedPreferences;
    String category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_ud);
        imageView = findViewById(R.id.imageView3);
        textView = findViewById(R.id.category_label);
        editText = findViewById(R.id.editTextTextPersonName);
        recyclerView = findViewById(R.id.category_list);
        delete = findViewById(R.id.category_update);
        update = findViewById(R.id.category_delete);


        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        sharedPreferences = getSharedPreferences("category", MODE_PRIVATE);
        category_name = sharedPreferences.getString("category_name", "");
        textView.setText(category_name);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        verticalAdapter = new VerticalAdapter(this, verticaLModelList);
        recyclerView.setAdapter(verticalAdapter);
        loadVData();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(category_ud.this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, 888);

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = sharedPreferences.getInt("category_id", 0);

                dbHelper.categoryUpdate(db, id, editText.getText().toString(), imageViewToByte(imageView));

                startActivity(new Intent(getApplicationContext(), category.class));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = sharedPreferences.getInt("category_id",0);
                dbHelper.categoryDelete(db,id);

                startActivity(new Intent(getApplicationContext(), category.class));
                finish();

            }
        });

    }



    private void loadVData() {

        sharedPreferences = getSharedPreferences("category", MODE_PRIVATE);
        category_name = sharedPreferences.getString("category_name", "");

        Cursor cursor = db.rawQuery("select * from Product where C_name = ?", new String[]{category_name});

        //Cursor cursor = db.query("Product",columns,selection,category_name,null,null,null,null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String p_name = cursor.getString(1);
            String c_name = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String p_price = cursor.getString(4);


            verticaLModelList.add(new VerticaLModel(p_name, c_name, image, p_price));


        }
        verticalAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}