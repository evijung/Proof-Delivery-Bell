package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailJob extends AppCompatActivity {
    //Explicit
    private TextView jobNoTextView, storeCodeTextView, storeNameTextView, arrivalTextView, intentToCallTextView;
    private ListView listView;
    private ImageView firstImageView, secondImageView, thirdImageView;
    private Button arrivalButton, takeImgButton, confirmButton, signatureButton;
    private MyConstant myConstant = new MyConstant();
    private String[] loginStrings;
    private String planDtl_Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);

        //bind Widget
        bindWidget();

        //Get Intent Data
        loginStrings = getIntent().getStringArrayExtra("Login");
        planDtl_Id = getIntent().getStringExtra("planDtl2_id");

        //Load Data
        SynData synData = new SynData(DetailJob.this);
        synData.execute(myConstant.getUrlDetailWherePlanId(), planDtl_Id);


    }//Main Method

    private class SynData extends AsyncTask<String, Void, String> {
        //Explicit
        private Context context;

        public SynData(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder().add("isAdd", "true").add("planDtl2_id", strings[1]).build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();


            } catch (Exception e) {
                Log.d("12octV4", "e doInBack ==> " + e);
                return null;
            }
        }//doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("12octV4", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                //Show Text
                jobNoTextView.setText("Job No : " + jsonObject.getString("work_sheet_no"));
                storeCodeTextView.setText("Store Code : " + jsonObject.getString("store_code"));
                storeNameTextView.setText("Store Name : " + jsonObject.getString("store_nameEng"));
                arrivalTextView.setText("Arrival : " + jsonObject.getString("plan_arrivalDateTime"));
                intentToCallTextView.setText("Call : " + jsonObject.getString("store_tel"));
            } catch (Exception e) {
                Log.d("12octV4", "e onPost ==> " + e);
            }
        }
    }//SynData


    private void bindWidget() {

        //Bind Widget
        jobNoTextView = (TextView) findViewById(R.id.textView14);
        storeCodeTextView = (TextView) findViewById(R.id.textView15);
        storeNameTextView = (TextView) findViewById(R.id.textView16);
        arrivalTextView = (TextView) findViewById(R.id.textView17);
        intentToCallTextView = (TextView) findViewById(R.id.textView18);
        listView = (ListView) findViewById(R.id.livContainer);
        firstImageView = (ImageView) findViewById(R.id.imageView5);
        secondImageView = (ImageView) findViewById(R.id.imageView4);
        thirdImageView = (ImageView) findViewById(R.id.imageView3);
        arrivalButton = (Button) findViewById(R.id.button7);
        takeImgButton = (Button) findViewById(R.id.button6);
        confirmButton = (Button) findViewById(R.id.button9);
        signatureButton = (Button) findViewById(R.id.button8);
    }
}//Main Class
