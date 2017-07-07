package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Tunyaporn on 6/5/2017.
 */

public class JobManagementListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] workSheetNoStrings, routeNoStrings, departDateStrings, storeStrings;
    private JobManagementListViewHolder jobManagementListViewHolder;

    public JobManagementListViewAdapter(Context context,
                                        String[] workSheetNoStrings,
                                        String[] routeNoStrings,
                                        String[] departDateStrings,
                                        String[] storeStrings) {
        this.context = context;
        this.workSheetNoStrings = workSheetNoStrings;
        this.routeNoStrings = routeNoStrings;
        this.departDateStrings = departDateStrings;
        this.storeStrings = storeStrings;
    }

    @Override
    public int getCount() {
        return workSheetNoStrings.length;
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
            view = LayoutInflater.from(context).inflate(R.layout.jm_listview, viewGroup, false);
            jobManagementListViewHolder = new JobManagementListViewHolder(view);
            view.setTag(jobManagementListViewHolder);
        } else {
            jobManagementListViewHolder = (JobManagementListViewHolder) view.getTag();
        }

        //Set data to view
        jobManagementListViewHolder.dateTextView.setText(context.getResources().getText(R.string.depart_date) + " :: " + departDateStrings[i]);
        jobManagementListViewHolder.workSheetTextView.setText(context.getResources().getText(R.string.job) + " :: " + workSheetNoStrings[i]);
        jobManagementListViewHolder.routeTextView.setText(context.getResources().getText(R.string.route_no) + " :: " + routeNoStrings[i]);
        jobManagementListViewHolder.storeTextView.setText(context.getResources().getText(R.string.store) + " :: " + storeStrings[i]);

        return view;
    }

    private class JobManagementListViewHolder {
        private TextView dateTextView, workSheetTextView, routeTextView, storeTextView;

        public JobManagementListViewHolder(View view) {
            dateTextView = (TextView) view.findViewById(R.id.textView34);
            workSheetTextView = (TextView) view.findViewById(R.id.textView32);
            routeTextView = (TextView) view.findViewById(R.id.textView33);
            storeTextView = (TextView) view.findViewById(R.id.textView35);
        }
    }

}
