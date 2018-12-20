package com.example.diram.parkingspacefinder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.diram.parkingspacefinder.helper.Progress;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private Context context;
    ImageView userprofile1;
    private int REQUEST_CAMERA = 0;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private int SELECT_FILE = 1;
    Bitmap photo;
    String encodedCameraImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    context = this;
    userprofile1 = findViewById(R.id.imageView4);
    }

    public void onClick(View view) {


        EditText username = findViewById(R.id.editText3);
        EditText email = findViewById(R.id.editText5);
        EditText password = findViewById(R.id.editText6);
        EditText mobilenumber = findViewById(R.id.editText4);
        ImageView userprofile1 = findViewById(R.id.imageView4);
        final Progress loading = new Progress(this, "", "Loading please wait ...");
        loading.showDialog();
        Ion.with(this)
                .load("https://dirambora97.000webhostapp.com/parking/v1/Api.php")
                .setMultipartParameter("username", username.getText().toString())
                .setMultipartParameter("email", email.getText().toString())
                .setMultipartParameter("password", password.getText().toString())
                .setMultipartParameter("mobilenumber", mobilenumber.getText().toString())
               // .setMultipartFile("iconPath", new File(photo))
                .setMultipartParameter("action", "save")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        loading.dismissDialog();
                        if (e == null) {
                            Log.e("Server=>", result);
                            try {
                                JSONObject resultBack = new JSONObject(result);
                                if (resultBack.getBoolean("status")) {
                                    setUserProfile(resultBack.getJSONObject("profile"));
                                    goHome();
                                } else {
                                    Toast.makeText(context, "Password or username is invalid", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e1) {
                                Toast.makeText(context, "Check network info" + e1.getMessage(), Toast.LENGTH_LONG).show();
                                e1.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.existing:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                return true;
            case R.id.camera_photo:

                if (checkSelfPermission(Manifest.permission.CAMERA)   != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                photo = null;
                if (data != null) {
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                userprofile1.setImageBitmap(photo);
                Glide.with(this).load(photo)
                        .apply(new RequestOptions().circleCrop())
                        .into(userprofile1);

            } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                photo = (Bitmap) data.getExtras().get("data");
                userprofile1.setImageBitmap(photo);
                Glide.with(this).load(photo)
                        .apply(new RequestOptions().circleCrop())
                        .into(userprofile1);


                encodedCameraImage = encodeImage(photo);


            }

        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private void setUserProfile(JSONObject profile) {
        SharedPreferences.Editor editor = getSharedPreferences("parking", MODE_PRIVATE).edit();
        editor.putString("profile", profile.toString());
        editor.apply();
    }


    private void goHome() {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}

