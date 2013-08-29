package com.sportsfire;

import java.util.ArrayList;
import java.util.List;

import com.sportsfire.objects.Player;
import com.sportsfire.objects.Squad;

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
	Squad selectedSquad;
	ArrayAdapter<String> listAdapter;

	public interface Callbacks {

		public void onPlayerSelected(Player player);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onPlayerSelected(Player id) {
		}
	};

	public PlayerListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			if (getArguments().containsKey(ARG_ITEM_ID)) {
				selectedSquad = getArguments().getParcelable(ARG_ITEM_ID);
				playersList = selectedSquad.getPlayerNameList();
			}
		}
		listAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1, android.R.id.text1, playersList);
		setListAdapter(listAdapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setActivateOnItemClick(true);
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			// throw new IllegalStateException("Activity must implement fragment's callbacks.");
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
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onPlayerSelected(selectedSquad.getPlayerList().get(position));

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
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
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
