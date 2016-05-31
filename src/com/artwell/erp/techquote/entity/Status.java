package com.artwell.erp.techquote.entity;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	//未报价(1),未审核(2),已审核(3);
	未报价(1),已报价(2);
    private final int value; 
    
    //Mapping from value to enum object
	private static final Map<Integer, Status> sValueEnumMap = new HashMap<Integer, Status>();
	static {
        for (Status type : Status.values()) {
        	sValueEnumMap.put(type.value, type);
        }
    }
    
    public static Status of(int value) {
        Status status = sValueEnumMap.get(value);
        if (status == null) {
        	throw new IllegalArgumentException("Invalid value for enum:+"+value);
        }
        return status;
    }

	public int getValue() { 
		return value;
	}

	private Status(int value) {
        this.value= value;
    }
}
