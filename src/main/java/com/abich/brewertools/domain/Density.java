package com.abich.brewertools.domain;

import android.util.Log;

public abstract class Density {
	public static Brix getBrix(SpecificGravity specificGravity) {
		double sg = specificGravity.getDoubleValue();
		double brix = 143.254 * Math.pow(sg, 3) - 648.670 * Math.pow(sg, 2)
				+ 1125.805 * sg - 620.389;
		brix=round(brix,3);
		return new Brix(brix);
	}

	public static SpecificGravity getSpecificGravity(Brix brix) {
		double b = brix.getDoubleValue();
		double sg = 0.00000005785037196 * Math.pow(b, 3) + 0.00001261831344
				* Math.pow(b, 2) + 0.003873042366 * b + 0.9999994636;
		sg=round(sg,3);
		return new SpecificGravity(sg);
	}

	public abstract double getDoubleValue();

	public abstract String getLabel();

	public abstract SpecificGravity toSpecificGravity();

	public abstract Brix toBrix();

	public Density getCorrectedDensityForRefractometer(Density originalDensity) {
		// Refractometer
		// cg = current specific gravity
		// cb = current Brix reading (refractometer)
		double ib = originalDensity.toBrix().getDoubleValue();
		Log.d("Density", "Initial Brix=" + ib);
		double cb = toBrix().getDoubleValue();
		Log.d("Density", "Current Brix=" + cb);
		double cg = 1.001843 - 0.002318474 * ib - 0.000007775 * Math.pow(ib, 2)
				- 0.000000034 * Math.pow(ib, 3) + 0.00574 * cb + 0.00003344
				* Math.pow(cb, 2) + 0.000000086 * Math.pow(cb, 3);
		Log.d("Density", "Current Gravity=" + cg);
		cg = round(cg,3);
		Log.d("Density", "Current Gravity rounded=" + cg);
		return new SpecificGravity(cg);
	}

	public Density getCorrectedDensityForHydrometer(Density originalDensity) {
		double initialBrix = originalDensity.toBrix().getDoubleValue();
		double currentBrix = toBrix().getDoubleValue();
		double trueBrix = (97 * initialBrix + 1200 * currentBrix) / 1297;
		trueBrix = round(trueBrix,3);
		return new Brix(trueBrix).toSpecificGravity();
	}

	private static double round(double trueBrix, double decimals) {
		double factor = Math.pow(10, decimals);
		return (double) Math.round(trueBrix * factor) / factor;
	}
}
