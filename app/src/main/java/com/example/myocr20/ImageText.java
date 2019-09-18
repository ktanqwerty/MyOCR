package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

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
    Button button,speak;
    Bitmap bitma;
    EditText editText;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text);

        imageView = (ImageView)findViewById(R.id.imageView1);
        button = (Button)findViewById(R.id.button2);
        speak = (Button)findViewById(R.id.button3);
        editText = (EditText)findViewById(R.id.editText);

        Intent intent = getIntent();
        bitma = (Bitmap) intent.getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitma);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(ImageText.this,"NO TEXT", LENGTH_SHORT);
                } else{
                    Frame frame = new Frame.Builder().setBitmap(bitma).build();

                    SparseArray<TextBlock> items = textRecognizer.detect(frame);

                    StringBuilder a = new StringBuilder();
                    for(int i=0;i<items.size();i++)
                    {
                        TextBlock myItem=items.valueAt(i);
                        a.append(myItem.getValue());
                        a.append("\n");
                    }

                    if(items.size() ==0)
                    {
                        editText.setText("NO TEXT");
                    }else{
                        editText.setText(a.toString());}
                }
            }
        });


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(ImageText.this,"NO TEXT", LENGTH_SHORT);
                } else{
                    Frame frame = new Frame.Builder().setBitmap(bitma).build();

                    SparseArray<TextBlock> items = textRecognizer.detect(frame);

                    StringBuilder a = new StringBuilder();
                    for(int i=0;i<items.size();i++)
                    {
                        TextBlock myItem=items.valueAt(i);
                        a.append(myItem.getValue());
                        a.append("\n");
                    }

                    String text = a.toString();

                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if(i!=TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.UK);
                            }
                        }
                    });
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }

            }
        });



    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}
