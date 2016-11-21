package com.szuni.inventory.model;

public enum Unit {
	LITER("l"), PIECE("db");
	
	private String stringRepresentation;
	
	private Unit(String representation) {
		stringRepresentation = representation;
	}

	public String getStringRepresentation() {
		return stringRepresentation;
	}

}
