package com.example.myocr20;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =123 ;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        list = new ArrayList<>();
            listView = findViewById(R.id.list_view);

            adapter = new ArrayAdapter<String>(this, R.layout.user_info, R.id.userinfo, list);
            Intent intent = getIntent();
            //String a = intent.getStringExtra("scannedtext");
             user = FirebaseAuth.getInstance().getCurrentUser();
            Log.i("uid", user.getUid());
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");
            //myRef.child("child").push().setValue("sdf");
            String b = "abc";
            // myRef.child(user.getUid()).child("1").setValue(b);
            // myRef.child(user.getUid()).child(b);

            myRef = database.getReference().child("Users").child(user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String st = ds.getValue(String.class);
                        list.add(st);
                        Log.i("dsf",st);
                    }
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String value1 = (String) adapterView.getItemAtPosition(i);
                    //Toast.makeText(HistoryActivity.this,value1,Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(HistoryActivity.this,scanfinal.class);
                    intent2.putExtra("value",value1);
                    startActivity(intent2);
                }
            });





    }
}
