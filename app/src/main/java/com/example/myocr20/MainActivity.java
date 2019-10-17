    package com.example.myocr20;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

    public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton open_camera;
    FloatingActionButton gallery;
    ImageView imageView;
        private static final int RC_SIGN_IN =123 ;
        private static final String TAG = "SearchActivity";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int IMAGE_PICK= 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int GALLERY_REQUEST_CODE = 2;
    //TextView textView;
    TextView textView1;
    Uri imageUri;
    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null) {


            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),

                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.AnonymousBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
        NavigationView nav = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView1=findViewById(R.id.textemail);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

         user = FirebaseAuth.getInstance().getCurrentUser();

        View headerview = nav.getHeaderView(0);
        TextView htextview = headerview.findViewById(R.id.textemail);
        if(user!=null){
            htextview.setText(user.getEmail());
        }


        //camera

        open_camera = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        gallery = (FloatingActionButton) findViewById(R.id.gallery);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(imageView);
        Glide.with(this).load(R.drawable.scanning).into(imageViewTarget);

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
                /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);*/

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                 imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intentn = new Intent();
                intentn.setType("image/*");
                intentn.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentn.createChooser(intentn,"SELECT IMAGE"),GALLERY_REQUEST_CODE);*/
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
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
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user==null) {


                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),

                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.AnonymousBuilder().build());

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }

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
            Intent intenth1 = new Intent(MainActivity.this , firstActivity.class);
            startActivity(intenth1);

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
                   // Uri uri1 = data.getData();
                   /* CropImage.activity(uri1)
                            .start(this);*/

                  //  Uri uri = getImageUri(getApplicationContext(),bitmapImage1);

                    //change
                   /* Bitmap bitmapImage1 = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getApplicationContext(), bitmapImage1);
                    CropImage.activity(tempUri)
                            .start(this);*/
                    //change 2
                    try {
                        Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);

                        String imageurl = getRealPathFromURI(imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CropImage.activity(imageUri)
                            .start(this);

                   /* Intent   camintent1= new Intent(MainActivity.this,ImageText.class);
                    camintent1.putExtra("bitmap", bitmapImage1);
                    startActivity(camintent1);*/


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
                   //Crop Activity below



                    Intent   gallintent= new Intent(MainActivity.this,ImageText.class);
                    gallintent.putExtra("uri", uri);
                    Log.i("nice","gallery is working");
                    startActivity(gallintent);

                }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri uri = result.getUri();
                    Intent   cropintent= new Intent(MainActivity.this,ImageText.class);
                    cropintent.putExtra("uri", uri);
                    Log.i("nice","crop is working");
                    startActivity(cropintent);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }




        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
