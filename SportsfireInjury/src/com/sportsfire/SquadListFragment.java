//import android.app.ListActivity;
//import android.app.ListFragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//public class SquadListFragment extends ListFragment {
//	
//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//	    String[] links = getResources().getStringArray(R.array.tut_links);
//	    String content = links[position];
//	    Intent intent = new Intent(getActivity().getApplicationContext(), PlayerListFragment.class);
//		intent.putExtra("playerSelected", "a");
//		SquadListFragment.this.startActivity(intent);
//	    //Intent showContent = new Intent(getActivity().getApplicationContext(),TutViewerActivity.class);
//	   // showContent.setData(Uri.parse(content));
//	    //startActivity(showContent);
//	}
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//        setListAdapter(ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
//        			R.array.tut_titles, R.layout.activity_list_page));
//    }
//}

package com.sportsfire;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;




public class SquadListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    SquadList squad;
    List<String> squadList;
    
    public interface Callbacks {

        public void onSquadSelected(Squad squad);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        public void onSquadSelected(Squad id) {
        }
    };

    public SquadListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	squad = new SquadList(getActivity());
    	squadList = squad.getSquadNameList();
        setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,android.R.id.text1, squadList));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState
                .containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
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

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onSquadSelected(squad.getSquadList().get(position));
 
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
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
    public void onResume() {
    	super.onResume();
    	//refresh squadlist
    	squad.refresh();
    }
}