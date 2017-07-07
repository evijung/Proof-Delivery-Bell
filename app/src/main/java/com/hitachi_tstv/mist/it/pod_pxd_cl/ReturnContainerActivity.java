package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ReturnContainerActivity extends Activity {
    private Button saveButton, clearButton;
    private MyConstant myConstant = new MyConstant();
    private String storeCodeString, storeNameString, planString;
    private String[] imageStrings, containerNameStrings, contIdStrings, dbQuantityStrings, dbContainerNameStrings, dbContIdStrings, loginStrings;
    private TextView storeTextView, quantityTextView;
    private ListView containerListView, dbReturnContListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_container);

        //bind widget
        saveButton = (Button) findViewById(R.id.button6);
//        clearButton = (Button) findViewById(R.id.button5);
        containerListView = (ListView) findViewById(R.id.containerListview);
//        dbReturnContListView = (ListView) findViewById(R.id.dbReturn);
        storeTextView = (TextView) findViewById(R.id.txtStoreCode);

        //Get String Extra
        storeCodeString = getIntent().getStringExtra("StoreCode");
        storeNameString = getIntent().getStringExtra("StoreName");

        storeTextView.setText(getResources().getString(R.string.store_code) + " " + storeCodeString + " " + storeNameString);

        planString = getIntent().getStringExtra("PlanDtl");
        loginStrings = getIntent().getStringArrayExtra("Login");

        Log.d("Tag", planString);

        //Syn data to get Basket
        SynGetBasket synGetBasket = new SynGetBasket(ReturnContainerActivity.this);
        synGetBasket.execute(myConstant.getUrlGetReturnContainerQuantity(),planString);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class SyncUploadQuantity extends AsyncTask<String, Void, String> {
        private Context context;
        private String containerString, quantityString, driverString, planIdString;

        public SyncUploadQuantity(Context context, String containerString, String quantityString, String driverString, String planIdString) {
            this.context = context;
            this.containerString = containerString;
            this.quantityString = quantityString;
            this.driverString = driverString;
            this.planIdString = planIdString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", "SyncUploadQuantity JSON ==> " + s);


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("ContainerId", containerString)
                        .add("Quantity", quantityString)
                        .add("drv_username", driverString)
                        .add("planDtl2_id", planIdString).build();

                Request.Builder builder = new Request.Builder();
                Request request = builder.post(requestBody).url(myConstant.getUrlSaveReturnCont()).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class SynGetBasket extends AsyncTask<String, Void, String> {
        private Context context;
        private ArrayList<ReturnContainerItem> returnContainerItems;

        public SynGetBasket(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag20/03/2017", "JSON ==> " + s);
            returnContainerItems = new ArrayList<ReturnContainerItem>();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ReturnContainerItem returnContainerItem = new ReturnContainerItem();
                    returnContainerItem.setContNameString(jsonObject.getString("container"));
                    returnContainerItem.setIdString(jsonObject.getString("conNo"));
                    String path = myConstant.getLink() + myConstant.getProject() + jsonObject.getString("conFilePath");
                    returnContainerItem.setImageString(path);
                    returnContainerItem.setReturnQtyAnInt(jsonObject.getInt("qty"));
                    returnContainerItems.add(returnContainerItem);
                }

                ReturnContainerAdapter returnContainerAdapter = new ReturnContainerAdapter(context, returnContainerItems);
                containerListView.setAdapter(returnContainerAdapter);
                containerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        Log.d("Tag", String.valueOf(i));


                        final Dialog dialog = new Dialog(ReturnContainerActivity.this);
                        dialog.setTitle(returnContainerItems.get(i).getContNameString());
                        dialog.setContentView(R.layout.dialog_get_return);

                        final String container = returnContainerItems.get(i).getIdString();

                        quantityTextView = (TextView) view.findViewById(R.id.textView24);
                        final EditText quantityEditText = (EditText) dialog.findViewById(R.id.editText4);

                        Button saveButton = (Button) dialog.findViewById(R.id.button15);

                        quantityEditText.setText(quantityTextView.getText().toString());

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("TAG", "Edit Text ==> " + quantityEditText.getText());

                                int qty = 0;
                                try {
                                    qty = Integer.parseInt(quantityEditText.getText().toString());

                                } catch (NumberFormatException e) {
                                    qty = 0;
                                }

                                SyncUploadQuantity syncUploadQuantity = new SyncUploadQuantity(context, container, String.valueOf(qty), loginStrings[1], planString);
                                syncUploadQuantity.execute();

                                quantityTextView.setText(String.valueOf(qty));

                                returnContainerItems.get(i).setReturnQtyAnInt(qty);

                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });

            } catch (JSONException e) {
                Log.d("Tag", "e onPost ==> " + e.toString());
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Log.d("Tag20/03/2017", "Do in back");

                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormEncodingBuilder().add("isAdd", "true").add("planDtl2_id", strings[1]).build();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Tag20/03/2017", "e doInBack ==> " + e.toString());
                return null;
            }

        }//DoInBack
    }
}
