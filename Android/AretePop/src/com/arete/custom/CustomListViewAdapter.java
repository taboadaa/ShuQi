package com.arete.custom;

import java.util.ArrayList;

import phychips.arete.newver.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<customCell>
{
    private int mResource;
    private ArrayList<customCell> mList;
    private LayoutInflater mInflater;

    public CustomListViewAdapter(Context context, int layoutResource,
	    ArrayList<customCell> objects)
    {
	super(context, layoutResource, objects);
	this.mResource = layoutResource;
	this.mList = objects;
	this.mInflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
	final customCell cell = mList.get(position);

	if (convertView == null)
	{
	    convertView = mInflater.inflate(mResource, null);
	}

	if (cell != null)
	{
	    TextView tTextView = (TextView) convertView
		    .findViewById(R.id.list_tag);
	    TextView cTextView = (TextView) convertView
		    .findViewById(R.id.list_count);
//	    @SuppressWarnings("unused")
//	    ImageView cImageView = (ImageView) convertView
//	    .findViewById(R.id.listSelect);
	    tTextView.setText(cell.getName());
	    cTextView.setText(cell.getValue());
	}

	return convertView;

    }
}
