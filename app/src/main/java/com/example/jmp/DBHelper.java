package com.example.jmp;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "peserta";
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_PHOTO = "photo";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_PHOTO + " BLOB)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /*
    public void storeImage (ModelClass objectModelClass){
        try{
            SQLiteDatabase objectSqLiteDatabase= this.getWritableDatabase();
            Bitmap imageToStoreBitmap = objectModelClass.getImage();

            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100,objectByteArrayOutputStream);

            imageInBytes = objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues = new ContentValues();

            objectContentValues.put("photo",objectModelClass.getImageName());
            objectContentValues.put("", imageInBytes);

            long checkIfQueryRuns = objectSqLiteDatabase.insert("", null, objectContentValues);
            if(checkIfQueryRuns!=0){
                Toast.makeText(context, "Data berhasil ditambahkan", Toast.LENGTH_SHORT). show();
            }
            else{
                Toast.makeText(context, "Data gagal ditambahkan", Toast.LENGTH_SHORT). show();
            }
        }
        catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT). show();
        }
    }
     */


    public boolean addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues user_values = new ContentValues();

        user_values.put(KEY_NAME, user.getName());
        user_values.put(KEY_ADDRESS, user.getAddress());
        user_values.put(KEY_GENDER, user.getGender());
        user_values.put(KEY_PHONE, user.getPhone());
        user_values.put(KEY_CITY, user.getCity());
        user_values.put(KEY_PHOTO, user.getPhoto());

        Log.d(DATABASE_NAME, "addUser: Adding " + user + " to " + TABLE_USERS);
        long result = db.insert(TABLE_USERS, null, user_values);
        if (result == -1)
            return false;
        else
            return true;
    }


    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String select_query = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(select_query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setAddress(cursor.getString(2));
                user.setGender(cursor.getString(3));
                user.setPhone(cursor.getString(4));
                user.setCity(cursor.getString(5));
                user.setPhoto(cursor.getBlob(6));

                userList.add(user);

            } while (cursor.moveToNext());
        }
        return userList;
    }
}
