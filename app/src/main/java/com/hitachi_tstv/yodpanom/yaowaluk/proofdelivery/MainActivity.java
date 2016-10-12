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
import android.widget.EditText;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Internal;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit
    public EditText userEditText, passwordEditText;
    private Button button;
    private String userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);


        // Button controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Value From Edit text
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check space
                if (userString.equals("")||passwordString.equals("")) {
                    //Have Space
                    MyAlert myAlert = new MyAlert(MainActivity.this);
                    myAlert.myErrorDialog(new MyConstant().getIconAnInt(),
                            new MyConstant().getTitleHaveSpaceString(),
                            new MyConstant().getMessageHaveeSpaceString());

                } else {
                    // No Space
                    SynUser synUser = new SynUser(MainActivity.this);
                    MyConstant  myConstant = new MyConstant();
                    synUser.execute(myConstant.getUrlUserString());


                }


            }//Onclick
        });


    }   //Main method

    //Create Inner Class
    private class SynUser extends AsyncTask<String, Void, String> {
        //Explicit

        private Context context;
        private boolean aBoolean = true;//User false
        private String[] logingStrings = new String[2]; //for User success login
        private String[] columLoginStrings;
        private String truePasswordString;
        private ProgressDialog progressDialog;

        public SynUser(Context context) {
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
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).build();
                Response response = okHttpClient.newCall(request).execute();


                return response.body().string();
            } catch (Exception e) {
                Log.d("11octV1", "e doInback-->" + e.toString());
                return null;
            }

        }   // doInback;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.d("11octV1", "JSON---->" + s);
            MyConstant myConstant = new MyConstant();
            columLoginStrings = myConstant.getColumLogin();

            // using JSON Array
            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (userString.equals(jsonObject.getString("drv_username"))) {
                        aBoolean = !aBoolean;
                        for (int i1 = 0; i1 < logingStrings.length; i1++) {
                            logingStrings[i1] = jsonObject.getString(columLoginStrings[i1]);
                            Log.d("11octV2", "LoginString(" + i1 + ")=" + logingStrings[i1]);
                            truePasswordString = jsonObject.getString("drv_password");

                        }//end for i1

                    } //end if

                }   // end for  i

                if (aBoolean) {
                    MyAlert myAlert = new MyAlert(context);
                    myAlert.myErrorDialog(myConstant.getIconAnInt(),
                            myConstant.getTitleUserFalesString(),
                            myConstant.getMessageUserFalesString());

                }else if (!passwordString.equals(truePasswordString)) {
                    //password false
                    MyAlert myAlert = new MyAlert(context);
                    myAlert.myErrorDialog(myConstant.getIconAnInt(),
                            myConstant.getTitlePasswordFalse(),
                            myConstant.getMessagePasswordFalse());
                } else {
                    //password true
                    Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                    intent.putExtra("Login", logingStrings);
                    intent.putExtra("Date", "");
                    intent.putExtra("PlanId", "");
                    startActivity(intent);
                    finish();


                }

            } catch (Exception e) {
                Log.d("11octV1", "e onPost-->" + e.toString());
            }



        }   //onPost

    }   //SynUser Class




}   //Main Class