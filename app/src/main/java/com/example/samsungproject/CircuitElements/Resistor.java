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


public class Resistor extends CircuitElement{
    public Resistor(Context context, int rotation, int resistance, int x, int y, int axisRotX, int axisRotY) {
        super(context, 2, 1, rotation, Dictionary.RESISTOR_TYPE, resistance, x, y, axisRotX, axisRotY);
        init( true);
    }

    public Resistor(Resistor base, int x, int y) {
        super(base, x, y);
        init( false);
    }

    @Override
    public void rotate() {
        int rot = (rotation + 1) % 4;
        if (Constructor.canPlaceRectSmall(axisRotX, axisRotY, rot, id)) {
            delete();
            Constructor.createResistor(context, axisRotX, axisRotY, rot, resistance);
        }
    }


    void init(boolean base){
        if (base){
            bp = Dictionary.RESISTOR_IMAGE;
            bp = Bitmap.createScaledBitmap(bp,
                    2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                    (Dictionary.SQRSIZE + Dictionary.SQRWIDTH), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90 * rotation);
            bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, false);
        }
    }

    public void replace( int newX, int newY){
        CircuitElement baseElement = Dictionary.field[baseY][baseX];
        delete();
        Constructor.createResistor(context, newX, newY, this.rotation, this.resistance);
        CircuitElement element = Dictionary.field[newY][newX];
        Dictionary.field[element.baseY][element.baseX].bp = baseElement.bp;
    }

    @Override
    public void updateConnections() {
        int maxY = Dictionary.field.length;
        int maxX = Dictionary.field[0].length;
        Resistor element = (Resistor) Dictionary.field[baseY][baseX];
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

}
