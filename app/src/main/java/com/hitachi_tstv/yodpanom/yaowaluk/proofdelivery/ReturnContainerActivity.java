package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class ReturnContainerActivity extends AppCompatActivity {
    private Button saveButton, clearButton;
    private String[] imageStrings, containerNameStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_container);

        //bind widget
        saveButton = (Button) findViewById(R.id.button6);
        clearButton = (Button) findViewById(R.id.button5);

        //Syn data to get Basket

    }

    private class SynGetBasket extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
