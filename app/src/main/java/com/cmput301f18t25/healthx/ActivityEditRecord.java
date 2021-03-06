/*
 * Class Name: ActivityEditRecord
 *
 * Version: Version 1.0
 *
 * Date : December 3, 2018
 *
 * Copyright (c) Team 25, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */


package com.cmput301f18t25.healthx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cmput301f18t25.healthx.PermissionRequest.verifyPermission;
/**
 * This is the activity that allows the user to edit an existing record.
 *
 * @author Ivan
 * @author Dhruba
 * @author Cecilia
 * @version 1.0
 *
 */

public class ActivityEditRecord extends AppCompatActivity {

    private LocationManager locationManager;
    Location location;
    double longitude;
    double latitude;
    String title;
    String comment;
    String dateString;
    String problemId;
    Record oldRecord;
    ArrayList<String> imageURIs;
    ArrayList<String> oldURIs;

    Uri imageFileUri;
    ImageView imagePhoto;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    int problemPosition;
    int recordPostion;

    private ProblemList mProblemList = ProblemList.getInstance();
    private OfflineBehaviour offline = OfflineBehaviour.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageURIs = new ArrayList<>(10);
        oldURIs= new ArrayList<>(10);

        Bundle bundle = this.getIntent().getExtras();

        EditText title_textView = findViewById(R.id.record_title);
        EditText comment_textView = findViewById(R.id.record_comment);
        DatePicker recordDate_T = findViewById(R.id.recordDate);
        imagePhoto = findViewById(R.id.view_record_photo);

        oldRecord = (Record) bundle.getSerializable("record");
        title = oldRecord.getTitle();
        comment = oldRecord.getComment();
        dateString = oldRecord.getDate();
        problemId = oldRecord.getProblemID();

        try {
            if (oldRecord.getImageURIs().size() != 0) {
                oldURIs = oldRecord.getImageURIs();
                imageURIs.addAll(oldURIs);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        problemPosition = bundle.getInt("position");
        recordPostion = bundle.getInt("recordPositon");

        title_textView.setText(title);
        comment_textView.setText(comment);
        recordDate_T.updateDate(Integer.valueOf(dateString.substring(0, 4)),
                Integer.valueOf(dateString.substring(5, 7)) - 1,
                Integer.valueOf(dateString.substring(8, 10)));

        if (imageURIs.size() > 0) {
            imagePhoto.setImageDrawable(Drawable.createFromPath(imageURIs.get(0)));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /** if clicked the save button*/
        if (id == android.R.id.home) {
            if (id == android.R.id.home) {
                Intent intent = new Intent();
                setResult(10,intent);
                finish();
            }
        }
        if (id == R.id.save_button) {

            EditText title_textView = findViewById(R.id.record_title);
            EditText comment_textView = findViewById(R.id.record_comment);
            DatePicker recordDate_T = findViewById(R.id.recordDate);

            Date selected = new Date(recordDate_T.getYear() - 1900,
                    recordDate_T.getMonth(), recordDate_T.getDayOfMonth());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String recordDate = format.format(selected);

            Toast.makeText(getApplicationContext(), recordDate, Toast.LENGTH_SHORT).show();


            String recordTitle = title_textView.getText().toString();
            String recordComment = comment_textView.getText().toString();
            setGeoLocation();

            /** Check if app is connected to a network */

            Record newRecord = new Record(recordTitle, recordComment, latitude, longitude, imageURIs,recordDate,problemId);
            newRecord.setId(oldRecord.getId());

            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null == activeNetwork) {

                offline.addItem(oldRecord, "DELETE");
                offline.addItem(newRecord, "ADD");
                Toast.makeText(getApplicationContext(), "You are offline.", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                Intent intent = new Intent();
                setResult(10,intent);
                finish();
            } else {

                ElasticSearchRecordController.DeleteRecordTask deleteRecordTask = new ElasticSearchRecordController.DeleteRecordTask();
                deleteRecordTask.execute(oldRecord);


                ElasticSearchRecordController.AddRecordTask addRecordTask = new ElasticSearchRecordController.AddRecordTask();
                addRecordTask.execute(newRecord);
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                Intent intent = new Intent();
                setResult(10,intent);

                finish();
            }

        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                imagePhoto.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
                imageURIs.add(imageFileUri.getPath());
            }
        }
    }

    public void addPhoto(View view){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }


        try {
            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
            m.invoke(null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        verifyPermission(this);

        String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";

        File imageFile = new File(folder,imageFilePath);
        imageFileUri = Uri.fromFile(imageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    public void setGeoLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null){
                longitude = location.getLongitude();
                latitude = location.getLatitude();

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                setGeoLocation();
                break;
        }

    }

}
