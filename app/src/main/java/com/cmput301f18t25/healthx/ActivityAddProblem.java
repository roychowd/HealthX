package com.cmput301f18t25.healthx;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ActivityAddProblem extends AppCompatActivity {

    private ProblemList mProblemList = ProblemList.getInstance();
    private OfflineBehaviour offline = OfflineBehaviour.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        // if clicked the save button,
        if (id == android.R.id.home) {
            Intent intent = new Intent();
            setResult(10,intent);
            Log.i("CWei", "finished");
            finish();
//            Bundle bundle = this.getIntent().getExtras();
//            Intent intent = new Intent(this, ViewProblemList.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
        }
        if (id == R.id.save_button) {


            EditText title_textView = (EditText) findViewById(R.id.title_input);
            DatePicker dateStarted_textView = findViewById(R.id.dateStarted_input);
            EditText description_textView = (EditText) findViewById(R.id.description_input);


            String problemTitle = title_textView.getText().toString();
            Date selected = new Date(dateStarted_textView.getYear() - 1900,
                    dateStarted_textView.getMonth(), dateStarted_textView.getDayOfMonth());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String problemDate = format.format(selected);
            String problemDescription = description_textView.getText().toString();

//            // Check if app is connected to a network.
//            Problem newProblem = new Problem(problemTitle, problemDescription, problemDate, mProblemList.getUser().getId());
//            mProblemList.addToProblemList(newProblem);
//            OfflineBehaviour offline = new OfflineBehaviour();

            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null == activeNetwork) {
                Toast.makeText(getApplicationContext(), "You are offline.", Toast.LENGTH_SHORT).show();
//                newProblem.setId(UUID.randomUUID().toString()); // set a random id
//                offline.addItem(newProblem, "ADD");
//                finish();
            } else {
                Problem newProblem = new Problem(problemTitle, problemDescription, problemDate, mProblemList.getUser().getId());
                //Bundle bundle = getIntent().getExtras();
                Toast.makeText(this,problemDate,Toast.LENGTH_LONG).show();
                ElasticSearchProblemController.AddProblemTask addProblemTask = new ElasticSearchProblemController.AddProblemTask();
                addProblemTask.execute(newProblem);
                try {
                    Thread.sleep(1000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                Intent intent = new Intent();
                setResult(10,intent);
                Log.i("CWei", "finished adding");
                finish();
//

            }



        }
        return super.onOptionsItemSelected(item);

    }


}
