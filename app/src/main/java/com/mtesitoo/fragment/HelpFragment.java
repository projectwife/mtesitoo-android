package com.mtesitoo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtesitoo.adapter.ExpandableListViewAdapter;


import com.mtesitoo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Nan on 1/1/2016.
 */
public class HelpFragment extends Fragment {

    ExpandableListView mExpandableListView;
    Button otherQuestions;
    ExpandableListViewAdapter expandableListViewAdapter;

    private List<String> listDataGroup;

    private HashMap<String, String> listDataChild;


    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_home_fragment_help, container, false);
        ButterKnife.bind(this, view);

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();

        //initialize the listeners
        initListeners();



        return view;
    }

    /**
     * method to initialize the objects
     */
    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();
        //  listDataChild = new ArrayList<>();

        // initializing the adapter object(changed)
        expandableListViewAdapter = new ExpandableListViewAdapter(getContext(), listDataGroup, listDataChild);


        // setting list adapter
        mExpandableListView.setAdapter(expandableListViewAdapter);

    }



// method to initialize the listeners
   private void initListeners() {


        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);

                //Tests to seee if user has clicked on final position in ListView, where
                //email can be sent to ank additional questions
                if (groupPosition == 10) {

                    emailintent.setType("plain/text");
                    emailintent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"admin@tesitoo.com"});
                    emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Other Questions");
                    emailintent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                    startActivity(Intent.createChooser(emailintent, "Send mail..."));
                }
                return false;
            }
        });

        // ExpandableListView on child click listener
       // FOR TOAST MESSAGES - if desired
    /*    mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        listDataGroup.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataGroup.get(groupPosition)), Toast.LENGTH_SHORT)
                        .show();

                return false;
            }
        });

        // ExpandableListView Group expanded listener
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();

            }
        });
        */
    }

    private void initListData() {

        String[] questionArray;
        // Adding group data
        questionArray = getResources().getStringArray(R.array.Questions);
        for (String item : questionArray) {
            listDataGroup.add(item);
        }


        //Get Child Data
        String answer1 = getResources().getString(R.string.Answer1);
        String answer2 = getResources().getString(R.string.Answer2);
        String answer3 = getResources().getString(R.string.Answer3);
        String answer4 = getResources().getString(R.string.Answer4);
        String answer5 = getResources().getString(R.string.Answer5);
        String answer6 = getResources().getString(R.string.Answer6);
        String answer7 = getResources().getString(R.string.Answer7);
        String answer8 = getResources().getString(R.string.Answer8);
        String answer9 = getResources().getString(R.string.Answer9);
        String answer10 = getResources().getString(R.string.Answer10);
        String answer11 = getResources().getString(R.string.Answer11);

        // Adding child data

        listDataChild.put(listDataGroup.get(0), answer1);
        listDataChild.put(listDataGroup.get(1), answer2);
        listDataChild.put(listDataGroup.get(2), answer3);
        listDataChild.put(listDataGroup.get(3), answer4);
        listDataChild.put(listDataGroup.get(4), answer5);
        listDataChild.put(listDataGroup.get(5), answer6);
        listDataChild.put(listDataGroup.get(6), answer7);
        listDataChild.put(listDataGroup.get(7), answer8);
        listDataChild.put(listDataGroup.get(8), answer9);
        listDataChild.put(listDataGroup.get(9), answer10);
        listDataChild.put(listDataGroup.get(10), answer11);



        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();

    }}

