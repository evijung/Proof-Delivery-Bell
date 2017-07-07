package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by tunyaporn on 6/22/2017.
 */

public class TripDateAdapter extends BaseAdapter {
    private Context context;
    private String [] tripPlanIdStrings, cntTripStrings, tripDateStrings;
    TripDateViewHolder tripDateViewHolder;

    public TripDateAdapter(Context context, String[] tripPlanIdStrings, String[] cntTripStrings, String[] tripDateStrings) {
        this.context = context;
        this.tripPlanIdStrings = tripPlanIdStrings;
        this.cntTripStrings = cntTripStrings;
        this.tripDateStrings = tripDateStrings;
    }

    @Override
    public int getCount() {
        return cntTripStrings.length;
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
            view = LayoutInflater.from(context).inflate(R.layout.trip_date_listview, viewGroup, false);
            tripDateViewHolder = new TripDateViewHolder(view);
            view.setTag(tripDateViewHolder);
        } else {
            tripDateViewHolder = (TripDateViewHolder) view.getTag();
        }

        tripDateViewHolder.tripCntTextView.setText(context.getResources().getText(R.string.trip_qty) + " " + cntTripStrings[i] + " " + context.getResources().getText(R.string.trip));
        tripDateViewHolder.dateTextView.setText(context.getResources().getText(R.string.date) + " " + tripDateStrings[i]);

        return view;
    }

    private class TripDateViewHolder {
        TextView dateTextView, tripCntTextView;

        public TripDateViewHolder(View view) {
            dateTextView = (TextView) view.findViewById(R.id.txtTripDate);
            tripCntTextView = (TextView) view.findViewById(R.id.txtTripQty);
        }
    }
}
