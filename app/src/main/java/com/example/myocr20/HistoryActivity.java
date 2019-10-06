package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ListView listView = (ListView) findViewById(R.id.list_view);
        ArrayList<String> arrayList = new ArrayList<String>();

        String scannedtextt;
        Intent intent4 = getIntent();
        scannedtextt = intent4.getStringExtra("scannedtext");

        try {


            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR,id INTEGER  )");
            if(scannedtextt !=null) {
                myDatabase.execSQL("INSERT INTO users(name,age) VALUES ('" +scannedtextt+ "',3)");
            }
            Cursor c = myDatabase.rawQuery("SELECT * FROM users", null);
            int nameindex = c.getColumnIndex("name");
            int ageindex = c.getColumnIndex("age");
            c.moveToFirst();
            while (c != null) {
                Log.i("name", c.getString(nameindex));
                arrayList.add(c.getString(nameindex));
                c.moveToNext();
            }

        }


        catch (Exception e){
            e.printStackTrace();
        }


        arrayList.add("fds");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

    }
}
