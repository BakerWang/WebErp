package com.artwell.erp.techquote.entity;

import java.util.HashMap;
import java.util.Map;

public enum Unit {
	MIN(1,"分钟"),
	CNY(2,"元"),
	WEIGHT(3,"克/件");
	
    private final int value; 
    private final String displayText; 
    
    //Mapping from value to enum object
	private static final Map<Integer, Unit> sValueEnumMap = new HashMap<Integer, Unit>();
	static {
        for (Unit type : Unit.values()) {
        	sValueEnumMap.put(type.value, type);
        }
    }
    
    public static Unit of(int value) {
    	Unit unit = sValueEnumMap.get(value);
        if (unit == null) {
        	throw new IllegalArgumentException("Invalid value for enum:+"+value);
        }
        return unit;
    }

	public int getValue() { 
		return value;
	}
	
	public String toString(){
	 	return displayText;
	}
	

	private Unit(int value, String displayText) {
        this.value= value;
        this.displayText = displayText;
    }
}
