package com.hitachi_tstv.mist.it.pod_pxd_cl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * Created by tunyaporns on 2/14/2017.
 */

public class ReturnContainerAdapter extends BaseAdapter {
    private Context context;
    private String[] containerTypeStrings, imageTextStrings, conIdStrings;
    private ReturnContainerViewHolder returnContainerViewHolder;
    private ArrayList<ReturnContainerItem> returnContainerItems;

    public ReturnContainerAdapter(Context context, ArrayList<ReturnContainerItem> returnContainerItems) {
        this.context = context;
        this.returnContainerItems = returnContainerItems;
    }

    @Override
    public int getCount() {
        return returnContainerItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.return_cont_list_view, viewGroup, false);
            returnContainerViewHolder = new ReturnContainerViewHolder(view);
            view.setTag(returnContainerViewHolder);
        } else {
            returnContainerViewHolder = (ReturnContainerViewHolder) view.getTag();
        }

        //load & show picture
        Glide.with(context).load(returnContainerItems.get(i).getImageString()).into(returnContainerViewHolder.pictureImageView);
        //set text
        returnContainerViewHolder.containerTextView.setText(returnContainerItems.get(i).getContNameString());
        returnContainerViewHolder.qtyTextView.setText(returnContainerItems.get(i).getReturnQtyAnInt());


        return view;
    }

    private class ReturnContainerViewHolder {
        private ImageView pictureImageView;
        private TextView containerTextView, qtyTextView;

        public ReturnContainerViewHolder(View view) {
            pictureImageView = (ImageView) view.findViewById(R.id.imageView7);
            containerTextView = (TextView) view.findViewById(R.id.textView23);
            qtyTextView = (TextView) view.findViewById(R.id.textView24);
        }
    }

}
