package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        try {


            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR,age INT(3))");
            myDatabase.execSQL("INSERT INTO users(name,age) VALUES ('ROB',34)");
            Cursor c = myDatabase.rawQuery("SELECT * FROM users", null);
            int nameindex = c.getColumnIndex("name");
            int ageindex = c.getColumnIndex("age");
            c.moveToFirst();
            while (c != null) {
                Log.i("name", c.getString(nameindex));
                c.moveToNext();
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
