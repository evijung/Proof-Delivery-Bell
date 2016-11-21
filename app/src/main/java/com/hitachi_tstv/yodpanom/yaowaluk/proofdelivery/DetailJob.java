package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailJob extends Activity implements View.OnClickListener {
    //Explicit
    private TextView jobNoTextView, storeCodeTextView, storeNameTextView, arrivalTextView, intentToCallTextView;
    private ListView listView;
    private ImageView firstImageView, secondImageView, thirdImageView;
    private Uri firstUri, secondUri, thirdUri;
    private Button arrivalButton, takeImgButton, confirmButton, signatureButton, contractButton, backButton;
    private MyConstant myConstant = new MyConstant();
    private String[] loginStrings, containerStrings, quantityStrings;
    private String planDtl2_Id, pathFirstImageString, pathSecondImageString, pathThirdImageString, driverUserNameString, getTimeDate,storeLatString,storeLngString,storeRadiusString;
    private LocationManager locationManager;
    private Criteria criteria;
    private Bitmap firstBitmap = null;
    private Bitmap secondBitmap = null;
    private Bitmap thirdBitmap = null;
    private int version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);


        version = Build.VERSION.SDK_INT;

        //bind Widget
        bindWidget();

        //Set Invisible The Button
        firstImageView.setVisibility(View.GONE);
        secondImageView.setVisibility(View.GONE);
        thirdImageView.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
        signatureButton.setVisibility(View.GONE);
//        takeImgButton.setVisibility(View.GONE);

        int picRes = R.drawable.picture;
        firstImageView.setImageResource(picRes);
        secondImageView.setImageResource(picRes);
        thirdImageView.setImageResource(picRes);

        //Get Intent Data
        loginStrings = getIntent().getStringArrayExtra("Login");
        planDtl2_Id = getIntent().getStringExtra("planDtl2_id");
        driverUserNameString = loginStrings[2];

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        getTimeDate = dateFormat.format(date);

        //Load Data
        SynData synData = new SynData(DetailJob.this);
        synData.execute(myConstant.getUrlDetailWherePlanId(), planDtl2_Id);

        SynContainList synContainList = new SynContainList(DetailJob.this);
        synContainList.execute(myConstant.getUrlContainerList(), planDtl2_Id);

        //Get Event From Click Button or Image
        firstImageView.setOnClickListener(DetailJob.this);
        secondImageView.setOnClickListener(DetailJob.this);
        thirdImageView.setOnClickListener(DetailJob.this);
        arrivalButton.setOnClickListener(DetailJob.this);
