package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TripDetailActivity extends Activity {
    private String[] loginStrings, storeCodeStrings, storeNameStrings, storeNameEnStrings, storeArrivalStrings;
    private String planIdString, planDtlString, dateString, truckNoString, workSheetNoString, runningNoString, tripNoString;
    private String planDCArriveString, planDCDepartString, departDCString, arriveDCString, mileSTString, mileENString, latString, longString;
    private String departPathString, arrivePathString, flagStartString, flagStopString, storeArrayString, pathStopImageString, pathStartImageString;
    private Bitmap startBitmap, stopBitmap;
    private MyConstant myConstant;
    private TextView jobTextView, startTimeTextView, stopTimeTextView;
    private ImageView startImageView, stopImageView;
    private ListView listView;
    private Button startButton, stopButton;
    private Uri startUri, stopUri;
    private boolean sendStatus;
    private Criteria criteria;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        //Bind Widget
        jobTextView = (TextView) findViewById(R.id.textView31);
        startTimeTextView = (TextView) findViewById(R.id.textView37);
        stopTimeTextView = (TextView) findViewById(R.id.textView38);
        startImageView = (ImageView) findViewById(R.id.imageView8);
        stopImageView = (ImageView) findViewById(R.id.imageView9);
        startButton = (Button) findViewById(R.id.button18);
        stopButton = (Button) findViewById(R.id.button17);
        listView = (ListView) findViewById(R.id.lisDetail);


        int picRes = R.drawable.picture;

        startImageView.setImageResource(picRes);
        stopImageView.setImageResource(picRes);

        //Get Intent Extra
        loginStrings = getIntent().getStringArrayExtra("Login");
        dateString = getIntent().getStringExtra("Date");
        planIdString = getIntent().getStringExtra("PlanId");
        planDtlString = getIntent().getStringExtra("PlanDtlId");
        truckNoString = getIntent().getStringExtra("TruckNo");

        myConstant = new MyConstant();

        SyncTripDetail syncTripDetail = new SyncTripDetail();
        syncTripDetail.execute();


    }

    private class TripDetailAdapter extends BaseAdapter {
        private String[] codeStrings, nameStrings, timeStrings;
        private Context context;
        private TripDetailViewHolder tripDetailViewHolder;

        public TripDetailAdapter(String[] codeStrings, String[] nameStrings, String[] timeStrings, Context context) {
            this.codeStrings = codeStrings;
            this.nameStrings = nameStrings;
            this.timeStrings = timeStrings;
            this.context = context;
        }

        @Override
        public int getCount() {
            return codeStrings.length;
        }

        @Override
        public Object getItem(int i) {
            return codeStrings[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.trip_dtl_listview, viewGroup, false);
                tripDetailViewHolder = new TripDetailViewHolder(view);
                view.setTag(tripDetailViewHolder);
            } else {
                tripDetailViewHolder = (TripDetailViewHolder) view.getTag();
            }

            tripDetailViewHolder.storeCodeTextView.setText(codeStrings[i]);
            tripDetailViewHolder.storeNameTextView.setText(nameStrings[i]);
            tripDetailViewHolder.storeArriveTextView.setText(timeStrings[i]);

            return view;
        }
    }

    private class TripDetailViewHolder {
        TextView storeCodeTextView, storeNameTextView, storeArriveTextView;

        public TripDetailViewHolder(View view) {
            storeCodeTextView = (TextView) view.findViewById(R.id.textView45);
            storeNameTextView = (TextView) view.findViewById(R.id.textView44);
            storeArriveTextView = (TextView) view.findViewById(R.id.textView43);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    pathStopImageString = stopUri.getPath().toString();
                    Log.d("12octV5", "Path First ==> " + pathStopImageString);
                    try {
                        stopBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(stopUri));
                        if (stopBitmap.getHeight() < stopBitmap.getWidth()) {
                            stopBitmap = rotateBitmap(stopBitmap);
                        }
                        stopImageView.setImageBitmap(stopBitmap);
                        Log.d("TAG", "Height ==> " + stopBitmap.getHeight() + " Width ==> " + stopBitmap.getWidth());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    pathStartImageString = startUri.getPath().toString();
                    Log.d("12octV5", "Path First ==> " + pathStartImageString);
                    try {
                        startBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(startUri));
                        if (startBitmap.getHeight() < startBitmap.getWidth()) {
                            startBitmap = rotateBitmap(startBitmap);
                        }
                        startImageView.setImageBitmap(startBitmap);
                        Log.d("TAG", "Height ==> " + startBitmap.getHeight() + " Width ==> " + startBitmap.getWidth());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private Bitmap rotateBitmap(Bitmap src) {

        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(90);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

    private class SyncTripDetail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d("Tag", "url: " + myConstant.getUrlGetTripDetail());
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("planDtlId", planDtlString)
                        .add("driver_id", loginStrings[0])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(myConstant.getUrlGetTripDetail()).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Log.d("Tag", "doInBackground: " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("Tag", "JSON : " + s);

            //Get Data From Server
            try {
                JSONObject jsonObject = new JSONObject(s);
                workSheetNoString = jsonObject.getString("work_sheet_no");
                runningNoString = jsonObject.getString("runningNo");
                tripNoString = jsonObject.getString("trip_no");
                planDCArriveString = jsonObject.getString("plan_dc_st_arrivalTime");
                planDCDepartString = jsonObject.getString("plan_dc_st_departureTime");
                arriveDCString = jsonObject.getString("dc_en_arrivalDate");
                departDCString = jsonObject.getString("dc_st_arrivalDate");
                mileSTString = jsonObject.getString("mile_st_departure");
                mileENString = jsonObject.getString("mile_en_arrival");
                departPathString = jsonObject.getString("st_departure_FileName");
                arrivePathString = jsonObject.getString("en_arrival_FileName");
                flagStartString = jsonObject.getString("flag_start");
                flagStopString = jsonObject.getString("flag_end");
                storeArrayString = jsonObject.getString("store");

                try {
                    JSONArray store = new JSONArray(storeArrayString);

                    storeCodeStrings = new String[store.length()];
                    storeNameStrings = new String[store.length()];
                    storeNameEnStrings = new String[store.length()];
                    storeArrivalStrings = new String[store.length()];

                    for (int i1 = 0; i1 < store.length(); i1++) {
                        JSONObject object = store.getJSONObject(i1);
                        storeCodeStrings[i1] = object.getString("store_code");
                        storeNameStrings[i1] = object.getString("store_name");
                        storeNameEnStrings[i1] = object.getString("store_nameEng");
                        storeArrivalStrings[i1] = object.getString("plan_arrivalDate");
                    }


                    TripDetailAdapter tripDetailAdapter = new TripDetailAdapter(storeCodeStrings, storeNameStrings, storeArrivalStrings, TripDetailActivity.this);
                    listView.setAdapter(tripDetailAdapter);
                } catch (JSONException e) {
                    Log.d("Tag", "e onpost in: " + e);
                }

                //Set Text
                if (!workSheetNoString.equals("null")) {
                    jobTextView.setText(getResources().getText(R.string.job) + " : " + workSheetNoString);
                }
                if (!arriveDCString.equals("null")) {
                    stopTimeTextView.setText(arriveDCString);
                }
                if (!departDCString.equals("null")) {
                    startTimeTextView.setText(departDCString);
                }

                //Config Show/UnShow
                if (flagStartString.equals("Show")) {
                    startImageView.setVisibility(View.VISIBLE);
                    startButton.setVisibility(View.VISIBLE);
                } else {
                    startImageView.setVisibility(View.GONE);
                    startButton.setVisibility(View.GONE);
                }

                if (flagStopString.equals("Show")) {
                    stopImageView.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                } else {
                    stopImageView.setVisibility(View.GONE);
                    stopButton.setVisibility(View.GONE);
                }

                //Set On Click Listener
                stopImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File originalFile1 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "stop.png");

                        Intent cameraIntent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        stopUri = Uri.fromFile(originalFile1);
                        Log.d("TAG", "Path Stop " + stopUri);
                        cameraIntent1.putExtra(MediaStore.EXTRA_OUTPUT, stopUri);
                        startActivityForResult(cameraIntent1, 1);
                    }
                });

                startImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File originalFile1 = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "start.png");

                        Intent cameraIntent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startUri = Uri.fromFile(originalFile1);
                        Log.d("TAG", "Path Start " + startUri);
                        cameraIntent1.putExtra(MediaStore.EXTRA_OUTPUT, startUri);
                        startActivityForResult(cameraIntent1, 2);
                    }
                });

                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pathStartImageString != null) {
                            if (setLatLong(0)) {
                                SynUploadImage synUploadImage = new SynUploadImage(TripDetailActivity.this, startBitmap, "start.png", latString, longString, "Start");
                                synUploadImage.execute();
                                if (!sendStatus) {
                                    pathStartImageString = null;
                                    Toast.makeText(TripDetailActivity.this, getResources().getText(R.string.save_comp), Toast.LENGTH_LONG).show();


                                }
                            } else {
                                Toast.makeText(TripDetailActivity.this, getResources().getText(R.string.err_gps1), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(TripDetailActivity.this, getResources().getText(R.string.err_conf1), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pathStopImageString != null) {
                            if (setLatLong(0)) {
                                SynUploadImage synUploadImage = new SynUploadImage(TripDetailActivity.this, stopBitmap, "stop.png", latString, longString, "Stop");
                                synUploadImage.execute();
                                if (!sendStatus) {
                                    pathStartImageString = null;
                                    Toast.makeText(TripDetailActivity.this, getResources().getText(R.string.save_comp), Toast.LENGTH_LONG).show();


                                }
                            } else {
                                Toast.makeText(TripDetailActivity.this, getResources().getText(R.string.err_gps1), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(TripDetailActivity.this, getResources().getText(R.string.err_conf1), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (JSONException e) {
                Log.d("Tag", "e onpost out: " + e);
            }
        }
    }

    public boolean setLatLong(int rev) {
        boolean b = false;
        boolean result = false;

        do {
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

            if (strLat.equals("Unknown") && strLng.equals("Unknown") && rev < 10) {
                rev++;
                b = true;

                Log.d("ServiceTag", "Repeat ");
            } else if (strLat.equals("Unknown") && strLng.equals("Unknown") && rev >= 10) {
                //Can't get lat/long
                Log.d("ServiceTag", "Can't get lat/long ");
                rev++;
                b = false;
            } else {
                latString = strLat;
                longString = strLng;
                b = false;
                result = true;
            }
        } while (b);


        return result;

    }

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


    }

    private class SynUploadImage extends AsyncTask<Void, Void, String> {
        private Context context;
        private Bitmap bitmap;
        private UploadImageUtils uploadImageUtils;
        private String mUploadedFileName;
        private String latString, longString, opString;
        ProgressDialog progress;
        Runnable progressRunnable, progressFinishRunnable;
        Handler pdCanceller;

        public SynUploadImage(Context context, Bitmap bitmap, String mUploadedFileName, String latString, String longString, String opString) {
            this.context = context;
            this.bitmap = bitmap;
            this.opString = opString;
            this.mUploadedFileName = mUploadedFileName;
            this.latString = latString;
            this.longString = longString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setMessage(getResources().getString(R.string.waiting));
            progress.show();

            progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progress.cancel();

                }
            };

            progressFinishRunnable = new Runnable() {
                @Override
                public void run() {
                    progress.cancel();
                    finish();
                }
            };
        }

        @Override
        protected String doInBackground(Void... voids) {
            uploadImageUtils = new UploadImageUtils();

            final String result = uploadImageUtils.uploadFile(mUploadedFileName, myConstant.getUrlSaveImage(), bitmap, planDtlString, "DC");
            Log.d("TAG", "Do in back after save:-->" + mUploadedFileName);
            if (result == "NOK") {
                return "NOK1";
            } else {
                Log.d("TAG", "Send:-->" + planDtlString + " " + latString + " " + longString + " " + mUploadedFileName + " " + result + " " + loginStrings[2] + " " + opString);
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Log.d("name", mUploadedFileName);
                    RequestBody requestBody = new FormEncodingBuilder()
                            .add("isAdd", "true")
                            .add("PlanDtl2_ID", planDtlString)
                            .add("Latitude", latString)
                            .add("Longitude", longString)
                            .add("op", opString)
                            .add("File_Name", mUploadedFileName)
                            .add("File_Path", result)
                            .add("drv_username", loginStrings[2])
                            .build();
                    Request.Builder builder = new Request.Builder();
                    Request request = builder.url(myConstant.getUrlSetDCImagePath()).post(requestBody).build();
                    Response response = okHttpClient.newCall(request).execute();

                    return response.body().string();
                } catch (Exception e) {
                    sendStatus = true;
                    return "NOK2";
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("TAG", "JSON_Upload ==> " + s);
            if (s.equals("OK")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getString(R.string.add_img_comp), Toast.LENGTH_SHORT).show();
                    }
                });
                pdCanceller = new Handler();
                pdCanceller.postDelayed(progressFinishRunnable, 3000);

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getString(R.string.add_img_incomp), Toast.LENGTH_SHORT).show();
                    }
                });
                pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);
            }

        }
    }

}
