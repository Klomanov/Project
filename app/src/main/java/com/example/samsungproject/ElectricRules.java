package com.example.samsungproject;

import com.example.samsungproject.CircuitElements.CircuitElement;
import com.example.samsungproject.CircuitElements.CircuitSection;
import com.example.samsungproject.CircuitElements.Power;
import com.example.samsungproject.CircuitElements.Resistor;
import com.example.samsungproject.CircuitElements.Wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ElectricRules {


    public static HashSet<CircuitElement> findClosedCircuit(int startX, int startY, int x, int y, Boolean[][] visited,
                                                            HashSet<CircuitElement> circuit, HashSet<CircuitElement> testCircuit) {
        CircuitElement element;
        CircuitElement end = Dictionary.field[startY][startX];
        if (x == -1 && y == -1) {
            x = startX;
            y = startY;
            element = Dictionary.field[y][x];
            element = Dictionary.field[element.baseY][element.baseX];
        }
        else {
            element = Dictionary.field[y][x];
            element = Dictionary.field[element.baseY][element.baseX];
            if (end.rotation == Dictionary.RIGHT || end.rotation == Dictionary.LEFT){
                if (element.rightConnection != null && element.rightConnection.id == end.id) {
                    circuit.addAll(testCircuit);
                    return circuit;
                }
            }
            else{
                if (element.upConnection != null && element.upConnection.id == end.id){
                    circuit.addAll(testCircuit);
                    return circuit;
                }
            }
        }
        visited[element.baseY][element.baseX] = true;
        testCircuit.add(element);
        if (element.rightConnection != null && element.rightConnection.resistance >= 0 && !visited[element.rightConnection.baseY][element.rightConnection.baseX] &&
        element.rightConnection.leftConnection != null) {
            HashSet<CircuitElement> hashSet = findClosedCircuit(startX, startY, element.rightConnection.x, element.rightConnection.y, visited, circuit, testCircuit);
            if (hashSet != null) circuit.addAll(hashSet);
        }
        if (element.downConnection != null && element.downConnection.resistance >= 0 && !visited[element.downConnection.baseY][element.downConnection.baseX] &&
        element.downConnection.upConnection != null) {
            HashSet<CircuitElement> hashSet = findClosedCircuit(startX, startY, element.downConnection.x, element.downConnection.y, visited, circuit, testCircuit);
            if (hashSet != null) circuit.addAll(hashSet);
        }
        if (element.leftConnection != null && element.leftConnection.resistance >= 0 && !visited[element.leftConnection.baseY][element.leftConnection.baseX] &&
        element.leftConnection.rightConnection != null) {
            HashSet<CircuitElement> hashSet = findClosedCircuit(startX, startY, element.leftConnection.x, element.leftConnection.y, visited, circuit, testCircuit);
            if (hashSet != null) circuit.addAll(hashSet);
        }
        if (element.upConnection != null && element.upConnection.resistance >= 0 && !visited[element.upConnection.baseY][element.upConnection.baseX] &&
        element.upConnection.downConnection != null) {
            HashSet<CircuitElement> hashSet = findClosedCircuit(startX, startY, element.upConnection.x, element.upConnection.y, visited, circuit, testCircuit);
            if (hashSet != null) circuit.addAll(hashSet);
        }
        testCircuit.clear();
        return circuit;
    }
}


