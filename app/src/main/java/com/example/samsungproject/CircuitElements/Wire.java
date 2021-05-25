package com.example.samsungproject.CircuitElements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.example.samsungproject.Constructor;
import com.example.samsungproject.Dictionary;
import com.example.samsungproject.R;

import java.io.Serializable;

public class Wire extends CircuitElement{

    public Wire(Context context, int rotation, int x, int y, int wireType) {
        super(context, 1, 1, rotation, wireType, Dictionary.WIRE_RESISTANCE, x, y, x, y);
        if (wireType == Dictionary.WIRE_TYPE)   bp = Dictionary.WIRE_IMAGE;
        if (wireType == Dictionary.R_WIRE_TYPE) bp = Dictionary.R_WIRE_IMAGE;
        if (wireType == Dictionary.T_WIRE_TYPE) bp = Dictionary.T_WIRE_IMAGE;
        bp = Bitmap.createScaledBitmap(bp, sizeX * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                sizeY * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH), false);
        Matrix matrix = new Matrix();
        matrix.postRotate(90 * rotation);
        bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, false);
    }

    @Override
    public void rotate() {
        int rot = (rotation + 1) % 4;
        delete();
        Constructor.createWire(context, axisRotX, axisRotY, rot, type);
    }

    public void replace(int newX, int newY){
        CircuitElement baseElement = Dictionary.field[baseY][baseX];
        delete();
        Constructor.createWire(this.context, newX, newY, this.rotation, type);
        CircuitElement element = Dictionary.field[newY][newX];
        Dictionary.field[element.baseY][element.baseX].bp = baseElement.bp;
    }

    @Override
    public void updateConnections() {
        int maxY = Dictionary.field.length;
        int maxX = Dictionary.field[0].length;
        switch (type){
            case Dictionary.WIRE_TYPE:
                if (rotation == Dictionary.RIGHT || rotation == Dictionary.LEFT) {
                    if (x > 0) {
                        CircuitElement element = Dictionary.field[y][x - 1];
                        leftConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                    if (x < maxX - 1) {
                        CircuitElement element = Dictionary.field[y][x + 1];
                        rightConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                }
                else {
                    if (y > 0) {
                        CircuitElement element = Dictionary.field[y - 1][x];
                        upConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                    if (y < maxY - 1) {
                        CircuitElement element = Dictionary.field[y + 1][x];
                        downConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                }
                break;
            case Dictionary.R_WIRE_TYPE:
                if (rotation == Dictionary.RIGHT){
                    if (x < maxX - 1){
                        CircuitElement element = Dictionary.field[y][x + 1];
                        rightConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                    if (y < maxY - 1){
                        CircuitElement element = Dictionary.field[y + 1][x];
                        downConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                }
                if (rotation == Dictionary.DOWN){
                    if (x > 0){
                        CircuitElement element = Dictionary.field[y][x - 1];
                        leftConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                    if (y < maxY - 1){
                        CircuitElement element = Dictionary.field[y + 1][x];
                        downConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                }
                if (rotation == Dictionary.LEFT){
                    if (x > 0){
                        CircuitElement element = Dictionary.field[y][x - 1];
                        leftConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                    if (y > 0){
                        CircuitElement element = Dictionary.field[y - 1][x];
                        upConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                }
                if (rotation == Dictionary.UP){
                    if (y > 0){
                        CircuitElement element = Dictionary.field[y - 1][x];
                        upConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                    if (x < maxX - 1){
                        CircuitElement element = Dictionary.field[y][x + 1];
                        rightConnection = Dictionary.field[element.baseY][element.baseX];
                    }
                }
                break;
            case Dictionary.T_WIRE_TYPE:
                if (rotation == Dictionary.RIGHT || rotation == Dictionary.DOWN){
                    CircuitElement element1 = Dictionary.field[y][x + 1];
                    rightConnection = Dictionary.field[element1.baseY][element1.baseX];
                    CircuitElement element2 = Dictionary.field[y + 1][x];
                    downConnection = Dictionary.field[element2.baseY][element2.baseX];
                }
                if (rotation == Dictionary.LEFT || rotation == Dictionary.UP){
                    CircuitElement element1 = Dictionary.field[y][x - 1];
                    leftConnection = Dictionary.field[element1.baseY][element1.baseX];
                    CircuitElement element2 = Dictionary.field[y - 1][x];
                    upConnection = Dictionary.field[element2.baseY][element2.baseX];
                }
                if (rotation == Dictionary.RIGHT){
                    CircuitElement element = Dictionary.field[y - 1][x];
                    upConnection = Dictionary.field[element.baseY][element.baseX];
                }
                if (rotation == Dictionary.DOWN){
                    CircuitElement element = Dictionary.field[y][x - 1];
                    leftConnection = Dictionary.field[element.baseY][element.baseX];
                }
                if (rotation == Dictionary.LEFT){
                    CircuitElement element = Dictionary.field[y + 1][x];
                    downConnection = Dictionary.field[element.baseY][element.baseX];
                }
                if (rotation == Dictionary.UP){
                    CircuitElement element = Dictionary.field[y][x + 1];
                    rightConnection = Dictionary.field[element.baseY][element.baseX];
                }
                break;
        }
    }
}
