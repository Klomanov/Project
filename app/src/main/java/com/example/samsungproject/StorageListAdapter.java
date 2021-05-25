package com.example.samsungproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungproject.CircuitElements.CircuitElement;

class StorageItem{
    public String name;
    public String field;
    public StorageItem(String name, String field){
        this.name = name;
        this.field = field;
    }
}

public class StorageListAdapter extends ArrayAdapter<StorageItem> {
    StorageActivity activity;

    public StorageListAdapter(Context context, StorageItem[] storageItems, StorageActivity activity) {
        super(context, R.layout.schema_list_item, storageItems);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StorageItem storageItem = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.schema_list_item, null);
        }
        ((Button) convertView.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dictionary.database.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_NAME +  " LIKE \"" + storageItem.name + "\"", null);
                activity.updateListView();
                Toast toast = Toast.makeText(activity.getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        ((TextView) convertView.findViewById(R.id.schema_name)).setText(storageItem.name);
        ((Button) convertView.findViewById(R.id.replace_field)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dictionary.jsonField = storageItem.field;
                Toast toast = Toast.makeText(activity.getApplicationContext(), R.string.loaded, Toast.LENGTH_SHORT);
                toast.show();
                activity.finish();
            }
        });

        return convertView;
    }

}
