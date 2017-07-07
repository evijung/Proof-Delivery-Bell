package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JobManagementActivity extends Activity {
    private String[] loginStrings, workSheetNoStrings, storeStrings,routeNoStrings, departDateStrings, planDtlIdStrings;
    private String planIdString, dateString, truckString;
    private ListView listView;
    private Button dateButton;
    private MyConstant myConstant;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_management);

        myConstant = new MyConstant();
        //Bind Widget
        listView = (ListView) findViewById(R.id.lisTripJob);
        dateButton = (Button) findViewById(R.id.button5);

        //Get intent extra
        loginStrings = getIntent().getStringArrayExtra("Login");
        planIdString = getIntent().getStringExtra("PlanId");
        dateString = getIntent().getStringExtra("Date");
        truckString = getIntent().getStringExtra("TruckNo");

        dateButton.setText(dateString);

        SynTripData synTripData = new SynTripData(this);
        synTripData.execute(myConstant.getUrlGetTripData());

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobManagementActivity.this,TripDateActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("PlanId", planIdString);
                intent.putExtra("TruckNo", truckString);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(JobManagementActivity.this,ServiceActivity.class);
        intent.putExtra("Date", dateString);
        intent.putExtra("Login", loginStrings);
        intent.putExtra("PlanId", planIdString);
        intent.putExtra("TruckNo", truckString);
        startActivity(intent);
    }

    private class SynTripData extends AsyncTask<String, Void, String> {
        private Context context;

        public SynTripData(Context context) {
            this.context = context;
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
            } catch (IOException e) {
                Log.d("Tag", "Do in back ==> " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", "JSON ==> " + s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                //Set array size
                planDtlIdStrings = new String[jsonArray.length()];
                workSheetNoStrings = new String[jsonArray.length()];
                routeNoStrings = new String[jsonArray.length()];
                departDateStrings = new String[jsonArray.length()];
                storeStrings = new String[jsonArray.length()];

                for (int i = 0;i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    planDtlIdStrings[i] = jsonObject.getString("planDtl_id");
                    workSheetNoStrings[i] = jsonObject.getString("work_sheet_no");
                    routeNoStrings[i] = jsonObject.getString("runningNo");
                    departDateStrings[i] = jsonObject.getString("plan_dc_st_departureDate");
                    storeStrings[i] = jsonObject.getString("store");
                }

                JobManagementListViewAdapter jobManagementListViewAdapter = new JobManagementListViewAdapter(context, workSheetNoStrings, routeNoStrings, departDateStrings, storeStrings);
                listView.setAdapter(jobManagementListViewAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(JobManagementActivity.this,TripDetailActivity.class);
                        intent.putExtra("Date", dateString);
                        intent.putExtra("Login", loginStrings);
                        intent.putExtra("PlanId", planIdString);
                        intent.putExtra("PlanDtlId", planDtlIdStrings[i]);
                        intent.putExtra("TruckNo", truckString);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                Log.d("Tag", "On post ==> " + e);
            }
        }
    }
}
