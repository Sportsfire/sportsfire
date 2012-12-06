package com.sportsfire.sportsfireinjury;

import com.sportsfire.Squad;

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
public class SquadListActivity extends FragmentActivity implements
		SquadListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_squad_list);
	}

	/**
	 * Callback method from {@link SquadListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onSquadSelected(Squad id) {
		// In single-pane mode, simply start the detail activity
		// for the selected item ID.
		Intent playerIntent = new Intent(this, PlayerListActivity.class);
		playerIntent.putExtra(PlayerListFragment.ARG_ITEM_ID, id);
		startActivity(playerIntent);
	}
}
