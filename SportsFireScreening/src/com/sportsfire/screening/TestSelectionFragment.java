package com.sportsfire.screening;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

public class TestSelectionFragment extends Fragment {
	private Callbacks mCallbacks = sDummyCallbacks;
	HashMap<CompoundButton, Spinner> testSelectionMap = new HashMap<CompoundButton, Spinner>();

	public interface Callbacks {

		public void onTestsChosen(HashMap<String, Integer> testsMap);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onTestsChosen(HashMap<String, Integer> testsMap) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.screening_testselection_fragment, container,
				false);
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.WeightSwitch),
				(Spinner) rootView.findViewById(R.id.WeightSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.SqueezeSwitch),
				(Spinner) rootView.findViewById(R.id.SqueezeSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.CMJSwitch),
				(Spinner) rootView.findViewById(R.id.CMJSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.HeightSwitch),
				(Spinner) rootView.findViewById(R.id.HeightSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.BodyFatSwitch),
				(Spinner) rootView.findViewById(R.id.BodyFatSpinner));
		String[] choices;
		if (getActivity().getClass().equals(AnalysisPageActivity.class)) {
			choices = getResources().getStringArray(R.array.ComparisonChoices);
			for (Spinner spinner : testSelectionMap.values()) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_spinner_dropdown_item, choices);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
			}
		}
		return rootView;
	}

	public void onSwitchClicked(View view) {
		if (((CompoundButton) view).isChecked()) {
			(testSelectionMap.get(view)).setVisibility(View.VISIBLE);
		} else {
			(testSelectionMap.get(view)).setVisibility(View.INVISIBLE);
		}
	}

	public void sendData(View view) {
		HashMap<String, Integer> selectedTests = new HashMap<String, Integer>();
		for (CompoundButton k : testSelectionMap.keySet()) {
			if (k.isChecked()) {
				selectedTests.put(k.getText().toString(),
						(testSelectionMap.get(k)).getSelectedItemPosition());
			}
		}
		mCallbacks.onTestsChosen(selectedTests);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}
}
