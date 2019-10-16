package com.example.myocr20;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class scanfinal extends AppCompatActivity {
    Bitmap bitmap1;
    Button savetext;
    Button copybutton;
    Button googlebutton;
    Button mailbutton;
    Button speakbutton;
    EditText editText;
     String scannedtext;
     Uri uri;
    TextToSpeech tts;
    String value1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanfinal);


        Intent intent2 = getIntent();
        editText = findViewById(R.id.editText);
        savetext = findViewById(R.id.saveText);
        copybutton = findViewById(R.id.copy);
        googlebutton = findViewById(R.id.googleb);
        mailbutton = findViewById(R.id.gmailb);
        speakbutton = findViewById(R.id.speakb);

        //change
            bitmap1 = intent2.getParcelableExtra("Bitmap");
            uri = intent2.getParcelableExtra("uri");
            value1 = intent2.getStringExtra("value");

            if(value1==null) {


                if (bitmap1 == null) {


                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap1 = BitmapFactory.decodeStream(imageStream);

                }


                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(scanfinal.this, "NO TEXT", LENGTH_SHORT);
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap1).build();

                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder a = new StringBuilder();


                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        a.append(myItem.getValue());
                        a.append("\n");
                    }
                    scannedtext = a.toString();
                    // Log.i("a",scannedtext);
                    if (items.size() == 0) {
                        editText.setText("NO TEXT");
                    } else {
                        editText.setText(a.toString());
                    }
                }
            }
            else {
                scannedtext= value1;
                editText.setText(value1);
            }



                savetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Intent intent3  =  new Intent(scanfinal.this , HistoryActivity.class);
                        if(value1==null) {


                            if (scannedtext != null) {
                                //  intent3.putExtra("scannedtext", scannedtext.toString());
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Users");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                myRef.child(user.getUid()).push().setValue(scannedtext);
                                Toast.makeText(scanfinal.this, "Text Saved Sucessfully", LENGTH_SHORT).show();

                            }
                        }
                        else{
                            Toast.makeText(scanfinal.this,"Already saved in history", LENGTH_SHORT).show();
                        }
                        //startActivity(intent3);
                    }
                });


        copybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", scannedtext);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(scanfinal.this,"Text Copied to Clipboard",LENGTH_SHORT).show();
            }
        });

        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, scannedtext); // query contains search string
                startActivity(intent);
            }
        });

        mailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO , Uri.parse("mailto:"+ scannedtext));
                //emailIntent.setType("plain/text");
                startActivity(emailIntent);
            }
        });

        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i!= TextToSpeech.ERROR){
                            tts.setLanguage(Locale.US);
                            String toSpeak = scannedtext;
                            tts.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }
                });



            }
        });


    }
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}
