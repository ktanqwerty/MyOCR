package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class ImageText extends AppCompatActivity {


    ImageView imageView;
    Button scantext;
    Bitmap bitma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text);

        imageView = (ImageView)findViewById(R.id.imageView1);
        scantext = (Button)findViewById(R.id.scantext);



        Intent intent = getIntent();
        bitma = (Bitmap) intent.getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitma);

        scantext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ImageText.this, scanfinal.class);
                intent2.putExtra("Bitmap",bitma);
                startActivity(intent2);
            }
        });








    }



}
