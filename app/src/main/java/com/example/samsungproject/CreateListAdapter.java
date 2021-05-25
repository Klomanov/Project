package com.example.samsungproject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


class ListItem{
    public String name;
    public Bitmap image;
    public ListItem(String name, Bitmap image){
        this.name = name;
        this.image = image;
    }
}


public class CreateListAdapter extends ArrayAdapter<ListItem> {
    MainActivity activity;

    public CreateListAdapter(Context context, ListItem[] listItems, MainActivity activity) {
        super(context, R.layout.list_item, listItems);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem listItem = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }

        ((TextView) convertView.findViewById(R.id.name)).setText(listItem.name);
        ((ImageView) convertView.findViewById(R.id.picture)).setImageBitmap(listItem.image);
        ((Button) convertView.findViewById(R.id.createElement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItem.name.equals(getContext().getString(R.string.resistor))){
                    Dictionary.createType = Dictionary.RESISTOR_TYPE;
                }
                if (listItem.name.equals(getContext().getString(R.string.wire))){
                    Dictionary.createType = Dictionary.WIRE_TYPE;
                }
                if (listItem.name.equals(getContext().getString(R.string.rWire))){
                    Dictionary.createType = Dictionary.R_WIRE_TYPE;
                }
                if (listItem.name.equals(getContext().getString(R.string.tWire))){
                    Dictionary.createType = Dictionary.T_WIRE_TYPE;
                }
                if (listItem.name.equals(getContext().getString(R.string.power))){
                    Dictionary.createType = Dictionary.POWER_TYPE;
                }
                if (listItem.name.equals(getContext().getString(R.string.key))){
                    Dictionary.createType = Dictionary.KEY_TYPE;
                }
                if (listItem.name.equals(getContext().getString(R.string.bulb))){
                    Dictionary.createType = Dictionary.BULB_TYPE;
                }
                FloatingActionButton create = activity.findViewById(R.id.create);
                create.setImageResource(R.drawable.close);
                create.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.closeColor)));
                activity.buildMode = true;
                activity.createDialog.dismiss();
            }
        });

        return convertView;
    }
}
