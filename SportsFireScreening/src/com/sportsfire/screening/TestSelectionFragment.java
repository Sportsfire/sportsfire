package com.sportsfire.screening;

import java.util.LinkedHashMap;

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
	LinkedHashMap<CompoundButton, Spinner> testSelectionMap = new LinkedHashMap<CompoundButton, Spinner>();

	public interface Callbacks {

		public void onTestsChosen(LinkedHashMap<String, Integer> testsMap);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onTestsChosen(LinkedHashMap<String, Integer> testsMap) {
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
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.CountermovementJumpSwitch),
				(Spinner) rootView.findViewById(R.id.CountermovementJumpSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.DepthJumpSwitch),
				(Spinner) rootView.findViewById(R.id.DepthJumpSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.SquatJumpSwitch),
				(Spinner) rootView.findViewById(R.id.SquatJumpSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.TripleHopLSwitch),
				(Spinner) rootView.findViewById(R.id.TripleHopLSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.TripleHopRSwitch),
				(Spinner) rootView.findViewById(R.id.TripleHopRSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.AgilityLSwitch),
				(Spinner) rootView.findViewById(R.id.AgilityLSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.AgilityRSwitch),
				(Spinner) rootView.findViewById(R.id.AgilityRSpinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.Sprints5Switch),
				(Spinner) rootView.findViewById(R.id.Sprints5Spinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.Sprints10Switch),
				(Spinner) rootView.findViewById(R.id.Sprints10Spinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.Sprints20Switch),
				(Spinner) rootView.findViewById(R.id.Sprints20Spinner));
		testSelectionMap.put((CompoundButton) rootView.findViewById(R.id.YoYoTestSwitch),
				(Spinner) rootView.findViewById(R.id.YoYoTestSpinner));
		
	
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
		LinkedHashMap<String, Integer> selectedTests = new LinkedHashMap<String, Integer>();
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
