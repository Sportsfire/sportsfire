package com.sportsfire.sportsfireinjury;


import java.util.ArrayList;

import com.sportsfire.InjuryReportID;
import com.sportsfire.Player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayerInjuryFragment extends Fragment {
	public static final String ARG_ITEM_ID = "player_name";
	Player player;
	ArrayList<InjuryReportID> injury;
	public PlayerInjuryFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// mItem =
			// DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
			// have player name want to get injury details
			player = getArguments().getParcelable(ARG_ITEM_ID);
			injury = player.getInjuryReportList();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.player_injury_detail,
				container, false);
		if (player != null) {
			((TextView) rootView.findViewById(R.id.player_injury_detail))
					.setText(player.getInjuryReportNameList().get(0));
			((TextView) rootView.findViewById(R.id.sendButton1))
					.setTag(player.getInjuryReportList().get(0));
		}
		return rootView;
	}
}
