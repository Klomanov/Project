package com.example.samsungproject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.samsungproject.CircuitElements.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class MainView extends View implements Serializable {
    int sqrWidth = Dictionary.SQRWIDTH;
    int sqrSize = Dictionary.SQRSIZE;

    CircuitElement movingElement;

    boolean firstLaunch = true;
    boolean replacing = false;
    boolean releaseAfterScale = false;
    FloatingActionButton rotateButton, deleteButton;
    CircuitElement selectedElement;

    int oldY, oldX;

    double scale = 1;
    ScaleGestureDetector detector;
    double minScale = 0.5;
    double maxScale = 2;

    public MainView(Context context) {
        super(context);
        initTools(context);
    }


    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTools(context);
    }


    public MainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTools(context);
    }

    public void initTools(Context context){
        detector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public void initDraw() {

        Dictionary.field = new CircuitElement[getHeight()* 2 / (sqrWidth + sqrSize)][getWidth()* 2 /
                (sqrWidth + sqrSize)];
        for (int i = 0; i < Dictionary.field.length; i++) {
            for (int j = 0; j < Dictionary.field[0].length; j++) {
                Constructor.createNothing(j, i);
            }
        }


        //CreatorReplacer.createResistor(getContext(), Dictionary.Dictionary.field, 6, 6, Dictionary.DOWN, 50);
        //CreatorReplacer.createResistor(getContext(), Dictionary.field, 8, 6, Dictionary.UP, 50);
//        Constructor.createWire(getContext(), 8, 7, Dictionary.RIGHT, Dictionary.WIRE_TYPE);
//        Constructor.createPower(getContext(), 6, 6, Dictionary.RIGHT, 5, 2);
        //Constructor.createPower(getContext(), 10, 10, Dictionary.RIGHT, 5);
        //Dictionary.field[10][10].replace(10, 9);
        //CreatorReplacer.createWire(getContext(), Dictionary.field, 10, 10 , Dictionary.RIGHT);
        //CreatorReplacer.createWire(getContext(), Dictionary.field, 12, 12, Dictionary.UP);
        //CreatorReplacer.createResistor(getContext(), Dictionary.field, 6, 8, Dictionary.LEFT);
        //CreatorReplacer.createResistor(getContext(), Dictionary.field, 8, 8, Dictionary.RIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale((float) scale, (float) scale);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        //makeCalculations();
        for (int y = 0; y < getHeight() * 2; y += (sqrWidth + sqrSize)) {
            for (int x = 0; x < getWidth() * 2; x += (sqrWidth + sqrSize)) {
                canvas.drawRect(x, y, x + sqrSize, y + sqrSize, paint);
            }
        }
        if (firstLaunch) {
            initDraw();
            firstLaunch = false;
        }
        if (Dictionary.field != null) {

            MainView view = findViewById(R.id.mainView7);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            int height = (int) (scale * Dictionary.field.length * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH));
            int width = (int) (scale * Dictionary.field[0].length * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH));
            if (!(lp.width == width && lp.height == height)) {
                lp.height = height;
                lp.width = width;
                view.setLayoutParams(lp);
            }


            for (int y = 0; y < Dictionary.field.length; y++) {
                for (int x = 0; x < Dictionary.field[0].length; x++) {
                    CircuitElement object = Dictionary.field[y][x];
                    if (object.type != Dictionary.NOTHING_TYPE) {
                        //System.out.println(x + " " + y + " " + object.type + " " + object.id);
                    }
                    if (object.bp != null) {
                        canvas.drawBitmap(object.bp, x * (sqrSize + sqrWidth),
                                y * (sqrSize + sqrWidth), paint);
                    }

                }
            }
        }
        canvas.restore();
    }

    public void makeCalculations(){
        Boolean[][] visited = new Boolean[Dictionary.field.length][Dictionary.field[0].length];
        for (int i = 0; i < visited.length; i++){
            for (int j = 0; j < visited[0].length; j++){
                visited[i][j] = false;
                if (Dictionary.field[i][j].type != Dictionary.NOTHING_TYPE) {
                    Dictionary.field[i][j].updateConnections();
                }
            }
        }
        HashSet<CircuitElement> circuit = new HashSet<>();
        for (int i = 0; i < visited.length; i++){
            for (int j = 0; j < visited[0].length; j++){
                CircuitElement element = Dictionary.field[i][j];
                if (element.type == Dictionary.POWER_TYPE && i == element.axisRotY &&
                        j == element.axisRotX){
                    circuit.addAll(ElectricRules.findClosedCircuit(j, i, -1, -1, visited, new HashSet<CircuitElement>(),
                            new HashSet<CircuitElement>()));
                }
            }
        }
        for (int i = 0; i < visited.length; i++){
            for (int j = 0; j < visited[0].length; j++){
                if (Dictionary.field[i][j].type == Dictionary.BULB_TYPE) {
                    Bulb bulb = (Bulb) Dictionary.field[i][j];
                    if (bulb.bp != null) {
                        if (circuit.contains(bulb)) {
                            if (!bulb.lightOn) {
                                bulb.updateLight(true);
                            }
                        } else {
                            if (bulb.lightOn) {
                                bulb.updateLight(false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMask = event.getActionMasked();
        int pointerCount = event.getPointerCount();
        int x = (int) event.getX() / (int) (sqrWidth*scale + sqrSize*scale);
        int y = (int) event.getY() / (int) (sqrWidth*scale + sqrSize*scale);
        detector.onTouchEvent(event);
        mainSwitch: switch (actionMask) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointerCount >= 2){
                    releaseAfterScale = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;

            case MotionEvent.ACTION_DOWN:
                deleteSelectedStatus();
                int type = Dictionary.field[y][x].type;
                if (Dictionary.createType != -1){
                    switch (Dictionary.createType) {
                        case Dictionary.RESISTOR_TYPE:
                            Constructor.createResistor(getContext(), x, y, Dictionary.RIGHT,
                                    50);
                            break;
                        case Dictionary.KEY_TYPE:
                            Constructor.createKey(getContext(), x, y, Dictionary.RIGHT, false);
                            break;
                        case Dictionary.BULB_TYPE:
                            Constructor.createBulb(getContext(), x, y, Dictionary.RIGHT, false);
                            break;
                        case Dictionary.WIRE_TYPE:
                        case Dictionary.R_WIRE_TYPE:
                        case Dictionary.T_WIRE_TYPE:
                            Constructor.createWire(getContext(), x, y, Dictionary.RIGHT, Dictionary.createType);
                            break;
                        case Dictionary.POWER_TYPE:
                            Constructor.createPower(getContext(), x, y, Dictionary.RIGHT, 0, 12);
                            break;
                    }
                }
                if (type != Dictionary.NOTHING_TYPE) {
                    replacing = true;
                    oldX = x;
                    oldY = y;
                    movingElement = Dictionary.field[y][x];
                }


                break;

            case MotionEvent.ACTION_UP:
                deleteSelectedStatus();
                if (releaseAfterScale){
                    releaseAfterScale = false;
                }
                else {
                    if (rotateButton != null && deleteButton != null) {
                        removeButtons();
                        deleteSelectedStatus();
                    }
                }
                if (Dictionary.field[y][x].type != Dictionary.NOTHING_TYPE && rotateButton == null &&
                deleteButton == null) {
                    deleteSelectedStatus();
                    createButtons(x, y);
                }
                replacing = false;
                if (oldX == x && oldY == y && Dictionary.field[y][x].type == Dictionary.KEY_TYPE){
                    Key key = (Key) Dictionary.field[y][x];
                    key.performClick();
                }
                movingElement = null;
                break;

            case MotionEvent.ACTION_MOVE:
                if (replacing){
                    if (Dictionary.field[y][x].type != Dictionary.NOTHING_TYPE && rotateButton == null &&
                            deleteButton == null) {
                        deleteSelectedStatus();
                        createButtons(x, y);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                    switch (movingElement.type){
                        case Dictionary.BULB_TYPE:
                        case Dictionary.KEY_TYPE:
                        case Dictionary.RESISTOR_TYPE:
                            if (!Constructor.canPlaceRectSmall(x, y, movingElement.rotation, movingElement.id))break mainSwitch;
                            break;
                        case Dictionary.WIRE_TYPE:
                        case Dictionary.R_WIRE_TYPE:
                        case Dictionary.T_WIRE_TYPE:
                            if (!Constructor.canPlaceSqrSmall(x, y)) break mainSwitch;
                            break;
                        case Dictionary.POWER_TYPE:
                            if (!Constructor.canPlaceRectBig(x, y, movingElement.rotation, movingElement.id)) break mainSwitch;
                            break;
                    }
                    removeButtons();
                     movingElement.replace(x, y);
                    if (selectedElement != null && selectedElement == movingElement) {
                        selectedElement = Dictionary.field[Dictionary.field[y][x].baseY][Dictionary.field[y][x].baseX];
                    }
                    movingElement = Dictionary.field[y][x];
                }
        }
        makeCalculations();
        invalidate();

        return true;
    }

    public void createButtons(int x, int y){
        final CircuitElement element = Dictionary.field[Dictionary.field[y][x].baseY][Dictionary.field[y][x].baseX];

        selectedElement = element;

        int marginXRotate = (int) ((element.x + element.bp.getWidth()/(Dictionary.SQRWIDTH + Dictionary.SQRSIZE)) *
                (Dictionary.SQRSIZE + Dictionary.SQRWIDTH) * scale);
        int marginXDelete = (int) ((element.x - 1) * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH) * scale);
        int marginYDelete = (int) ((element.y + element.bp.getHeight()/(Dictionary.SQRWIDTH + Dictionary.SQRSIZE))
                * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH) * scale);
        int marginYRotate = (int) ((element.y - 1) * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH) * scale);

        switch (element.type){

            case Dictionary.POWER_TYPE:
                element.bp = Dictionary.POWER_SELECTED_IMAGE;
                break;
            case Dictionary.WIRE_TYPE:
                element.bp = Dictionary.WIRE_SELECTED_IMAGE;
                break;
            case Dictionary.R_WIRE_TYPE:
                element.bp = Dictionary.R_WIRE_SELECTED_IMAGE;
                break;
            case Dictionary.T_WIRE_TYPE:
                element.bp = Dictionary.T_WIRE_SELECTED_IMAGE;
                break;
            case Dictionary.RESISTOR_TYPE:
                element.bp = Dictionary.RESISTOR_SELECTED_IMAGE;
                break;
            case Dictionary.BULB_TYPE:
                if (!element.lightOn){
                    element.bp = Dictionary.BULB_OFF_SELECTED_IMAGE;
                }
                else {
                    element.bp = Dictionary.BULB_ON_SELECTED_IMAGE;
                }
                break;
            case Dictionary.KEY_TYPE:
                Key key = (Key) element;
                if (key.closed){
                    element.bp = Dictionary.KEY_ON_SELECTED_IMAGE;
                }
                else{
                    element.bp = Dictionary.KEY_OFF_SELECTED_IMAGE;
                }

        }
        Matrix matrix = new Matrix();
        element.bp = Bitmap.createScaledBitmap(element.bp, element.sizeX* (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                element.sizeY * (Dictionary.SQRWIDTH + Dictionary.SQRSIZE), false);
        matrix.postRotate(90 * element.rotation);
        element.bp = Bitmap.createBitmap(element.bp, 0, 0, element.bp.getWidth(), element.bp.getHeight(), matrix, false);


        rotateButton = new FloatingActionButton(getContext());
        rotateButton.setImageResource(R.drawable.rotate);
        rotateButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rotateColor)));
        rotateButton.setCustomSize((int) ((Dictionary.SQRSIZE + Dictionary.SQRWIDTH) * 1.2 * scale));
        rotateButton.setX(marginXRotate);
        rotateButton.setY(marginYRotate);

        deleteButton = new FloatingActionButton(getContext());
        deleteButton.setImageResource(R.drawable.delete);
        deleteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.deleteColor)));
        deleteButton.setCustomSize((int) ((Dictionary.SQRSIZE + Dictionary.SQRWIDTH) * 1.2 * scale));
        deleteButton.setX(marginXDelete);
        deleteButton.setY(marginYDelete);


        rotateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dictionary.field[y][x].rotate();
                makeCalculations();
                removeButtons();
                createButtons(element.axisRotX, element.axisRotY);
                invalidate();
            }
        });


        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dictionary.field[y][x].delete();
                makeCalculations();
                removeButtons();
                invalidate();
            }
        });


        RelativeLayout relativeLayout = (RelativeLayout) getParent();
        relativeLayout.addView(rotateButton);
        relativeLayout.addView(deleteButton);
        invalidate();
    }

    public void deleteSelectedStatus(){
        if (selectedElement != null) {
            switch (selectedElement.type) {
                case Dictionary.POWER_TYPE:
                    selectedElement.bp = Dictionary.POWER_IMAGE;
                    break;
                case Dictionary.WIRE_TYPE:
                    selectedElement.bp = Dictionary.WIRE_IMAGE;
                    break;
                case Dictionary.R_WIRE_TYPE:
                    selectedElement.bp = Dictionary.R_WIRE_IMAGE;
                    break;
                case Dictionary.T_WIRE_TYPE:
                    selectedElement.bp = Dictionary.T_WIRE_IMAGE;
                    break;
                case Dictionary.RESISTOR_TYPE:
                    selectedElement.bp = Dictionary.RESISTOR_IMAGE;
                    break;
                case Dictionary.BULB_TYPE:
                    if (selectedElement.lightOn){
                        selectedElement.bp = Dictionary.BULB_ON_IMAGE;
                    }
                    else{
                        selectedElement.bp = Dictionary.BULB_OFF_IMAGE;
                    }
                    break;
                case Dictionary.KEY_TYPE:
                    Key key = (Key) selectedElement;
                    if (key.closed) {
                        selectedElement.bp = Dictionary.KEY_ON_IMAGE;
                    } else {
                        selectedElement.bp = Dictionary.KEY_OFF_IMAGE;
                    }
            }
            Matrix matrix = new Matrix();
            selectedElement.bp = Bitmap.createScaledBitmap(selectedElement.bp, selectedElement.sizeX * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                    selectedElement.sizeY * (Dictionary.SQRWIDTH + Dictionary.SQRSIZE), false);
            matrix.postRotate(90 * selectedElement.rotation);
            selectedElement.bp = Bitmap.createBitmap(selectedElement.bp, 0, 0, selectedElement.bp.getWidth(), selectedElement.bp.getHeight(), matrix, false);
            selectedElement = null;
        }
    }

    public void removeButtons(){
        RelativeLayout relativeLayout = (RelativeLayout) getParent();
        if (rotateButton != null && deleteButton != null){
            relativeLayout.removeView(rotateButton);
            relativeLayout.removeView(deleteButton);
            rotateButton = null;
            deleteButton = null;
        }
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(minScale, Math.min(scale, maxScale));
            removeButtons();
            if (selectedElement != null) {
                createButtons(selectedElement.x, selectedElement.y);
            }
            return true;
        }
    }

    public static CircuitElement[][] deserializeField(String jsonObj){
        return new Gson().fromJson(jsonObj, CircuitElement[][].class);
    }

    public void updateField(String jsonObj){
        removeButtons();
        Dictionary.field = deserializeField(jsonObj);
        for (int y = 0; y < Dictionary.field.length; y++){
            for (int x = 0; x < Dictionary.field[0].length; x++){
                CircuitElement element = Dictionary.field[y][x];
                if (element.type != Dictionary.NOTHING_TYPE && element.axisRotX == x && element.axisRotY == y){
                    element.delete();
                    switch (element.type){
                        case Dictionary.BULB_TYPE:
                            Constructor.createBulb(getContext(), x, y, element.rotation, element.lightOn);
                            break;
                        case Dictionary.KEY_TYPE:
                            Constructor.createKey(getContext(), x, y, element.rotation, element.closed);
                            break;
                        case Dictionary.POWER_TYPE:
                            Constructor.createPower(getContext(), x, y, element.rotation, element.resistance, element.emf);
                            break;
                        case Dictionary.RESISTOR_TYPE:
                            Constructor.createResistor(getContext(), x, y, element.rotation, element.resistance);
                            break;
                        case Dictionary.WIRE_TYPE:
                        case Dictionary.R_WIRE_TYPE:
                        case Dictionary.T_WIRE_TYPE:
                            Constructor.createWire(getContext(), x, y, element.rotation, element.type);
                            break;
                    }
                }
            }
        }
    }
}


