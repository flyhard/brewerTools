package com.abich.brewertools.domain;

public class Brix extends Density {

	private final double value;

	public Brix(final double brix) {
		value = brix;
	}

	@Override
	public double getDoubleValue() {
		return value;
	}

	@Override
	public String getLabel() {
		return "\u00B0Brix";
	}

	@Override
	public SpecificGravity toSpecificGravity() {
		return Density.getSpecificGravity(this);
	}

	@Override
	public String toString() {
		return "Brix [value=" + value + "]";
	}

	@Override
	public Brix toBrix() {
		return this;
	}

}
