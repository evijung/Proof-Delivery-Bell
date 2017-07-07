package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Arrays;

public class TripDateActivity extends Activity {
    private String[] loginStrings, cntTripStrings, tripDateStrings, tripPlanIdStrings, truckNoStrings;
    private String planIdString, dateString, truckNoString;
    MyConstant myConstant = new MyConstant();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_date);

        loginStrings = getIntent().getStringArrayExtra("Login");
        planIdString = getIntent().getStringExtra("PlanId");
        truckNoString = getIntent().getStringExtra("TruckNo");
        dateString = getIntent().getStringExtra("Date");

        listView = (ListView) findViewById(R.id.lisTripDate);

        SynTripDate synTripDate = new SynTripDate();
        synTripDate.execute();
    }

    private class SynTripDate extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("driver_id", loginStrings[0])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(myConstant.getUrlGetTripDate()).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", "Trip Date JSON ==> " + s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                cntTripStrings = new String[jsonArray.length()];
                tripDateStrings = new String[jsonArray.length()];
                tripPlanIdStrings = new String[jsonArray.length()];
                truckNoStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    cntTripStrings[i] = jsonObject.getString("cnt_trip");
                    tripPlanIdStrings[i] = jsonObject.getString("planId");
                    tripDateStrings[i] = jsonObject.getString("planDate");
                    truckNoStrings[i] = jsonObject.getString("truck_no");
                }

                Log.d("Tag", "tripDateStrings ==> " + Arrays.toString(tripDateStrings));

                TripDateAdapter tripDateAdapter = new TripDateAdapter(TripDateActivity.this, tripPlanIdStrings, cntTripStrings, tripDateStrings);
                listView.setAdapter(tripDateAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(TripDateActivity.this, JobManagementActivity.class);
                        intent.putExtra("Login", loginStrings);
                        intent.putExtra("PlanId", tripPlanIdStrings[i]);
                        intent.putExtra("Date", tripDateStrings[i]);
                        intent.putExtra("TruckNo", truckNoStrings[i]);
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






}
