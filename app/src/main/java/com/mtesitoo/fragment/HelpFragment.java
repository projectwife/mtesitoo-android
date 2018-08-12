package com.mtesitoo.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.mtesitoo.R;
import com.mtesitoo.adapter.ExpandableListViewAdapter;

import butterknife.ButterKnife;

/**
 * Created by Nan on 1/1/2016.
 */
public class HelpFragment extends Fragment {

    ExpandableListView mExpandableListView;



    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_home_fragment_help, container, false);
        ButterKnife.bind(this, view);

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        String [] questions ={"q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10"};
        String [] answers = {"answer1","answer2", "answer3", "answer4", "answer5", "answer6", "answer7", "answer8", "answer9", "answer10"};

        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(getContext(), questions, answers);
        mExpandableListView.setAdapter(adapter);



        return view;
    }


}
