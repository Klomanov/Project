package com.example.samsungproject;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.samsungproject.CircuitElements.CircuitElement;

import java.io.Serializable;

public class Dictionary{

    public static int duringId = 0;

    public static int createType = -1;

    public static final int AMOUNT_ELEMENTS = 7;

    public static SQLiteDatabase database;
    public static CircuitElement[][] field;
    public static String jsonField;

    public static final int UP = 3;
    public static final int DOWN = 1;
    public static final int RIGHT = 0;
    public static final int LEFT = 2;


    public static final int SQRWIDTH = 3;
    public static final int SQRSIZE = 77;


    public static final int NOTHING_TYPE = 0;
    public static final int RESISTOR_TYPE = 1;
    public static final int WIRE_TYPE = 2;
    public static final int R_WIRE_TYPE = 3;
    public static final int T_WIRE_TYPE = 4;
    public static final int POWER_TYPE = 5;
    public static final int KEY_TYPE = 6;
    public static final int BULB_TYPE = 7;


    public static final int SECTION_TYPE_SERIAL = 0;
    public static final int SECTION_TYPE_PARALLEL = 1;
    public static final int SECTION_TYPE_SIMPLE = 2;


    public static Bitmap WIRE_IMAGE;
    public static Bitmap WIRE_SELECTED_IMAGE;
    public static Bitmap R_WIRE_IMAGE;
    public static Bitmap R_WIRE_SELECTED_IMAGE;
    public static Bitmap RESISTOR_IMAGE;
    public static Bitmap RESISTOR_SELECTED_IMAGE;
    public static Bitmap T_WIRE_IMAGE;
    public static Bitmap T_WIRE_SELECTED_IMAGE;
    public static Bitmap POWER_IMAGE;
    public static Bitmap POWER_SELECTED_IMAGE;
    public static Bitmap KEY_OFF_IMAGE;
    public static Bitmap KEY_OFF_SELECTED_IMAGE;
    public static Bitmap KEY_ON_IMAGE;
    public static Bitmap KEY_ON_SELECTED_IMAGE;
    public static Bitmap BULB_OFF_IMAGE;
    public static Bitmap BULB_OFF_SELECTED_IMAGE;
    public static Bitmap BULB_ON_IMAGE;
    public static Bitmap BULB_ON_SELECTED_IMAGE;


    public static final int WIRE_RESISTANCE = 0;
    public static final int NOTHING_RESISTANCE = -1;


    public static void initImages(Resources res){
        WIRE_IMAGE = BitmapFactory.decodeResource(res, R.drawable.wire);
        R_WIRE_IMAGE = BitmapFactory.decodeResource(res, R.drawable.r_wire);
        RESISTOR_IMAGE = BitmapFactory.decodeResource(res, R.drawable.resistor);
        T_WIRE_IMAGE = BitmapFactory.decodeResource(res, R.drawable.t_wire);
        POWER_IMAGE = BitmapFactory.decodeResource(res, R.drawable.power);
        KEY_OFF_IMAGE = BitmapFactory.decodeResource(res, R.drawable.key_off);
        KEY_ON_IMAGE = BitmapFactory.decodeResource(res, R.drawable.key_on);
        BULB_OFF_IMAGE = BitmapFactory.decodeResource(res, R.drawable.bulb_off);
        BULB_ON_IMAGE = BitmapFactory.decodeResource(res, R.drawable.bulb_on);
        POWER_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.selected_power);
        WIRE_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.wire_selected);
        R_WIRE_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.r_wire_selected);
        RESISTOR_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.resistor_selected);
        T_WIRE_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.t_wire_selected);
        KEY_OFF_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.key_off_selected);
        KEY_ON_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.key_on_selected);
        BULB_OFF_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.bulb_off_selected);
        BULB_ON_SELECTED_IMAGE = BitmapFactory.decodeResource(res, R.drawable.bulb_on_selected);
    }
}
