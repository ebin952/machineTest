package com.example.machinetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper{

    Context context;
    SQLiteDatabase db;
    public static final String DATABASE_NAME = "FoodTruck6";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE1_NAME = "Category";
    public static final String TABLE2_NAME = "Product";
    public static final String C_ID = "C_id";
    public static final String C_IMAGE = "C_image";
    public static final String P_ID = "P_id";
    public static final String P_NAME = "P_name";
    public static final String C_NAME = "C_name";
    public static final String P_IMAGE = "P_image";
    public static final String QUANTITY = "Quantity";
    public static final String P_PRICE = "Price";


        public  DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }



        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {


            sqLiteDatabase.execSQL("Create Table " + TABLE1_NAME + "( " + C_ID + " INTEGER Primary Key AUTOINCREMENT," + C_NAME + " text, " + C_IMAGE + " BLOB)");
            sqLiteDatabase.execSQL("Create Table " + TABLE2_NAME + "( " + P_ID + " INTEGER Primary Key AUTOINCREMENT," + P_NAME + " text," + C_NAME + " text, " + P_IMAGE + " BLOB, " +  P_PRICE + " float,"
                    +C_ID+" INTEGER, CONSTRAINT Category_fk FOREIGN KEY("+C_ID+") " +
                    "REFERENCES "+TABLE1_NAME+"("+C_ID+") ON DELETE CASCADE)");

        }



    @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("drop Table if exists " + TABLE1_NAME);
            sqLiteDatabase.execSQL("drop Table if exists " + TABLE2_NAME);

        }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
        public boolean dbInsertCategory(SQLiteDatabase db, String Name, byte[] Image) throws SQLiteException {

            try {
                db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(C_NAME, Name);
                cv.put(C_IMAGE, Image);
                db.insert(TABLE1_NAME, null, cv);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean dbInsertProduct(SQLiteDatabase db, String P_Name, String C_Name, byte[] P_Image, String Price) throws SQLiteException {
            try {

                Cursor cursor = db.rawQuery("select "+C_ID+" from "+TABLE1_NAME+" where "+C_NAME + "=?",new String[]{C_Name} );

                while(cursor.moveToNext()) {
                    int c_id = cursor.getInt(0);


                    db = this.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(P_NAME, P_Name);
                    cv.put(C_NAME, C_Name);
                    cv.put(P_IMAGE, P_Image);
                    cv.put(P_PRICE, Price);
                    cv.put(C_ID, c_id);
                    db.insert(TABLE2_NAME, null, cv);

                }
                cursor.close();
                return true;


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public List<String> getAllCategory() {

            List<String> category_list = new ArrayList<String>();

            String selectQuery = "SELECT * from " + TABLE1_NAME;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()){
                do {
                    cursor.getInt(0);
                    category_list.add(cursor.getString(1));
                }while (cursor.moveToNext());

            }


            cursor.close();

            return category_list;

        }

        public boolean categoryUpdate(SQLiteDatabase db, int id,String C_name, byte[] image) throws SQLiteException{
            ContentValues cv = new ContentValues();
            cv.put(C_ID,id);
            cv.put(C_NAME,C_name);
            cv.put(C_IMAGE,image);
            db.update(TABLE1_NAME,cv,C_ID +" = "+ id,null);
            return true;

        }

        public boolean categoryDelete(SQLiteDatabase db, int id)
        {
            ContentValues cv = new ContentValues();
            cv.put(C_ID,id);
            db.delete(TABLE1_NAME,C_ID +" = "+id,null);
            return true;
        }





}