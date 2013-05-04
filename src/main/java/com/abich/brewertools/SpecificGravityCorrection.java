package com.abich.brewertools;

import java.text.MessageFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.abich.brewertools.domain.Brix;
import com.abich.brewertools.domain.Density;
import com.abich.brewertools.domain.SpecificGravity;
import com.abich.brewertools.utils.AbstractTextWatcher;
import com.google.common.base.Function;
import com.google.common.base.Optional;

public class SpecificGravityCorrection extends Activity {
	private static final String TAG = "SpecificGravityCorrection";

	private Optional<? extends Density> og = Optional.absent();
	private Optional<? extends Density> mg = Optional.absent();
	private Optional<? extends Density> correctedGravity = Optional.absent();

	private EditText ogValue;
	private EditText mgValue;
	private EditText correctedValue;

	private TextView ogUnitLabel;
	private TextView mgUnitLabel;
	private TextView correctedUnitLabel;

	private RadioButton sgButton;
	private RadioButton refractometerButton;

	private final Function<Density, Density> toBrixFunction = new Function<Density, Density>() {
		@Override
		public Density apply(final Density in) {
			return in.toBrix();
		}
	};
	private final Function<Density, Density> toSpecificGravityFunction = new Function<Density, Density>() {
		@Override
		public Density apply(final Density in) {
			return in.toSpecificGravity();
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_specific_gravity_correction);

		sgButton = (RadioButton) findViewById(R.id.sg);
		refractometerButton = (RadioButton) findViewById(R.id.refractometer);

		ogValue = (EditText) findViewById(R.id.ogValue);
		mgValue = (EditText) findViewById(R.id.measuredValue);
		correctedValue = (EditText) findViewById(R.id.correctedGravity);

		initTextWatchers();

		ogUnitLabel = (TextView) findViewById(R.id.ogUnitLabel);
		mgUnitLabel = (TextView) findViewById(R.id.measuredUnitLabel);
		correctedUnitLabel = (TextView) findViewById(R.id.correctedGravityLabel);
	}

	private void initTextWatchers() {
		ogValue.addTextChangedListener(new AbstractTextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
				Log.d(TAG, "Updating OG value to " + s);
				final Double value = Double.parseDouble(s.toString());
				if (sgButton.isChecked()) {
					og = Optional.of(new SpecificGravity(value));
				} else {
					og = Optional.of(new Brix(value));
				}
				calculateCorrectedGravity();
			}

		});
		mgValue.addTextChangedListener(new AbstractTextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
				final Double value = Double.parseDouble(s.toString());
				if (sgButton.isChecked()) {
					mg = Optional.of(new SpecificGravity(value));
				} else {
					mg = Optional.of(new Brix(value));
				}
				calculateCorrectedGravity();
			}

		});

	}

	public void setUnitType(final View view) {
		assert view instanceof RadioButton;
		Log.d(TAG, "eventhandler started");
		final boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.sg:
			if (checked) {
				setOg(getOg().transform(toSpecificGravityFunction));
				setMg(getMg().transform(toSpecificGravityFunction));
				setCorrectedGravity(getCorrectedGravity().transform(
						toSpecificGravityFunction));
			}
			Log.d(TAG, "SG detected");
			break;
		case R.id.brix:
			if (checked) {
				setOg(getOg().transform(toBrixFunction));
				setMg(getMg().transform(toBrixFunction));
				setCorrectedGravity(getCorrectedGravity().transform(
						toBrixFunction));
			}
			Log.d(TAG, "Brix detected");
			break;
		}

		updateDisplayedValuesAndLabels();
	}

	private void updateDisplayedValuesAndLabels() {
		updateValueAndUnit(getOg(), ogValue, ogUnitLabel);
		updateValueAndUnit(getMg(), mgValue, mgUnitLabel);
		updateValueAndUnit(getCorrectedGravity(), correctedValue,
				correctedUnitLabel);
	}

	private void updateValueAndUnit(final Optional<? extends Density> value,
			final EditText field, final TextView unitLabel) {
		if (value.isPresent()) {
			final Density density = value.get();
			field.setText(MessageFormat.format("{0,number,#.000}",
					density.getDoubleValue()));
			unitLabel.setText(density.getLabel());
		}
	}

	private void calculateCorrectedGravity() {
		if (og.isPresent() && mg.isPresent()) {
			if (refractometerButton.isChecked()) {
				correctedGravity = Optional.of(mg.get()
						.getCorrectedDensityForRefractometer(og.get()));
			} else {
				correctedGravity = Optional.of(mg.get()
						.getCorrectedDensityForHydrometer(og.get()));
			}
			updateValueAndUnit(getCorrectedGravity(), correctedValue,
					correctedUnitLabel);
		}
	}

	public Optional<? extends Density> getOg() {
		return og;
	}

	public void setOg(final Optional<? extends Density> og) {
		this.og = og;
	}

	public Optional<? extends Density> getMg() {
		return mg;
	}

	public void setMg(final Optional<? extends Density> mg) {
		this.mg = mg;
	}

	public Optional<? extends Density> getCorrectedGravity() {
		return correctedGravity;
	}

	public void setCorrectedGravity(final Optional<Density> correctedGravity) {
		this.correctedGravity = correctedGravity;
	}

}
