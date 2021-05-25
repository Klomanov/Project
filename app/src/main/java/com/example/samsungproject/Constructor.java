package com.example.samsungproject;

import android.content.Context;
import android.util.Log;

import androidx.constraintlayout.solver.state.State;

import com.example.samsungproject.CircuitElements.Bulb;
import com.example.samsungproject.CircuitElements.CircuitElement;
import com.example.samsungproject.CircuitElements.Key;
import com.example.samsungproject.CircuitElements.Nothing;
import com.example.samsungproject.CircuitElements.Power;
import com.example.samsungproject.CircuitElements.Resistor;
import com.example.samsungproject.CircuitElements.Wire;

import java.io.DataInput;
import java.io.PipedOutputStream;

public class Constructor {
    static int maxY = Dictionary.field.length;
    static int maxX = Dictionary.field[0].length;

    public static void createNothing(int x, int y){
        Dictionary.field[y][x] = new Nothing(x, y);
    }

    public static  void createResistor(Context context, int x, int y, int rotation, int resistance) {
        if(canPlaceRectSmall(x, y, rotation)) {
            if (rotation == Dictionary.UP && y >= 1) {
                Resistor base = new Resistor(context, rotation, resistance, x, y - 1, x, y);
                Resistor body = new Resistor(base, x, y);
                Dictionary.field[y][x] = body;
                Dictionary.field[y - 1][x] = base;

            }
            if (rotation == Dictionary.DOWN && y <= maxY - 1) {
                Resistor base = new Resistor(context, rotation, resistance, x, y, x, y);
                Resistor body = new Resistor(base, x, y + 1);
                Dictionary.field[y][x] = base;
                Dictionary.field[y + 1][x] = body;
            }
            if (rotation == Dictionary.RIGHT && x <= maxX - 1) {
                Resistor base = new Resistor(context, rotation, resistance, x, y, x, y);
                Resistor body = new Resistor(base, x + 1, y);
                Dictionary.field[y][x] = base;
                Dictionary.field[y][x + 1] = body;
            }
            if (rotation == Dictionary.LEFT && x >= 1) {
                Resistor base = new Resistor(context, rotation, resistance, x - 1, y, x, y);
                Resistor body = new Resistor(base, x, y);
                Dictionary.field[y][x - 1] = base;
                Dictionary.field[y][x] = body;
            }
        }
    }

    public static void createKey(Context context, int x, int y, int rotation, boolean closed){
        if(canPlaceRectSmall(x, y, rotation)) {
            if (rotation == Dictionary.UP && y >= 1) {
                Key base = new Key(context, rotation, x, y - 1, x, y, closed);
                Key body = new Key(base, x, y);
                Dictionary.field[y][x] = body;
                Dictionary.field[y - 1][x] = base;

            }
            if (rotation == Dictionary.DOWN && y <= maxY - 1) {
                Key base = new Key(context, rotation, x, y, x, y, closed);
                Key body = new Key(base, x, y + 1);
                Dictionary.field[y][x] = base;
                Dictionary.field[y + 1][x] = body;
            }
            if (rotation == Dictionary.RIGHT && x <= maxX - 1) {
                Key base = new Key(context, rotation, x, y, x, y, closed);
                Key body = new Key(base, x + 1, y);
                Dictionary.field[y][x] = base;
                Dictionary.field[y][x + 1] = body;
            }
            if (rotation == Dictionary.LEFT && x >= 1) {
                Key base = new Key(context, rotation, x - 1, y, x, y, closed);
                Key body = new Key(base, x, y);
                Dictionary.field[y][x - 1] = base;
                Dictionary.field[y][x] = body;
            }
        }
    }

    public static void createBulb(Context context, int x, int y, int rotation, boolean lightOn){
        if(canPlaceRectSmall(x, y, rotation)) {
            if (rotation == Dictionary.UP && y >= 1) {
                Bulb base = new Bulb(context, rotation, x, y - 1, x, y, lightOn);
                Bulb body = new Bulb(base, x, y);
                Dictionary.field[y][x] = body;
                Dictionary.field[y - 1][x] = base;

            }
            if (rotation == Dictionary.DOWN && y <= maxY - 1) {
                Bulb base = new Bulb(context, rotation, x, y, x, y, lightOn);
                Bulb body = new Bulb(base, x, y + 1);
                Dictionary.field[y][x] = base;
                Dictionary.field[y + 1][x] = body;
            }
            if (rotation == Dictionary.RIGHT && x <= maxX - 1) {
                Bulb base = new Bulb(context, rotation, x, y, x, y, lightOn);
                Bulb body = new Bulb(base, x + 1, y);
                Dictionary.field[y][x] = base;
                Dictionary.field[y][x + 1] = body;
            }
            if (rotation == Dictionary.LEFT && x >= 1) {
                Bulb base = new Bulb(context, rotation, x - 1, y, x, y, lightOn);
                Bulb body = new Bulb(base, x, y);
                Dictionary.field[y][x - 1] = base;
                Dictionary.field[y][x] = body;
            }
        }
    }

    public static void createWire(Context context, int x, int y, int rotation, int wireType){
        if (canPlaceSqrSmall(x, y)) {
            Wire wire = new Wire(context, rotation, x, y, wireType);
            wire.axisRotX = x;
            wire.axisRotY = y;
            Dictionary.field[y][x] = wire;
        }
    }