//        takeImgButton.setOnClickListener(DetailJob.this);
        confirmButton.setOnClickListener(DetailJob.this);
        signatureButton.setOnClickListener(DetailJob.this);
        contractButton.setOnClickListener(DetailJob.this);
        backButton.setOnClickListener(DetailJob.this);

    }//Main Method

    private Bitmap rotateBitmap(Bitmap src) {

        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(90);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

    @Override
    public void onBackPressed() {

    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0: //From Take Photo
                if (resultCode == RESULT_OK) {
                    Log.d("12octV5", "Take Photo and Save Success");
                }
                break;
            case 1://From Select Image First

                if (resultCode == RESULT_OK) {

//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    firstImageView.setImageBitmap(photo);
//                    Uri uri = data.getData();
//                    if (version >= Build.VERSION_CODES.KITKAT) {
//
//                        pathFirstImageString = myFindPathImageOverKitkat(firstUri);
//                    } else {
//
//                        pathFirstImageString = myFindPathImage(firstUri);
//                    }
                    pathFirstImageString = firstUri.getPath().toString();
                    Log.d("12octV5", "Path First ==> " + pathFirstImageString);
                    try {
                        firstBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(firstUri));
                        if (firstBitmap.getHeight() < firstBitmap.getWidth()) {
                            firstBitmap = rotateBitmap(firstBitmap);
                        }
                        firstImageView.setImageBitmap(firstBitmap);
                        Log.d("TAG", "Height ==> " + firstBitmap.getHeight() + " Width ==> " + firstBitmap.getWidth());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

//                     firstImageView.setImageBitmap(BitmapFactory.decodeFile(pathFirstImageString));

//                     firstImageView.setImageURI(firstUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {

                    pathSecondImageString = secondUri.toString();
                    Log.d("12octV5", "Path Second ==> " + pathSecondImageString);
                    try {
                        secondBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(secondUri));
                        if (secondBitmap.getHeight() < secondBitmap.getWidth()) {
                            secondBitmap = rotateBitmap(secondBitmap);
                        }
                        secondImageView.setImageBitmap(secondBitmap);
                        Log.d("TAG", "Height ==> " + secondBitmap.getHeight() + " Width ==> " + secondBitmap.getWidth());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


//                    Uri uri = data.getData();
//                    if (version >= Build.VERSION_CODES.KITKAT) {
//
//                        pathSecondImageString = myFindPathImageOverKitkat(uri);
//                    } else {
//
//                        pathSecondImageString = myFindPathImage(uri);
//                    }
//                    Log.d("12octV5", "Path Second ==> " + pathSecondImageString);
//                    try {
//                        secondBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        if (secondBitmap.getHeight() < secondBitmap.getWidth()) {
//                            secondBitmap = rotateBitmap(secondBitmap);
//                        }
//                        Log.d("Tag", "Height ==> " + secondBitmap.getHeight() + " , Width ==> " + secondBitmap.getWidth());
//
//
//                        secondImageView.setImageBitmap(secondBitmap);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    // firstImageView.setImageBitmap(BitmapFactory.decodeFile(pathFirstImageString));

                    // firstImageView.setImageURI(uri);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {

                    pathThirdImageString = thirdUri.toString();
                    Log.d("12octV5", "Path Third ==> " + pathThirdImageString);
                    try {
                        thirdBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(thirdUri));
                        if (thirdBitmap.getHeight() < thirdBitmap.getWidth()) {
                            thirdBitmap = rotateBitmap(thirdBitmap);
                        }
                        thirdImageView.setImageBitmap(thirdBitmap);
                        Log.d("TAG", "Height ==> " + thirdBitmap.getHeight() + " Width ==> " + thirdBitmap.getWidth());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

//                    Uri uri = data.getData();
//                    if (version >= Build.VERSION_CODES.KITKAT) {
//
//                        pathThirdImageString = myFindPathImageOverKitkat(uri);
//                    } else {
//
//                        pathThirdImageString = myFindPathImage(uri);
//                    }
//                    Log.d("12octV5", "Path Third ==> " + pathThirdImageString);
//
//                    try {
//                        thirdBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        if (thirdBitmap.getHeight() < thirdBitmap.getWidth()) {
//                            thirdBitmap = rotateBitmap(thirdBitmap);
//                        }
//                        thirdImageView.setImageBitmap(thirdBitmap);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    // firstImageView.setImageBitmap(BitmapFactory.decodeFile(pathFirstImageString));

                    // firstImageView.setImageURI(uri);
                }
                break;

        }

    }

//    private String myFindPathImage(Uri uri) {
//        String result = null;
//        String[] strings = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri,
//                strings, null, null, null);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(index);
//            cursor.close();
//        } else {
//            result = uri.getPath();
//        }
//
//        cursor.close();
//        return result;
//
//    }
//
//    private String myFindPathImageOverKitkat(Uri uri) {
//
//        String wholeId = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            wholeId = DocumentsContract.getDocumentId(uri);
//        }
//        String id = wholeId.split(":")[1];
//
//        String result = null;
//        String[] strings = {MediaStore.Images.Media.DATA};
//
//        String sel = MediaStore.Images.Media._ID + "=?";
//
//        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                strings, sel, new String[]{id}, null);
//
//        int columnIndex = cursor.getColumnIndex(strings[0]);
//        if (cursor.moveToFirst()) {
//            result = cursor.getString(columnIndex);
//        }
//        cursor.close();
////        if (cursor != null) {
////            cursor.moveToFirst();
////            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
////            result = cursor.getString(index);
////            cursor.close();
////        } else {
////            result = uri.getPath();
////        }
//
//        Log.d("Tag", "Result ==> " + result + ", URI ==> " + uri);
//        Log.d("Tag", "Cursor ==> " + cursor.toString());
//
//        return result;
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView5: //First
                File originalFile1 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "first.png");

                Intent cameraIntent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                firstUri = Uri.fromFile(originalFile1);
                Log.d("TAG", "Path 1 " + firstUri);
                cameraIntent1.putExtra(MediaStore.EXTRA_OUTPUT, firstUri);
                startActivityForResult(cameraIntent1, 1);

