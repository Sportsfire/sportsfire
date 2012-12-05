package com.sportsfire.sportsfireinjury;

//import android.app.ListFragment;
//import android.os.Bundle;
//import android.app.Activity;
//import android.content.Intent;
//import android.view.Menu;
//import android.view.View;
//import android.widget.ArrayAdapter;
//
//public class PlayerListFragment extends ListFragment {
//	 public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        Bundle extras = getIntent().getExtras();
//	        String e = extras.getString("playerSelected", "bc");
//	        setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),
//	                R.array.tut_titles, R.layout.activity_playerlist_page));
//	    }
//}

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayerListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	public static final String ARG_ITEM_ID = "squad_name";
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	List<String> playersList = new ArrayList<String>();

	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onItemSelected(String id) {
		}
	};

	public PlayerListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setListAdapter(new
		// ArrayAdapter<DummyContent.DummyItem>(getActivity(),
		// R.layout.simple_list_item_activated_1,
		// R.id.text1,
		// DummyContent.ITEMS));
		if (getArguments() != null) {
			if (getArguments().containsKey(ARG_ITEM_ID)) {
				String name = getArguments().getString(ARG_ITEM_ID);
					playersList.add("d");
					playersList.add("e");
					playersList.add("f");
					playersList.add(name);
			}
		}
		playersList.add("g");
		playersList.add("h");
		playersList.add("i");
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, playersList));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setActivateOnItemClick(true);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			 //throw new IllegalStateException(
			// "Activity must implement fragment's callbacks.");
		} else {
			mCallbacks = (Callbacks) activity;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onItemSelected(playersList.get(position));
		// mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
