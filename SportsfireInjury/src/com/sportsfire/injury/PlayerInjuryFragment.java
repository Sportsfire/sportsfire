package com.sportsfire.injury;

import java.util.ArrayList;

import com.sportsfire.Player;
import com.sportsfire.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayerInjuryFragment extends Fragment {
	public static final String ARG_ITEM_ID = "player_name";
	Player player;
	ArrayList<String> injuryList;
	ArrayAdapter<String> adapter;

	public PlayerInjuryFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// have player name want to get injury details
			player = getArguments().getParcelable(ARG_ITEM_ID);
			injuryList = player.getInjuryReportNameList();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.player_injury_detail, container, false);
		if (player != null) {
			ListView listView = (ListView) rootView.findViewById(R.id.injurylist);
			adapter = new ArrayAdapter<String>(listView.getContext(),
					android.R.layout.simple_list_item_1, injuryList);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (((TextView) view).getText() != "No Injuries!") {
						System.out.println(view.toString());
						Intent intent = new Intent(view.getContext(), InjuryForm.class);
						intent.putExtra(InjuryForm.ARG_ITEM_INJURY, player.getInjuryReportList()
								.get(position));
						startActivity(intent);
					}

				}
			});

			((TextView) rootView.findViewById(R.id.sendButton1)).setTag(player
					.getInjuryReportList().get(0));
		}
		return rootView;
	}
}
