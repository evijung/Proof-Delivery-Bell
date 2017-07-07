package com.hitachi_tstv.mist.it.pod_pxd_cl;

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
    DateViewHolder dateViewHolder;

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

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.date_listview, viewGroup, false);
            dateViewHolder = new DateViewHolder(view);
            view.setTag(dateViewHolder);
        } else {
            dateViewHolder = (DateViewHolder) view.getTag();
        }

        //Show View
        dateViewHolder.dateTextView.setText(context.getResources().getString(R.string.date) + " :: " + dateStrings[i]);
        dateViewHolder.storeTextView.setText(context.getResources().getString(R.string.store_qty) + " :: " + storeStrings[i] + " " + context.getResources().getString(R.string.place));

        return view;
    }

    private class DateViewHolder {
        private TextView dateTextView, storeTextView;

        public DateViewHolder(View view) {
            dateTextView = (TextView) view.findViewById(R.id.textView7);
            storeTextView = (TextView) view.findViewById(R.id.textView6);
        }
    }


}//Main Class
