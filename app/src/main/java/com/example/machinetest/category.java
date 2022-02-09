package com.example.machinetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class category extends AppCompatActivity implements CategoryInterface, ProductInterface {

    //Declaring IDs
    ImageView profile,category_add;
    RecyclerView h_grid,v_view;
    Button see_all,add_image;
    private GoogleSignInClient mGoogleSignInClient;

    //Recycler for Category
    HorizontalModel horizontalModel;
    HorizontalAdapter horizontalAdapter;
    public int columnCount = 2;
    List<HorizontalModel>horizontalModelList = new ArrayList<>();

    //Recycler for Product
    VerticaLModel verticaLModel;
    VerticalAdapter verticalAdapter;
    List<VerticaLModel> verticaLModelList = new ArrayList<>();

    //SQLite
    SQLiteDatabase db;
    DBHelper dbHelper;
    private byte[] image_data;

    AlertDialog alertDialog;

    String c_name_str;
    final int REQUEST_CODE_GALLERY = 999;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Defining id's
        category_add = findViewById(R.id.category_add);
        see_all = findViewById(R.id.see_all);
        profile = findViewById(R.id.profile);
        h_grid = findViewById(R.id.h_grid);
        v_view = findViewById(R.id.v_view);
        add_image = findViewById(R.id.add_image);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        sharedPreferences = getSharedPreferences("category",MODE_PRIVATE);
        editor = sharedPreferences.edit();


        category_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       startActivity(new Intent(getApplicationContext(), category_add.class));

                    }
                });

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Product.class));
            }
        });

        //To perform SignOut operations
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.profile:
                        signOut();
                        break;
                }

            }
        });


        //category grid initialisation
        h_grid.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnCount, GridLayoutManager.HORIZONTAL,false);
        h_grid.setLayoutManager(gridLayoutManager);


        //category List
        horizontalAdapter = new HorizontalAdapter(this,horizontalModelList,this);
        h_grid.setAdapter(horizontalAdapter);
        LoadHData();

        //product recyclerview
        v_view.setHasFixedSize(true);
        v_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //product list
        verticalAdapter = new VerticalAdapter(this,verticaLModelList,this);
        v_view.setAdapter(verticalAdapter);
        LoadVData();


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

    private void LoadHData() {


        Cursor cursor = db.rawQuery("select * from Category",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String c_name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            horizontalModelList.add(new HorizontalModel(id,c_name,image));
            horizontalAdapter.notifyDataSetChanged();
        }








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
    public void onItemClick(int i) {

        String category_name = horizontalModelList.get(i).getName();
        int category_id = horizontalModelList.get(i).getId();

        editor.putString("category_name",category_name);
        editor.putInt("category_id",category_id);
        editor.apply();

        startActivity(new Intent(getApplicationContext(),category_ud.class));



    }

    @Override
    public void onProductClick(int i) {



    }
}