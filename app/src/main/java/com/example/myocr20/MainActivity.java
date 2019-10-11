package com.example.myocr20;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton open_camera;
    FloatingActionButton gallery;
    ImageView imageView;
    private static final String TAG = "SearchActivity";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int IMAGE_PICK= 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int GALLERY_REQUEST_CODE = 2;
    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //camera

        open_camera = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        gallery = (FloatingActionButton) findViewById(R.id.gallery);


        Log.d(TAG, "verify Permission: Asking user for permission");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) != PackageManager
                .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, CAMERA_REQUEST_CODE);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 2);
        }

        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentn = new Intent();
                intentn.setType("image/*");
                intentn.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentn.createChooser(intentn,"SELECT IMAGE"),GALLERY_REQUEST_CODE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intenth = new Intent(MainActivity.this , HistoryActivity.class);
            startActivity(intenth);

        } else if (id == R.id.nav_about) {

        }  else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
                if(requestCode == CAMERA_REQUEST_CODE) {
                    Bitmap bitmapImage1 = (Bitmap) data.getExtras().get("data");
                    Intent   camintent1= new Intent(MainActivity.this,ImageText.class);
                    camintent1.putExtra("bitmap", bitmapImage1);
                    startActivity(camintent1);


                }
                else if(requestCode == GALLERY_REQUEST_CODE ){
                    Uri uri = data.getData();
                   /*  InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap bitmap2 = BitmapFactory.decodeStream(imageStream);
                    */


                    Intent   gallintent= new Intent(MainActivity.this,ImageText.class);
                    gallintent.putExtra("uri", uri);
                    Log.i("nice","gallery is working");
                    startActivity(gallintent);

                }





        }

    }
}
