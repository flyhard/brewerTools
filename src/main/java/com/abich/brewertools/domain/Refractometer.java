package com.abich.brewertools.domain;

import android.util.Log;

public class Refractometer extends AbstractMeasurmentEquipment {

	@Override
	public Density correctDensity(Density measurement, Density originalDensity) {
		// Refractometer
		// cg = current specific gravity
		// cb = current Brix reading (refractometer)
		final double ib = originalDensity.toBrix().getDoubleValue();
		Log.d("Density", "Initial Brix=" + ib);
		final double cb = measurement.toBrix().getDoubleValue();
		Log.d("Density", "Current Brix=" + cb);
		double cg = 1.001843 - 0.002318474 * ib - 0.000007775 * Math.pow(ib, 2)
				- 0.000000034 * Math.pow(ib, 3) + 0.00574 * cb + 0.00003344
				* Math.pow(cb, 2) + 0.000000086 * Math.pow(cb, 3);
		Log.d("Density", "Current Gravity=" + cg);
		cg = round(cg, 3);
		Log.d("Density", "Current Gravity rounded=" + cg);
		return new SpecificGravity(cg);
	}

}
