package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ServiceActivity extends AppCompatActivity {

    //Explicit
    private TextView nameDriverTextView, idDriverTextView;
    private Button jobListButton, closeButton;
    private ListView listView;
    private String[] loginStrings;
    private MyConstant myConstant = new MyConstant();
    private String[] planDateStrings, cnt_storeStrings;
    private boolean aBoolean = true;

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

        //get value from intent
        loginStrings = getIntent().getStringArrayExtra("Login");

        //Show name
        nameDriverTextView.setText(loginStrings[1]);

        //SyncData
        SynDataWhereByDriverID synDataWhereByDriverID = new SynDataWhereByDriverID(ServiceActivity.this);
        synDataWhereByDriverID.execute(myConstant.getUrlDataWhereDriverID());

    }//Main Method

    private class SynDataWhereByDriverID extends AsyncTask<String, Void, String> {
        //Explicit
        private ProgressDialog progressDialog;
        private Context context;

        public SynDataWhereByDriverID(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder().add("isAdd", "true").add("driver_id", loginStrings[0]).build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();

            } catch (Exception e) {
                Log.d("12octV1", "e doInBack ==> " + e.toString());
                return null;
            }
//            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.d("12octV1", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);
                planDateStrings = new String[jsonArray.length()];
                cnt_storeStrings = new String[jsonArray.length()];
                for (int i = 0;i < jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    planDateStrings[i] = jsonObject.getString("planDate");
                    cnt_storeStrings[i] = jsonObject.getString("cnt_store");
                }//for
                if (aBoolean) {
                    //true :: not click on button
                    jobListButton.setText("Job List :: " + planDateStrings[0]);
                }

                //Get Event From Click
                jobListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ServiceActivity.this, JobListView.class);
                        intent.putExtra("Date", planDateStrings);
                        intent.putExtra("Store", cnt_storeStrings);
                        startActivity(intent);
                    }//On Click
                });



            } catch (Exception e) {
                Log.d("12octV1", "e onPost ==> " + e.toString());

            }
        }
    }//SynDataWhereByDriverID

}//Main Class
