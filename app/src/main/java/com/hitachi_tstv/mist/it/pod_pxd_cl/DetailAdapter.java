package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by masterUNG on 10/12/2016 AD.
 */

public class DetailAdapter extends BaseAdapter {

    //Explicit
    private Context context;
    private String[] workSheetStrings, storeNameStrings,
            planArrivalTimeStrings;
    private DetailViewHolder detailViewHolder;

    public DetailAdapter(Context context,
                         String[] workSheetStrings,
                         String[] storeNameStrings,
                         String[] planArrivalTimeStrings) {
        this.context = context;
        this.workSheetStrings = workSheetStrings;
        this.storeNameStrings = storeNameStrings;
        this.planArrivalTimeStrings = planArrivalTimeStrings;
    }

    @Override
    public int getCount() {
        return workSheetStrings.length;
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
            view = LayoutInflater.from(context).inflate(R.layout.detail_listview, viewGroup, false);
            detailViewHolder = new DetailViewHolder(view);
            view.setTag(detailViewHolder);
        } else {
            detailViewHolder = (DetailViewHolder) view.getTag();
        }

        //Set Text
        detailViewHolder.workSheetTextView.setText(workSheetStrings[i]);
        detailViewHolder.storeNameTextView.setText(storeNameStrings[i]);
        detailViewHolder.planArrivalTextView.setText(planArrivalTimeStrings[i]);

        return view;
    }

    private class DetailViewHolder {
        private TextView workSheetTextView, storeNameTextView, planArrivalTextView;

        public DetailViewHolder(View view) {
            workSheetTextView = (TextView) view.findViewById(R.id.textView11);
            storeNameTextView = (TextView) view.findViewById(R.id.textView12);
            planArrivalTextView = (TextView) view.findViewById(R.id.textView13);
        }
    }

}   // Main Class