package com.sportsfire.sportsfireinjury;


import com.sportsfire.Player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class PlayerListActivity extends FragmentActivity
        implements PlayerListFragment.Callbacks{

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_playerlist);

		if (findViewById(R.id.player_injury_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((PlayerListFragment) getSupportFragmentManager().findFragmentById(
					R.id.player_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link PlayerListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	//@Override
	public void onItemSelected(Player id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putParcelable(PlayerInjuryFragment.ARG_ITEM_ID, id);
			PlayerInjuryFragment fragment = new PlayerInjuryFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.player_injury_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PlayerInjuryActivity.class);
			detailIntent.putExtra(PlayerInjuryFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}