//    public static int countResistance(int x, int y){
//        int resist = 0;
//        Power power = (Power) Dictionary.field[y][x];
//        Boolean[][] visited = new Boolean[maxX][maxY];
//        ArrayList<CircuitElement> internalElements = new ArrayList<>();
//        internalElements = findInternalElements(power.x, power.y, visited, -1, -1, internalElements, 0);
//        for (int i = 0; i < maxY; i++){
//            for (int j = 0; j < maxX; j++){
//                visited[i][j] = false;
//            }
//        }
//        if (power.rotation == Dictionary.RIGHT && power.rightConnection != null){
//            spreadPotential(visited, internalElements, power.rightConnection.x, power.rightConnection.y, power.emf);
//        }
//        if (power.rotation == Dictionary.DOWN && power.downConnection != null){
//            spreadPotential(visited, internalElements, power.downConnection.x, power.downConnection.y, power.emf);
//        }
//        if (power.rotation == Dictionary.LEFT && power.leftConnection != null){
//            spreadPotential(visited, internalElements, power.leftConnection.x, power.leftConnection.y, power.emf);
//        }
//        if (power.rotation == Dictionary.UP && power.upConnection != null){
//            spreadPotential(visited, internalElements, power.upConnection.x, power.upConnection.y, power.emf);
//        }
//
//        return resist;
//    }
//
//    public static void makeSections(Boolean[][] visited, CircuitElement currentElement, CircuitSection circuit){
//        int y = currentElement.y;
//        int x = currentElement.x;
//        visited[y][x] = true;
//        if (currentElement.type != Dictionary.T_WIRE_TYPE){
//            if (circuit == null){
//                ArrayList<CircuitSection> innerSections = new ArrayList<>();
//                innerSections.add(new CircuitSection(currentElement));
//                circuit = new CircuitSection(Dictionary.SECTION_TYPE_SERIAL, innerSections);
//            }
//            circuit.addSection(new CircuitSection(currentElement));
//        }
//        else {
//
//        }
//        if (currentElement.rightConnection != null && !visited[y][x + 1]){
//            makeSections(visited, currentElement.rightConnection, circuit);
//        }
//    }
//
//    public static void makeSections(int x, int y){
//
//    }
//
//    public static void spreadPotential(Boolean[][] visited, ArrayList<CircuitElement> internalElements, int x, int y, double potential){
//        visited[y][x] = true;
//        double testFall = 0.00001;
//        CircuitElement element = Dictionary.field[y][x];
//        if (Dictionary.field[y][x].getClass() == Wire.class){
//            Wire wire = (Wire) Dictionary.field[y][x];
//            wire.potential = potential;
//
//        }
//        if (internalElements.contains(element)) {
//            if (element.rightConnection != null && !visited[element.rightConnection.y][element.rightConnection.x] && x < maxX - 1) {
//                spreadPotential(visited, internalElements, x + 1, y, potential - testFall);
//            }
//            if (element.downConnection != null && !visited[element.downConnection.y][element.downConnection.x] && y < maxY - 1){
//                spreadPotential(visited, internalElements, x, y + 1, potential - testFall);
//            }
//            if (element.leftConnection != null && !visited[element.leftConnection.y][element.leftConnection.x] && x > 0){
//                spreadPotential(visited, internalElements, x - 1, y, potential - testFall);
//            }
//            if (element.upConnection != null && !visited[element.upConnection.y][element.upConnection.x] && y > 0){
//                spreadPotential(visited, internalElements, x, y - 1, potential - testFall);
//            }
//        }
//        else{
//            if (element.rightConnection != null && !visited[element.rightConnection.y][element.rightConnection.x] && x < maxX - 1) {
//                spreadPotential(visited, internalElements, x + 1, y, potential);
//            }
//            if (element.downConnection != null && !visited[element.downConnection.y][element.downConnection.x] && y < maxY - 1){
//                spreadPotential(visited, internalElements, x, y + 1, potential);
//            }
//            if (element.leftConnection != null && !visited[element.leftConnection.y][element.leftConnection.x] && x > 0){
//                spreadPotential(visited, internalElements, x - 1, y, potential);
//            }
//            if (element.upConnection != null && !visited[element.upConnection.y][element.upConnection.x] && y > 0){
//                spreadPotential(visited, internalElements, x, y - 1, potential);
//            }
//        }
//
//    }
//
//    public static ArrayList<CircuitElement> findInternalElements( int startX, int startY, Boolean[][] visitField, int x, int y, ArrayList<CircuitElement> internalElements
//    ,int checkpoint) {
//        if (x == -1 && y == -1){
//            x = startX;
//            y = startY;
//        }
//        else{
//            if (startX == x && startY == y){
//                return internalElements;
//            }
//        }
//        CircuitElement element = Dictionary.field[y][x];
//        visitField[y][x] = true;
//        if (element.upConnection != null && element.upConnection.type != Dictionary.NOTHING_TYPE && (!visitField[y - 1][x] || (y - 1 == startY && x == startX)) &&
//                element.upConnection.resistance >= 0){
//            internalElements.add(element.upConnection);
//        }
//        if (element.rightConnection != null && element.rightConnection.type != Dictionary.NOTHING_TYPE && (!visitField[y - 1][x] || (y == startY && x + 1 == startX))&&
//                element.rightConnection.resistance >= 0){
//            internalElements.add(element.rightConnection);
//        }
//        if (element.downConnection != null && element.downConnection.type != Dictionary.NOTHING_TYPE && (!visitField[y + 1][x] || (y + 1 == startY && x == startX)) &&
//                element.downConnection.resistance >= 0){
//            internalElements.add(element.downConnection);
//        }
//        if (element.leftConnection != null && element.leftConnection.type != Dictionary.NOTHING_TYPE && (!visitField[y][x - 1] || (y == startY && x - 1 == startX)) &&
//                element.leftConnection.resistance >= 0){
//            internalElements.add(element.leftConnection);
//        }
//        for (int i = checkpoint; i <= internalElements.size(); i++){
//            internalElements.remove(i);
//        }
//        return null;
//    }



