package com.example.samsungproject.CircuitElements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.samsungproject.Constructor;
import com.example.samsungproject.Dictionary;

import java.io.Serializable;

public class Bulb extends CircuitElement{

    public Bulb(Context context, int rotation, int x, int y, int axisRotX, int axisRotY, boolean lightOn) {
        super(context, 2, 1,  rotation, Dictionary.BULB_TYPE, 0, x, y, axisRotX, axisRotY);
        this.lightOn = lightOn;
        init(true);
    }

    public Bulb(Bulb base, int x, int y) {
        super(base, x, y);
        this.lightOn = base.lightOn;
        init(false);
    }

    void init(boolean base){
        if (base){
            if (lightOn){
                bp = Dictionary.BULB_ON_IMAGE;
            }
            else {
                bp = Dictionary.BULB_OFF_IMAGE;
            }
            bp = Bitmap.createScaledBitmap(bp,
                    sizeX * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                  sizeY *  (Dictionary.SQRSIZE + Dictionary.SQRWIDTH), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90 * rotation);
            bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, false);
        }
    }

    @Override
    public void rotate() {
        int rot = (rotation + 1) % 4;
        if (Constructor.canPlaceRectSmall(axisRotX, axisRotY, rot, id)) {
            delete();
            Constructor.createBulb(context, axisRotX, axisRotY, rot, lightOn);
        }
    }

    @Override
    public void replace(int newX, int newY) {
        CircuitElement baseElement = Dictionary.field[baseY][baseX];
        delete();
        Constructor.createBulb(context, newX, newY, rotation, lightOn);
        CircuitElement element = Dictionary.field[newY][newX];
        Dictionary.field[element.baseY][element.baseX].bp = baseElement.bp;
    }

    @Override
    public void updateConnections() {
        int maxY = Dictionary.field.length;
        int maxX = Dictionary.field[0].length;
        Bulb element = (Bulb) Dictionary.field[baseY][baseX];
        if (rotation == Dictionary.RIGHT || rotation == Dictionary.LEFT){
            if (baseX > 0){
                CircuitElement connection = Dictionary.field[baseY][baseX - 1];
                element.leftConnection = Dictionary.field[connection.baseY][connection.baseX];
            }
            if (baseX < maxX - 1){
                CircuitElement connection = Dictionary.field[baseY][baseX + 2];
                element.rightConnection = Dictionary.field[connection.baseY][connection.baseX];
            }
        }
        if (rotation == Dictionary.DOWN || rotation == Dictionary.UP){
            if (baseY > 0){
                CircuitElement connection = Dictionary.field[baseY - 1][baseX];
                element.upConnection = Dictionary.field[connection.baseY][connection.baseX];
            }
            if (baseY < maxY - 1){
                CircuitElement connection = Dictionary.field[baseY + 2][baseX];
                element.downConnection = Dictionary.field[connection.baseY][connection.baseX];
            }
        }
    }

    public void updateLight(Boolean lightOn){
        Bulb base = (Bulb) Dictionary.field[baseY][baseX];
        base.lightOn = lightOn;
        if (lightOn){
            base.bp = Dictionary.BULB_ON_IMAGE;
            base.bp = Bitmap.createScaledBitmap(base.bp,
                    2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                    (Dictionary.SQRSIZE + Dictionary.SQRWIDTH), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90 * rotation);
            base.bp = Bitmap.createBitmap(base.bp, 0, 0, base.bp.getWidth(), base.bp.getHeight(), matrix, false);
        }
        else{
            base.bp = Dictionary.BULB_OFF_IMAGE;
            base.bp = Bitmap.createScaledBitmap(base.bp,
                    2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                    (Dictionary.SQRSIZE + Dictionary.SQRWIDTH), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90 * rotation);
            base.bp = Bitmap.createBitmap(base.bp, 0, 0, base.bp.getWidth(), base.bp.getHeight(), matrix, false);
        }
    }
}
