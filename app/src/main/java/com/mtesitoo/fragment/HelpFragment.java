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



        return view;
    }


}
