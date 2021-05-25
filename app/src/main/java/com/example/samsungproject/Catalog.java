package com.example.samsungproject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Catalog extends android.widget.ListView {


    public Catalog(Context context) {
        super(context);
    }

    public Catalog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Catalog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(StorageActivity activity){
        Cursor cursor = Dictionary.database.query(DBHelper.TABLE_NAME, null, null, null, null , null, null);

        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        if (cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAME);
            int fieldIndex = cursor.getColumnIndex(DBHelper.COLUMN_FIELD);

            do{
                fields.add(cursor.getString(fieldIndex));
                names.add(cursor.getString(nameIndex));
            }while (cursor.moveToNext());
        }
        cursor.close();

        StorageItem[] storageItems = new StorageItem[fields.size()];

        for(int i = 0; i < fields.size(); i++){
            storageItems[i] = new StorageItem(names.get(i), fields.get(i));
        }

        StorageListAdapter storageListAdapter = new StorageListAdapter(getContext(), storageItems, activity);
        setAdapter(storageListAdapter);
    }

}
