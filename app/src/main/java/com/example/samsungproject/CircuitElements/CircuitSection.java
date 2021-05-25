package com.example.samsungproject.CircuitElements;

import com.example.samsungproject.Dictionary;

import java.util.ArrayList;

public class CircuitSection {
    ArrayList<CircuitSection> innerSections;
    int typeSection;
    CircuitElement simpleElement;

    public CircuitSection(int typeSection, ArrayList<CircuitSection> innerSections){
        this.typeSection = typeSection;
        this.innerSections = innerSections;
        this.simpleElement = null;
    }

    public CircuitSection(CircuitElement simpleElement){
        this.simpleElement = simpleElement;
    }


    public int countResistance(){
        int resistance = 0;
        if (typeSection == Dictionary.SECTION_TYPE_SERIAL){
            for (CircuitSection section: innerSections
                 ) {
                resistance += section.countResistance();
            }
            return resistance;
        }if (typeSection == Dictionary.SECTION_TYPE_PARALLEL){
            for(CircuitSection section: innerSections){
                resistance += 1/section.countResistance();
            }
            return 1/resistance;
        }
        return simpleElement.resistance;
    }

    public void addSection(CircuitSection section){
        innerSections.add(section);
    }
}
