package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class JobListView extends AppCompatActivity {
    //Explicit
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list_view);

        //Bind Widget
        listView = (ListView) findViewById(R.id.livShowDate);

        //Create Listview
        String[] dateStrings = getIntent().getStringArrayExtra("Date");
        String[] storeStrings = getIntent().getStringArrayExtra("Store");

        DateAdapter dateAdapter = new DateAdapter(JobListView.this, dateStrings, storeStrings);
        listView.setAdapter(dateAdapter);

    }//Main Method
}//Main Class
