package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.widget.Toast.LENGTH_SHORT;

public class scanfinal extends AppCompatActivity {
    Bitmap bitmap1;
    Button savetext;
    EditText editText;
     String scannedtext;
     Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanfinal);


        Intent intent2 = getIntent();
        bitmap1 = (Bitmap) intent2.getParcelableExtra("Bitmap");

        //change
        if(bitmap1==null) {
            uri = intent2.getParcelableExtra("uri");

            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap1 = BitmapFactory.decodeStream(imageStream);
        }



        editText = findViewById(R.id.editText);
        savetext = findViewById(R.id.saveText);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
           Toast.makeText(scanfinal.this,"NO TEXT", LENGTH_SHORT);
        } else{
            Frame frame = new Frame.Builder().setBitmap(bitmap1).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder a = new StringBuilder();


            for(int i=0;i<items.size();i++)
            {
                TextBlock myItem=items.valueAt(i);
                a.append(myItem.getValue());
                a.append("\n");
            }
            scannedtext = a.toString();
            if(items.size() ==0)
            {
                editText.setText("NO TEXT");
            }else{
                editText.setText(a.toString());}
        }

    savetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3  =  new Intent(scanfinal.this , HistoryActivity.class);
                if(scannedtext != null) {
                    intent3.putExtra("scannedtext", scannedtext.toString());

                }

                startActivity(intent3);
            }
        });




    }
}
