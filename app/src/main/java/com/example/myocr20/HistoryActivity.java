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
    ListView listView;
    SQLiteDatabase myDatabase;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
          listView = (ListView) findViewById(R.id.list_view);
      arrayList = new ArrayList<String>();

        String scannedtextt;
        Intent intent4 = getIntent();
        scannedtextt = intent4.getStringExtra("scannedtext");

        try {


             myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR,id INTEGER PRIMARY KEY  AUTOINCREMENT )");
            if(scannedtextt !=null) {
                myDatabase.execSQL("INSERT INTO users(name,age) VALUES ('" +scannedtextt+ "'");
            }
            display();

        }


        catch (Exception e){
            e.printStackTrace();
        }



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure")
                        .setMessage("Do you want to Delete this Text")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //perform here
                                myDatabase.execSQL("DELETE FROM users WHERE id = i");
                                //arrayAdapter.notifyDataSetChanged();
                                arrayList.clear();
                                display();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });



    }
    public void display(){
        Cursor c = myDatabase.rawQuery("SELECT * FROM users", null);
        int nameindex = c.getColumnIndex("name");

        c.moveToFirst();
        while (c != null) {

            arrayList.add(c.getString(nameindex));
            c.moveToNext();
        }

        arrayList.add("fds");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
}
