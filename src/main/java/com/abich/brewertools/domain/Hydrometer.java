package com.abich.brewertools.domain;


public class Hydrometer extends AbstractMeasurmentEquipment {

	@Override
	public Density correctDensity(Density measurement, Density originalDensity) {
		final double initialBrix = originalDensity.toBrix().getDoubleValue();
		final double currentBrix = measurement.toBrix().getDoubleValue();
		double trueBrix = (97 * initialBrix + 1200 * currentBrix) / 1297;
		trueBrix = round(trueBrix, 3);
		return new Brix(trueBrix).toSpecificGravity();
	}
}
