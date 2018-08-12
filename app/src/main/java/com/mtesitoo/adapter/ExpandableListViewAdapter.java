package com.mtesitoo.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.content.res.Resources;
import android.widget.TextView;

import com.mtesitoo.fragment.HelpFragment;

import com.mtesitoo.R;

/**
 * Created by gwenp on 7/28/2018.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    Context context;

    String[] questions;
    String[] answers;


//    String[] questions = getResources().getStringArray(R.array.Questions);
//    String[] answers = getResources().getStringArray(R.array.Answers);

 // USED FOR TESTING
//    String [] questions ={"q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10"};
//     String [] answers = {"answer1","answer2", "answer3", "answer4", "answer5", "answer6", "answer7", "answer8", "answer9", "answer10"};

    //in the Constructor, pass the context in the parametres


    // The question answers can be an array of Hash maps where each item in the array is a hash map with 2 keys - question, answer
    // Or we can have a Question class which has 2 string fields - question, answer and we pass an array of these Question objects to the adapter
    public ExpandableListViewAdapter(Context context, String [] ques, String [] ans) {

        this.context = context;
        this.questions = ques;
        this.answers = ans;
    }

    @Override
    public int getGroupCount() {
        return questions.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    } // Each question has only 1 answer, therefore only 1 child view

    @Override
    public Object getGroup(int groupPosition) {
        return questions[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return answers[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        TextView textView = new TextView(context);
        textView.setText(questions[groupPosition]);
        textView.setPadding(100,0,0,0);
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText(answers[groupPosition]);
        textView.setPadding(100,0,0,0);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
