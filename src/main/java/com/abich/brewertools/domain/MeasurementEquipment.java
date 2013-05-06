package com.abich.brewertools.domain;

public interface MeasurementEquipment {
	Density correctDensity(Density measurement, Density startingValue);
}
