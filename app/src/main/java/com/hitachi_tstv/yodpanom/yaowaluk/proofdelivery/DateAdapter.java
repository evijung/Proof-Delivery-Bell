package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by tunyaporns on 10/12/2016.
 */

public class DateAdapter extends BaseAdapter{
    //Explicit
    private Context context;
    private String[] dateStrings, storeStrings;

    public DateAdapter(Context context,
                       String[] dateStrings,
                       String[] storeStrings) {
        this.context = context;
        this.dateStrings = dateStrings;
        this.storeStrings = storeStrings;
    }


    @Override
    public int getCount() {
        return dateStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.date_listview, viewGroup, false);

        //Bind Widget
        TextView dateTextView = (TextView) view1.findViewById(R.id.textView7);
        TextView storeTextView = (TextView) view1.findViewById(R.id.textView6);

        //Show View
        dateTextView.setText("Date :: " + dateStrings[i]);
        storeTextView.setText("Store " + storeStrings[i] + " place");

        return view1;
    }
}//Main Class
