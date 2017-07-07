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

public class ContainerListAdapter extends BaseAdapter {
    //Explicit
    private String[] containerStrings, quantityStrings;
    private Context context;
    private ContainerListViewHolder containerListViewHolder;

    public ContainerListAdapter(String[] containerStrings, String[] quantityStrings, Context context) {
        this.containerStrings = containerStrings;
        this.quantityStrings = quantityStrings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return containerStrings.length;
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
            view = LayoutInflater.from(context).inflate(R.layout.container_detail_listview, viewGroup, false);
            containerListViewHolder = new ContainerListViewHolder(view);
            view.setTag(containerListViewHolder);
        } else {
            containerListViewHolder = (ContainerListViewHolder) view.getTag();
        }


        containerListViewHolder.containerTextView.setText(containerStrings[i]);
        containerListViewHolder.quantityTextView.setText(quantityStrings[i]);
        containerListViewHolder.idTextView.setText(String.valueOf(i+1));

        return view;
    }

    private class ContainerListViewHolder {
        private TextView containerTextView, quantityTextView, idTextView;

        public ContainerListViewHolder(View view) {
            containerTextView = (TextView) view.findViewById(R.id.textView20);
            quantityTextView = (TextView) view.findViewById(R.id.textView19);
            idTextView = (TextView) view.findViewById(R.id.textView22);
        }
    }
}
