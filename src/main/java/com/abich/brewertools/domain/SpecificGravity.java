package com.abich.brewertools.domain;

public class SpecificGravity extends Density {

	private final double value;

	public SpecificGravity(double specificGravity) {
		value = specificGravity;
	}

	@Override
	public double getDoubleValue() {
		return value;
	}

	@Override
	public String getLabel() {
		return "SG";
	}

	@Override
	public SpecificGravity toSpecificGravity() {
		return this;
	}

	@Override
	public Brix toBrix() {
		return Density.getBrix(this);
	}

	@Override
	public String toString() {
		return "SpecificGravity [value=" + value + "]";
	}

}
