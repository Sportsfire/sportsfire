package com.sportsfire.screening;

import java.util.HashMap;

import com.sportsfire.*;
import com.sportsfire.injury.PlayerInjuryFragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;

/**
 * The activity makes heavy use of fragments. The list of items is a {@link SquadListFragment} This activity also
 * implements the required {@link SquadListFragment.Callbacks} interface to listen for item selections.
 */
public class AnalysisPageActivity extends FragmentActivity implements SquadListFragment.Callbacks,
		PlayerListFragment.Callbacks, TestSelectionFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
	 */
	TestSelectionFragment playerFragment;
	HashMap<CompoundButton, Spinner> testSelectionMap = new HashMap<CompoundButton, Spinner>();
	public static final String ARG_ITEM_SEASON_NAME = "argumentSeasonName";
	public static final String ARG_ITEM_SEASON_ID = "argumentSeasonId";
	private Player currentPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_analysis_page);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (findViewById(R.id.test_selection_container) != null) {
			// list items should be given the 'activated' state when touched.
			((SquadListFragment) getSupportFragmentManager().findFragmentById(R.id.squad_list))
					.setActivateOnItemClick(true);
		}
	}

	/**
	 * Callback method from {@link SquadListFragment.Callbacks} indicating that the item with the given ID was selected.
	 */
	public void onSquadSelected(Squad id) {
		// adding or replacing the detail fragment using a fragment transaction.
		Bundle arguments = new Bundle();
		arguments.putParcelable(PlayerListFragment.ARG_ITEM_ID, id);
		PlayerListFragment fragment = new PlayerListFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.player_list_container, fragment).commit();
		if (playerFragment != null){
			getSupportFragmentManager().beginTransaction().remove(playerFragment).commit();
		}
	}

	public void onPlayerSelected(Player id) {
		// adding or replacing the detail fragment using a fragment transaction.
		if (findViewById(R.id.player_list_container) != null) {
			// list items should be given the 'activated' state when touched.
			((SquadListFragment) getSupportFragmentManager().findFragmentById(R.id.squad_list))
					.setActivateOnItemClick(true);
		}
		currentPlayer = id;
		Bundle arguments = new Bundle();
		arguments.putParcelable(TestSelectionFragment.ARG_ITEM_ID, id);
		TestSelectionFragment fragment = new TestSelectionFragment();
		fragment.setArguments(arguments);
		playerFragment = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.test_selection_container, fragment).commit();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, com.sportsfire.MainPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void onSwitchClicked(View view) {
		if (playerFragment != null){
			playerFragment.onSwitchClicked(view);
		}
	}
	public void sendData(View view) {
		if (playerFragment != null){
			playerFragment.sendData(view);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (currentPlayer != null)
			onPlayerSelected(currentPlayer);
	}

	@Override
	public void onTestsChosen(HashMap<String, Integer> map) {
		// TODO Auto-generated method stub

	}

}