    public static void createPower(Context context, int x, int y, int rotation, int resistance, int emf){
        if (canPlaceRectBig(x, y, rotation)) {
            if (rotation == Dictionary.RIGHT && y < maxY - 2 && x < maxX - 1) {
                Power base = new Power(context, rotation, resistance, x, y, x, y, emf);
                Dictionary.field[y][x] = base;
                Dictionary.field[y][x + 1] = new Power(base, x + 1, y);
                Dictionary.field[y + 1][x] = new Power(base, x, y + 1);
                Dictionary.field[y + 1][x + 1] = new Power(base, x + 1, y + 1);
                Dictionary.field[y + 2][x] = new Power(base, x, y + 2);
                Dictionary.field[y + 2][x + 1] = new Power(base, x + 1, y + 2);
            }
            if (rotation == Dictionary.DOWN && x > 1 && y < maxY - 1) {
                Power base = new Power(context, rotation, resistance, x - 2, y, x, y, emf);
                Dictionary.field[y][x - 2] = base;
                Dictionary.field[y][x - 1] = new Power(base, x - 1, y);
                Dictionary.field[y][x] = new Power(base, x, y);
                Dictionary.field[y + 1][x - 2] = new Power(base, x - 2, y + 1);
                Dictionary.field[y + 1][x - 1] = new Power(base, x - 1, y + 1);
                Dictionary.field[y + 1][x] = new Power(base, x, y + 1);
            }
            if (rotation == Dictionary.LEFT && x > 0 && y > 1) {
                Power base = new Power(context, rotation, resistance, x - 1, y - 2, x, y, emf);
                Dictionary.field[y - 2][x - 1] = base;
                Dictionary.field[y - 2][x] = new Power(base, x, y - 2);
                Dictionary.field[y - 1][x - 1] = new Power(base, x - 1, y - 1);
                Dictionary.field[y - 1][x] = new Power(base, x, y - 1);
                Dictionary.field[y][x - 1] = new Power(base, x - 1, y);
                Dictionary.field[y][x] = new Power(base, x, y);
            }
            if (rotation == Dictionary.UP && y > 0 && x < maxX - 2) {
                Power base = new Power(context, rotation, resistance, x, y - 1, x, y, emf);
                Dictionary.field[y - 1][x] = base;
                Dictionary.field[y][x] = new Power(base, x, y);
                Dictionary.field[y - 1][x + 1] = new Power(base, x + 1, y - 1);
                Dictionary.field[y][x + 1] = new Power(base, x + 1, y);
                Dictionary.field[y - 1][x + 2] = new Power(base, x + 2, y - 1);
                Dictionary.field[y][x + 2] = new Power(base, x + 2, y);
            }
        }

    }

    public static boolean canPlaceSqrSmall(int x, int y){
        return Dictionary.field[y][x].type == Dictionary.NOTHING_TYPE;
    }

    public static boolean canPlaceRectSmall(int x, int y, int rotation){
        return canPlaceRectSmall(x, y, rotation, -1);
    }

    public static boolean canPlaceRectSmall(int x, int y, int rotation, int replaceId){
        if (y >= 0 && y < Dictionary.field.length && x > 0 && x < Dictionary.field[0].length &&
                (Dictionary.field[y][x].type != Dictionary.NOTHING_TYPE && Dictionary.field[y][x].id != replaceId)){
            return false;
        }
        if (y > 0 && rotation == Dictionary.UP && (Dictionary.field[y - 1][x].type == Dictionary.NOTHING_TYPE || Dictionary.field[y - 1][x].id == replaceId)) {
            return true;
        }
        if (y < Dictionary.field.length - 1 && rotation == Dictionary.DOWN && (Dictionary.field[y + 1][x].type == Dictionary.NOTHING_TYPE ||
                Dictionary.field[y + 1][x].id == replaceId)) {
            return true;
        }
        if (x > 0 && rotation == Dictionary.LEFT && (Dictionary.field[y][x - 1].type == Dictionary.NOTHING_TYPE ||
                Dictionary.field[y][x - 1].id == replaceId)) {
            return true;
        }
        if (x < Dictionary.field[0].length - 1 && rotation == Dictionary.RIGHT && (Dictionary.field[y][x + 1].type == Dictionary.NOTHING_TYPE ||
                Dictionary.field[y][x + 1].id == replaceId)) {
            return true;
        }
        return false;
    }

    public static boolean canPlaceRectBig(int x, int y, int rotation){
        return canPlaceRectBig(x, y, rotation, -1);
    }

    public static boolean canPlaceRectBig(int x, int y, int rotation, int replaceId){
        if (rotation == Dictionary.RIGHT){
            return x < maxX -1 && y < maxY - 2 && canPlaceRectSmall(x, y, Dictionary.RIGHT, replaceId) && canPlaceRectSmall(x, y + 1, Dictionary.RIGHT, replaceId) &&
                    canPlaceRectSmall(x, y + 2, Dictionary.RIGHT, replaceId);
        }
        if (rotation == Dictionary.DOWN){
            return x > 1 && y < maxY - 1 && canPlaceRectSmall(x - 2, y, Dictionary.DOWN, replaceId) && canPlaceRectSmall(x - 1, y, Dictionary.DOWN, replaceId) &&
                    canPlaceRectSmall(x, y, Dictionary.DOWN, replaceId);
        }
        if (rotation == Dictionary.LEFT){
            return x > 0 && y > 1 && canPlaceRectSmall(x, y - 2, Dictionary.LEFT, replaceId) && canPlaceRectSmall(x, y - 1, Dictionary.LEFT, replaceId) &&
                    canPlaceRectSmall(x, y, Dictionary.LEFT, replaceId);
        }
        return x < maxX - 2 && y > 0 && canPlaceRectSmall(x, y, Dictionary.UP, replaceId) && canPlaceRectSmall(x + 1, y, Dictionary.UP, replaceId) &&
                canPlaceRectSmall(x + 2, y, Dictionary.UP, replaceId);
    }
}
