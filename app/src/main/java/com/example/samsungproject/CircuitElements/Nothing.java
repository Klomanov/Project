package com.example.samsungproject.CircuitElements;

import com.example.samsungproject.Dictionary;

import java.io.Serializable;

public class Nothing extends CircuitElement{

    public Nothing(int x, int y){
        super(null, 1, 1, Dictionary.RIGHT, Dictionary.NOTHING_TYPE, Dictionary.NOTHING_RESISTANCE, x, y, x, y);

    }

    @Override
    public void rotate() {

    }

    @Override
    public void replace(int x, int y) {

    }

    @Override
    public void updateConnections() {

    }
}
