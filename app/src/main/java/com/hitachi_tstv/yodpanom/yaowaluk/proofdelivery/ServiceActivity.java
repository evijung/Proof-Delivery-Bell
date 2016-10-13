package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ServiceActivity extends AppCompatActivity {

    //Explicit
    private TextView nameDriverTextView, idDriverTextView;
    private Button jobListButton, closeButton;
    private ListView listView;
    private String[] loginStrings;
    private MyConstant myConstant = new MyConstant();
    private String[] planDateStrings, cnt_storeStrings, planIdStrings;
    private boolean aBoolean = true;
    private String[] workSheetStrings, storeNameStrings,
            planArrivalTimeStrings, planDtl2_idStrings, truckIdStrings;
    private String driverChooseString, dateChooseString, truckString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //Bind Widget
        nameDriverTextView = (TextView) findViewById(R.id.textView2);
        idDriverTextView = (TextView) findViewById(R.id.textView4);
        jobListButton = (Button) findViewById(R.id.button3);
        closeButton = (Button) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.listJob);

        //Get Value from Intent
        loginStrings = getIntent().getStringArrayExtra("Login");
        driverChooseString = getIntent().getStringExtra("PlanId");
        dateChooseString = getIntent().getStringExtra("Date");
        truckString = getIntent().getStringExtra("TruckId");

        if (driverChooseString.length() != 0) {

            aBoolean = false;

        } else {
            //From Main Activity

        }

        //Show Name
        nameDriverTextView.setText(loginStrings[1]);

        //Syn data
        SynDataWhereByDriverID synDataWhereByDriverID = new SynDataWhereByDriverID(ServiceActivity.this);
        synDataWhereByDriverID.execute(myConstant.getUrlDataWhereDriverID());



        //Close Controller
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }   // Main Method

    private class SynTruckLicense extends AsyncTask<String, Void, String> {
        //Explicit
        private Context context;
        private String truckIDString;


        public SynTruckLicense(Context context, String truckIDString) {
            this.context = context;
            this.truckIDString = truckIDString;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder().add("isAdd", "true").add("TruckId", truckIDString).build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(myConstant.getUrlTruckLicense()).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                Log.d("Tag", "e doInBack ==> " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Tag", "JSON ==> " + s);
        }
    }


    private class SynDataWhereByDriverID extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public SynDataWhereByDriverID(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("driver_id", loginStrings[0])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("12octV1", "e doInBack ==> " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("12octV1", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);
                planDateStrings = new String[jsonArray.length()];
                cnt_storeStrings = new String[jsonArray.length()];
                planIdStrings = new String[jsonArray.length()];
                truckIdStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    planDateStrings[i] = jsonObject.getString("planDate");
                    cnt_storeStrings[i] = jsonObject.getString("cnt_store");
                    planIdStrings[i] = jsonObject.getString("planId");
                    truckIdStrings[i] = jsonObject.getString("truck_no");

                }   // for

                if (aBoolean) {

                    //True Not Click on Button
                    jobListButton.setText("Job List = " + planDateStrings[0]);
                    truckString = truckIdStrings[0];
                    createDetailList(planIdStrings[0]);

                } else {
                    // From Job List View
                    jobListButton.setText("Job List = " + dateChooseString);
                    createDetailList(driverChooseString);
                }


                // Get Event From Click
                jobListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ServiceActivity.this, JobListView.class);
                        intent.putExtra("Date", planDateStrings);
                        intent.putExtra("Store", cnt_storeStrings);
                        intent.putExtra("Login", loginStrings);
                        intent.putExtra("PlanId", planIdStrings);
                        intent.putExtra("TruckId", truckIdStrings);
                        startActivity(intent);
                        finish();

                    }   // onClick
                });


            } catch (Exception e) {
                Log.d("12octV1", "e onPost ==> " + e.toString());
            }


        }   // onPost

    }   // SynDataWhereByDriverID

    private void createDetailList(String planIDString) {

        SynDetail synDetail = new SynDetail(ServiceActivity.this,
                planIDString);
        synDetail.execute(myConstant.getUrlDataWhereDriverIDanDate());

    }   // createDetailList

    private class SynDetail extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;
        private String planIdString;

        public SynDetail(Context context,
                         String planIdString
        ) {
            this.context = context;
            this.planIdString = planIdString;

        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("planId", planIdString)
                        .add("driver_id", loginStrings[0])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("12octV2", "e doInBack " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("12octV2", "JSoN ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);

                workSheetStrings = new String[jsonArray.length()];
                storeNameStrings = new String[jsonArray.length()];
                planArrivalTimeStrings = new String[jsonArray.length()];
                planDtl2_idStrings = new String[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    workSheetStrings[i] = jsonObject.getString("work_sheet_no");
                    storeNameStrings[i] = jsonObject.getString("store_nameEng");
                    planArrivalTimeStrings[i] = jsonObject.getString("plan_arrivalTime");
                    planDtl2_idStrings[i] = jsonObject.getString("planDtl2_id");

                }   // for

                DetailAdapter detailAdapter = new DetailAdapter(context,
                        workSheetStrings, storeNameStrings, planArrivalTimeStrings);
                listView.setAdapter(detailAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ServiceActivity.this,DetailJob.class);
                        intent.putExtra("Login", loginStrings);
                        intent.putExtra("planDtl2_id", planDtl2_idStrings[i]);
                        startActivity(intent);

                    }
                });


            } catch (Exception e) {
                Log.d("12octV2", "e onPost ==> " + e.toString());
            }


        }   // onPost

    }   // SynDetail


}   // Main Class