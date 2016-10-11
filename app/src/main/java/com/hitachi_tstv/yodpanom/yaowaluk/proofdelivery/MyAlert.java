package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by tunyaporns on 10/11/2016.
 */

public class MyAlert {
    //Expicit
    private Context context;

    public MyAlert(Context context) {
        this.context = context;
    }

    //Dialog for Error
    public void myErrorDialog(int intIcon,
                              String strMessage,
                              String strTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(intIcon);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
}
