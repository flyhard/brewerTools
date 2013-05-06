package com.abich.brewertools.domain;

public abstract class AbstractMeasurmentEquipment implements MeasurementEquipment{

	protected static double round(double trueBrix, double decimals) {
		double factor = Math.pow(10, decimals);
		return (double) Math.round(trueBrix * factor) / factor;
	}


}