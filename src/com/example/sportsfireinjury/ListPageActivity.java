package com.example.sportsfireinjury;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of Squads. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link SquadDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SquadListFragment} and the item details (if present) is a
 * {@link SquadDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link SquadListFragment.Callbacks} interface to listen for item selections.
 */
public class ListPageActivity extends FragmentActivity implements
		SquadListFragment.Callbacks,PlayerListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_page);
		if (findViewById(R.id.player_list_container) != null) {
			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((SquadListFragment) getSupportFragmentManager().findFragmentById(
					R.id.squad_list)).setActivateOnItemClick(true);
		}
		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link SquadListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onSquadSelected(String id) {
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(PlayerListFragment.ARG_ITEM_ID, id);
			PlayerListFragment fragment = new PlayerListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.player_list_container, fragment).commit();

			
	}
public void onItemSelected(String id) {
		// In two-pane mode, show the detail view in this activity by
		// adding or replacing the detail fragment using a
		// fragment transaction.
	if (findViewById(R.id.player_list_container) != null) {
		// In two-pane mode, list items should be given the
		// 'activated' state when touched.
		((SquadListFragment) getSupportFragmentManager().findFragmentById(
				R.id.squad_list)).setActivateOnItemClick(true);
	}
		Bundle arguments = new Bundle();
		arguments.putString(PlayerInjuryFragment.ARG_ITEM_ID, id);
		PlayerInjuryFragment fragment = new PlayerInjuryFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.player_injury_detail_container, fragment).commit();
}
}
