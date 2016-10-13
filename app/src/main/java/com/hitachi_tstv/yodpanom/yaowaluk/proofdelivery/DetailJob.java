package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class DetailJob extends AppCompatActivity implements View.OnClickListener {
    //Explicit
    private TextView jobNoTextView, storeCodeTextView, storeNameTextView, arrivalTextView, intentToCallTextView;
    private ListView listView;
    private ImageView firstImageView, secondImageView, thirdImageView;
    private Button arrivalButton, takeImgButton, confirmButton, signatureButton;
    private MyConstant myConstant = new MyConstant();
    private String[] loginStrings, containerStrings, quantityStrings;
    private String planDtl_Id, pathFirstImageString, pathSecondImageString, pathThirdImageString;


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

        SynContainList synContainList = new SynContainList(DetailJob.this);
        synContainList.execute(myConstant.getUrlContainerList(), planDtl_Id);

        //Get Event From Click Button or Image
        firstImageView.setOnClickListener(DetailJob.this);
        secondImageView.setOnClickListener(DetailJob.this);
        thirdImageView.setOnClickListener(DetailJob.this);
        arrivalButton.setOnClickListener(DetailJob.this);
        takeImgButton.setOnClickListener(DetailJob.this);
        confirmButton.setOnClickListener(DetailJob.this);
        signatureButton.setOnClickListener(DetailJob.this);

    }//Main Method

    private class SynContainList extends AsyncTask<String, Void, String> {
        //Explicit
        private Context context;

        public SynContainList(Context context) {
            this.context = context;
            Log.d("Tag", "Construct SynContainList");
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
                Log.d("12octV6", "e doInBack ==> " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                containerStrings = new String[jsonArray.length()];
                quantityStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    containerStrings[i] = jsonObject.getString("container");
                    quantityStrings[i] = jsonObject.getString("qty");
                }

                ContainerListAdapter containerListAdapter = new ContainerListAdapter(containerStrings, quantityStrings, context);
                listView.setAdapter(containerListAdapter);

            } catch (Exception e) {
                Log.d("12octV6", "e onPost ==> " + e);
            }
            Log.d("12octV6", "JSON ==> " + s);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0: //From Take Photo
                if (resultCode == RESULT_OK) {
                    Log.d("12octV5", "Take Photo and Save Success");
                }
                break;
            case 1://From Select Image First
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    pathFirstImageString = myFindPathImage(uri);
                    Log.d("12octV5", "Path First ==> " + pathFirstImageString);

                }
                break;
            case 2:
                break;
            case 3:
                break;

        }

    }

    private String myFindPathImage(Uri uri) {
        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);
        } else {
            result = uri.getPath();
        }

        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView5: //First

                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(Intent.createChooser(intent1, "Please Choose Photo"), 1);

                break;
            case R.id.imageView4: //Second
                break;
            case R.id.imageView3: //Third
                break;
            case R.id.button6: //Take Image

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);

                break;
            case R.id.button7: //Arrival
                break;
            case R.id.button8: //Signature
                break;
            case R.id.button9: //Confirm
                break;
        }//switch
    }// onClick

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
