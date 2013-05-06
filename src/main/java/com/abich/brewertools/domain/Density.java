package com.abich.brewertools.domain;

public abstract class Density {
	public static Brix getBrix(SpecificGravity specificGravity) {
		final double sg = specificGravity.getDoubleValue();
		double brix = 143.254 * Math.pow(sg, 3) - 648.670 * Math.pow(sg, 2)
				+ 1125.805 * sg - 620.389;
		brix = round(brix, 3);
		return new Brix(brix);
	}

	public static SpecificGravity getSpecificGravity(Brix brix) {
		final double b = brix.getDoubleValue();
		double sg = 0.00000005785037196 * Math.pow(b, 3) + 0.00001261831344
				* Math.pow(b, 2) + 0.003873042366 * b + 0.9999994636;
		sg = round(sg, 3);
		return new SpecificGravity(sg);
	}

	public abstract double getDoubleValue();

	public abstract String getLabel();

	public abstract SpecificGravity toSpecificGravity();

	public abstract Brix toBrix();

	public Density getCorrectedDensity(Density originalDensity,
			MeasurementEquipment equip) {
		return equip.correctDensity(this, originalDensity);
	}

	public Density getCorrectedDensityForRefractometer(Density originalDensity) {
		return getCorrectedDensity(originalDensity, new Refractometer());
	}

	public Density getCorrectedDensityForHydrometer(Density originalDensity) {
		return getCorrectedDensity(originalDensity, new Hydrometer());
	}

	private static double round(double trueBrix, double decimals) {
		final double factor = Math.pow(10, decimals);
		return Math.round(trueBrix * factor) / factor;
	}
}