//                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
//                intent1.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent1, "Please Choose Photo"), 1);

                break;
            case R.id.imageView4: //Second
                File originalFile2 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "second.png");

                Intent cameraIntent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                secondUri = Uri.fromFile(originalFile2);
                Log.d("TAG", "Path 2 " +   secondUri);
                cameraIntent2.putExtra(MediaStore.EXTRA_OUTPUT, secondUri);
                startActivityForResult(cameraIntent2, 2);

//                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
//                intent2.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent2, "Please Choose Photo"), 2);

                break;
            case R.id.imageView3: //Third
                File originalFile3 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "third.png");

                Intent cameraIntent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                thirdUri = Uri.fromFile(originalFile3);
                Log.d("TAG", "Path " + thirdUri);
                cameraIntent3.putExtra(MediaStore.EXTRA_OUTPUT, thirdUri);
                startActivityForResult(cameraIntent3, 3);

//                Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
//                intent3.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent3, "Please Choose Photo"), 3);

                break;
//            case R.id.button6: //Take Image
//
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(intent, 0);
//
//                break;
            case R.id.button7: //Arrival
                String strLat = "Unknown";
                String strLng = "Unknown";
                setupLocation();
                Location networkLocation = requestLocation(LocationManager.NETWORK_PROVIDER, "No Internet");
                if (networkLocation != null) {
                    strLat = String.format("%.7f", networkLocation.getLatitude());
                    strLng = String.format("%.7f", networkLocation.getLongitude());
                }

                Location gpsLocation = requestLocation(LocationManager.GPS_PROVIDER, "No GPS card");
                if (gpsLocation != null) {
                    strLat = String.format("%.7f", gpsLocation.getLatitude());
                    strLng = String.format("%.7f", gpsLocation.getLongitude());
                }


                if (strLat.equals("Unknown") && strLng.equals("Unknown")) {
                    Toast.makeText(this, "Failure Lat/Lng is Unknown", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("13OctV1", " ++++++++++Latitude.-> " + strLat + " Longitude.-> " + strLng);

                    double lat1, lat2, lng1, lng2;
                    lat1 = Double.parseDouble(strLat);
                    lng1 = Double.parseDouble(strLng);
                    lat2 = Double.parseDouble(storeLatString);
                    lng2 = Double.parseDouble(storeLngString);

                    Log.d("TAG", "lat1 ==> " + lat1 + " lng1 ==> " + lng1 + " lat2 ==> " + lat2 + " lng2 ==> " + lng2);




                    SynGPStoServer synGPStoServer = new SynGPStoServer(DetailJob.this);
                    synGPStoServer.execute(myConstant.getUrlArrivalGPS(), strLat, strLng, getTimeDate, driverUserNameString, planDtl2_Id);

                }
                break;
            case R.id.button8: //Signature
                Intent intentSign = new Intent(DetailJob.this, SignatureActivity.class);
                intentSign.putExtra("Login", loginStrings);
                intentSign.putExtra("PlanDtl", planDtl2_Id);
                startActivity(intentSign);
                break;
            case R.id.button9: //Confirm
                Log.d("Tag1", "First ==> " + pathFirstImageString);
                Log.d("Tag2", "Second ==> " + pathSecondImageString);
                Log.d("Tag3", "Third ==> " + pathThirdImageString);
                if (pathFirstImageString != null && pathSecondImageString != null && pathThirdImageString != null) {

                    SynUploadImage synUploadImage = new SynUploadImage(DetailJob.this, firstBitmap);
                    synUploadImage.execute();

                    synUploadImage = new SynUploadImage(DetailJob.this, secondBitmap);
                    synUploadImage.execute();

                    synUploadImage = new SynUploadImage(DetailJob.this, thirdBitmap);
                    synUploadImage.execute();

                    SynUpdateStatus synUpdateStatus = new SynUpdateStatus(DetailJob.this);
                    synUpdateStatus.execute();

                } else {
                    Toast.makeText(this, "Please take all photo", Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.button10: //Call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String phoneNo = "tel:" + intentToCallTextView.getText().toString();
                callIntent.setData(Uri.parse(phoneNo));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);


                break;

            case R.id.button13: //Back
                Intent backIntent = new Intent(DetailJob.this, ServiceActivity.class);
                backIntent.putExtra("Login", loginStrings);
                backIntent.putExtra("Date", "");
                backIntent.putExtra("PlanId", "");
                backIntent.putExtra("TruckNo", "");
                startActivity(backIntent);
                finish();
        }//switch
    }// onClick

    private class SynUpdateStatus extends AsyncTask<String, Void, String> {
        //Explicit
        private Context context;

        public SynUpdateStatus(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("TAG", "JSON_Upload ==> " + s);
            boolean b = (s.equals("OK"));
            Log.d("Tag", "Bool ==> " + b);
            if (s.equals("OK")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Save Job Complete!!", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(DetailJob.this, ServiceActivity.class);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("Date", "");
                intent.putExtra("PlanId", "");
                intent.putExtra("TruckNo", "");
                startActivity(intent);

                finish();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Save Job Incomplete!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("PlanDtl2_ID", planDtl2_Id)
                        .add("Driver_Name", loginStrings[2])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(myConstant.getUrlUpdateStatus()).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                return "NOK2";
            }
        }
    }

    private class SynUploadImage extends AsyncTask<Void, Void, String> {
        //Explicit
        private Context context;
        private Bitmap bitmap;
        private UploadImageUtils uploadImageUtils;
        private String mUploadedFileName;
        private ProgressDialog progressDialog;

        public SynUploadImage(Context context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Waiting...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            uploadImageUtils = new UploadImageUtils();
            mUploadedFileName = uploadImageUtils.getRandomFileName();
            final String result = uploadImageUtils.uploadFile(mUploadedFileName, myConstant.getUrlSaveImage(), bitmap, planDtl2_Id, "P");
            Log.d("TAG", "Do in back after save:-->" + result);
            if (result == "NOK") {
                return "NOK1";
            } else {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Log.d("name", mUploadedFileName);
                    RequestBody requestBody = new FormEncodingBuilder()
                            .add("isAdd", "true")
                            .add("PlanDtl2_ID", planDtl2_Id)
                            .add("File_Name", mUploadedFileName)
                            .add("File_Path", result)
                            .add("Driver_Name", loginStrings[2])
                            .build();
                    Request.Builder builder = new Request.Builder();
                    Request request = builder.url(myConstant.getUrlSaveImagePath()).post(requestBody).build();
                    Response response = okHttpClient.newCall(request).execute();

                    return response.body().string();
                } catch (Exception e) {

                    return "NOK2";
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            Log.d("TAG", "JSON_Upload ==> " + s);
            boolean b = (s.equals("OK"));
            Log.d("Tag", "Bool ==> " + b);
            if (s.equals("OK")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Add Image Successful!!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Add Image Unsuccessful!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

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
                intentToCallTextView.setText(jsonObject.getString("store_tel"));

                storeLatString = jsonObject.getString("store_lat");
                storeLngString = jsonObject.getString("store_long");
                storeRadiusString = jsonObject.getString("gps_radius");
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
//        takeImgButton = (Button) findViewById(R.id.button6);
        confirmButton = (Button) findViewById(R.id.button9);
        signatureButton = (Button) findViewById(R.id.button8);
        contractButton = (Button) findViewById(R.id.button10);
        backButton = (Button) findViewById(R.id.button13);
    }

    private class SynGPStoServer extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public SynGPStoServer(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Lat", params[1])
                        .add("Lng", params[2])
                        .add("stamp", params[3])
                        .add("drv_username", params[4])
                        .add("planDtl2_id", params[5])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                Log.d("13OctV1", "doInBackSynGPS--->" + e.toString());
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("13OctV1", "JSON__GPS->" + s);
            if (s.equals("Success")) {
                firstImageView.setVisibility(View.VISIBLE);
                secondImageView.setVisibility(View.VISIBLE);
                thirdImageView.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
//                takeImgButton.setVisibility(View.VISIBLE);
                signatureButton.setVisibility(View.VISIBLE);
                arrivalButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Update GPS To Server Successful!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Update GPS To Server Unsuccessful!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }   //onPostExcute
    }// Class Syn GPS TO SERVER

    public Location requestLocation(String strProvider, String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("GPS", strError);
        }


        return location;
    }

    public final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            latTextView.setText(String.format("%.7f", location.getLatitude()));
//            lngTextView.setText(String.format("%.7f", location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void setupLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);


    }   // setupLocation
}//Main Class
