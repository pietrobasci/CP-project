package com.unisannio.multisala;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pietrobasci on 03/01/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    public ExpandableListAdapter(Activity context, ArrayList<String> data, HashMap<String, ArrayList<String>> dateProgra) {
        this.context = context;
        this.dateProgrammazioneFilm = dateProgra;
        this.dataProgrammazioneFilm = data;
    }

    @Override
    public int getGroupCount() {
        return dataProgrammazioneFilm.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dateProgrammazioneFilm.get(dataProgrammazioneFilm.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataProgrammazioneFilm.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dateProgrammazioneFilm.get(dataProgrammazioneFilm.get(groupPosition)).get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String data = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_expandablelistview, null);
        }
        TextView dat = (TextView) convertView.findViewById(R.id.textView10);
        dat.setText(data);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String oraa = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_expandablelistview, null);
        }
        TextView ora = (TextView) convertView.findViewById(R.id.textView11);
        ora.setText(oraa.toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private Activity context;
    private HashMap<String, ArrayList<String>> dateProgrammazioneFilm ;
    private ArrayList<String> dataProgrammazioneFilm;
}


