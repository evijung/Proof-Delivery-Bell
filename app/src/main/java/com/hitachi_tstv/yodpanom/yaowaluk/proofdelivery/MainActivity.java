package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


                }


            }//Onclick
        });


    }   //Main method


}   //Main Class