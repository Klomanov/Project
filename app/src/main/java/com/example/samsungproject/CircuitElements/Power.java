package com.example.samsungproject.CircuitElements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.samsungproject.Constructor;
import com.example.samsungproject.Dictionary;

import java.io.Serializable;

public class Power extends CircuitElement{

    public Power(Context context, int rotation, int resistance, int x, int y, int axisRotX, int axisRotY, int emf) {
        super(context, 2, 3, rotation, Dictionary.POWER_TYPE, resistance, x, y, axisRotX, axisRotY);
        init(true);
        this.emf = emf;
    }

    public Power(Power base, int x, int y) {
        super(base, x, y);
        init(false);
        this.emf = base.emf;
    }

    @Override
    public void rotate() {
        int rot = (rotation + 1) % 4;
        if (Constructor.canPlaceRectBig(axisRotX, axisRotY, rot, id)) {
            delete();
            Constructor.createPower(context, axisRotX, axisRotY, rot, resistance, emf);
        }
    }

    @Override
    public void replace(int newX, int newY) {
        CircuitElement baseElement = Dictionary.field[baseY][baseX];
        delete();
        Constructor.createPower(context, newX, newY, rotation, resistance, emf);
        CircuitElement element = Dictionary.field[newY][newX];
        Dictionary.field[element.baseY][element.baseX].bp = baseElement.bp;
    }

    @Override
    public void updateConnections() {
        int maxX = Dictionary.field[0].length;
        int maxY = Dictionary.field.length;
        Power base = (Power) Dictionary.field[baseY][baseX];
        if (rotation == Dictionary.RIGHT) {
            if (baseY < maxY - 1 && baseX < maxX - 2) {
                CircuitElement connection1 = Dictionary.field[baseY + 1][baseX + 2];
                base.rightConnection = Dictionary.field[connection1.baseY][connection1.baseX];
            }
            if(baseY < maxY - 1 && baseX > 0) {
                CircuitElement connection2 = Dictionary.field[baseY + 1][baseX - 1];
                base.leftConnection = Dictionary.field[connection2.baseY][connection2.baseX];
            }
        }
        if (rotation == Dictionary.DOWN && x > 1 && y < maxY - 1) {
            if (baseX > 0 && baseY > 0) {
                CircuitElement connection1 = Dictionary.field[baseY - 1][baseX - 1];
                base.upConnection = Dictionary.field[connection1.baseY][connection1.baseX];
            }
            if (baseY < maxY - 2 && x > 0) {
                CircuitElement connection2 = Dictionary.field[baseY + 2][baseX - 1];
                base.downConnection = Dictionary.field[connection2.baseY][connection2.baseX];
            }
        }
        if (rotation == Dictionary.LEFT && x > 0 && y > 1) {
            if (baseY > 0 && baseX > 1) {
                CircuitElement connection1 = Dictionary.field[baseY - 1][baseX - 2];
                base.leftConnection = Dictionary.field[connection1.baseY][connection1.baseX];
            }
            if (baseX < maxX - 1 && baseY > 0) {
                CircuitElement connection2 = Dictionary.field[baseY - 1][baseX + 1];
                base.rightConnection = Dictionary.field[connection2.baseY][connection2.baseX];
            }
        }
        if (rotation == Dictionary.UP && y > 0 && x < maxX - 2) {
            if (baseX < maxX - 1 && baseY > 1) {
                CircuitElement connection1 = Dictionary.field[baseY - 2][baseX + 1];
                base.upConnection = Dictionary.field[connection1.baseY][connection1.baseX];
            }
            if (baseY < maxY - 1 && baseX < maxX - 1) {
                CircuitElement connection2 = Dictionary.field[baseY + 1][baseX + 1];
                base.downConnection = Dictionary.field[connection2.baseY][connection2.baseX];
            }
        }
    }

    void init(boolean base){
        if (base){
            bp = Dictionary.POWER_IMAGE;
            bp = Bitmap.createScaledBitmap(bp, 2* (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                    3 * (Dictionary.SQRWIDTH + Dictionary.SQRSIZE), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(90 * rotation);
            bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, false);
        }

    }

}
