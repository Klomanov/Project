package com.example.samsungproject.CircuitElements;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.samsungproject.Constructor;
import com.example.samsungproject.Dictionary;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Random;

import kotlin.random.RandomKt;

public class CircuitElement{
    public int sizeX;
    public int sizeY;
    transient public Bitmap bp;
    public int rotation;
    public int id;
    public int type;
    public int resistance;
    public int baseX;
    public int baseY;
    public int axisRotX, axisRotY;
    public int emf;
    public boolean closed;
    public boolean lightOn;
    transient public CircuitElement upConnection = null;
    transient public CircuitElement rightConnection = null;
    transient public CircuitElement downConnection = null;
    transient public CircuitElement leftConnection = null;
    public int x, y;
    transient public Context context;
    public CircuitElement(){

    }

    public CircuitElement(Context context, int sizeX, int sizeY, int rotation, int type, int resistance, int x, int y, int axisRotX, int axisRotY){
        this.context = context;
        this.id = Dictionary.duringId++;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.rotation = rotation;
        this.type = type;
        this.resistance = resistance;
        this.baseX= x;
        this.baseY = y;
        this.x = x;
        this.y = y;
        this.axisRotX = axisRotX;
        this.axisRotY = axisRotY;
    }
    public CircuitElement(CircuitElement base, int x, int y){
        this.context = base.context;
        this.id = base.id;
        this.sizeX = base.sizeX;
        this.sizeY = base.sizeY;
        this.rotation = base.rotation;
        this.type = base.type;
        this.resistance = base.resistance;
        this.baseX = base.baseX;
        this.baseY = base.baseY;
        this.axisRotX = base.axisRotX;
        this.axisRotY = base.axisRotY;
        this.upConnection = base.upConnection;
        this.downConnection = base.downConnection;
        this.rightConnection = base.rightConnection;
        this.leftConnection = base.leftConnection;
        this.x = x;
        this.y = y;
    }

    public void delete(){
        Constructor.createNothing(x, y);
    //    System.out.println(4);
        if (y > 0 && Dictionary.field[y - 1][x].id == id) {Dictionary.field[y - 1][x].delete(); }
        if (y < Dictionary.field.length - 1 && Dictionary.field[y + 1][x].id == id) {Dictionary.field[y + 1][x].delete();}
        if (x < Dictionary.field[0].length - 1 && Dictionary.field[y][x + 1].id == id) {Dictionary.field[y][x + 1].delete();}
        if (x > 0 && Dictionary.field[y][x - 1].id == id) {Dictionary.field[y][x - 1].delete(); }
//        if (y > 0 && x > 0 && Dictionary.field[y - 1][x - 1].id == id) {Dictionary.field[y - 1][x - 1].delete();}
//        if (y > 0 && x < Dictionary.field[0].length && Dictionary.field[y - 1][x + 1].id == id) {Dictionary.field[y - 1][x + 1].delete();}
//        if (y < Dictionary.field[0].length && x > 0 && Dictionary.field[y + 1][x - 1].id == id) {Dictionary.field[y + 1][x - 1].delete();}
//        if (y < Dictionary.field[0].length && x < Dictionary.field[0].length && Dictionary.field[y + 1][x + 1].id == id) {Dictionary.field[y + 1][x + 1].delete();}
    }

    public void replace(int x, int y) { }

    public void updateConnections() { }

    public void rotate() {}
}
