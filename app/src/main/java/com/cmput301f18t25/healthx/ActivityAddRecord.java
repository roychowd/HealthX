package com.cmput301f18t25.healthx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityAddRecord extends AppCompatActivity {
    Bitmap recordPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        EditText title = (EditText) findViewById(R.id.record_title);
        EditText comment = (EditText) findViewById(R.id.record_comment);

        String recordTitle = title.getText().toString();
        String recordComment = comment.getText().toString();

        // if clicked the save button,
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ViewRecordList.class);
            startActivity(intent);
        }
        if (id == R.id.save_button) {
            Record newRecord = new Record(recordTitle, recordComment, 0.00, 0.00, recordPhoto);

        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.view_photo);
        byte[] byteArray = data.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        // Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
        // Drawable drawable = new BitmapDrawable(bitmapScaled);
        // imageView.setImageDrawable(drawable);
        imageView.setImageBitmap(bitmap);
        recordPhoto = bitmap;

    }

    public void addPhoto(View view){

        Intent photoIntent = new Intent(this, ActivityAddPhoto.class);
        startActivityForResult(photoIntent, 1);

    }

}