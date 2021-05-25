package com.example.samsungproject.CircuitElements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.samsungproject.Constructor;
import com.example.samsungproject.Dictionary;

import java.io.Serializable;

public class Key extends CircuitElement{

    public Key(Context context, int rotation, int x, int y, int axisRotX, int axisRotY, boolean closed) {
        super(context, 2, 1, rotation, Dictionary.KEY_TYPE, -1, x, y, axisRotX, axisRotY);
        this.closed = closed;
        init(true);
    }

    public Key(Key base, int x, int y) {
        super(base, x, y);
        closed = base.closed;
        init(false);
    }

    @Override
    public void rotate() {
        int rot = (rotation + 1) % 4;
        if (Constructor.canPlaceRectSmall(axisRotX, axisRotY, rot, id)) {
            delete();
            Constructor.createKey(context, axisRotX, axisRotY, rot, closed);
        }
    }

    @Override
    public void replace(int newX, int newY) {
        CircuitElement baseElement = Dictionary.field[baseY][baseX];
        delete();
        Constructor.createKey(context, newX, newY, rotation, closed);
        CircuitElement element = Dictionary.field[newY][newX];
        Dictionary.field[element.baseY][element.baseX].bp = baseElement.bp;

    }

    @Override
    public void updateConnections() {
        int maxY = Dictionary.field.length;
        int maxX = Dictionary.field[0].length;
        Key element = (Key) Dictionary.field[baseY][baseX];
        if (rotation == Dictionary.RIGHT || rotation == Dictionary.LEFT){
            if (baseX > 0){
                element.leftConnection = Dictionary.field[baseY][baseX - 1];
            }
            if (baseX < maxX - 1){
                element.rightConnection = Dictionary.field[baseY][baseX + 2];
            }
        }
        if (rotation == Dictionary.DOWN || rotation == Dictionary.UP){
            if (baseY > 0){
                element.upConnection = Dictionary.field[baseY - 1][baseX];
            }
            if (baseY < maxY - 1){
                element.downConnection = Dictionary.field[baseY + 2][baseX];
            }
        }
    }

    void init(boolean base){
        if (base){
            if (closed) {
                bp = Dictionary.KEY_ON_IMAGE;
                resistance = 0;
            }
            else{
                bp = Dictionary.KEY_OFF_IMAGE;
                resistance = -1;
            }
            bp = Bitmap.createScaledBitmap(bp,
                    2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                    (Dictionary.SQRSIZE + Dictionary.SQRWIDTH), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90 * rotation);
            bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, false);
        }
    }

    public void performClick(){
        delete();
        Constructor.createKey(context, axisRotX, axisRotY, rotation, !closed);
    }
}
