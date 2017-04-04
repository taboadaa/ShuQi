package com.arete.custom;

import java.util.ArrayList;


import phychips.arete.newver.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListView2Adapter extends ArrayAdapter<customCell>
{

//    private Context context;
    private int mResource;
    private ArrayList<customCell> mList;
    private LayoutInflater mInflater;

    public CustomListView2Adapter(Context context, int layoutResource,
	    ArrayList<customCell> objects)
    {
	super(context, layoutResource, objects);
//	this.context = context;
	this.mResource = layoutResource;
	this.mList = objects;
	this.mInflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
	final customCell stag = mList.get(position);

	if (convertView == null)
	{
	    convertView = mInflater.inflate(mResource, null);
	}

	if (stag != null)
	{
	    TextView tTextView = (TextView) convertView
		    .findViewById(R.id.popsettingname);
	    TextView cTextView = (TextView) convertView
		    .findViewById(R.id.popsettingvalue);

	    tTextView.setText(stag.getName());
	    cTextView.setText(stag.getValue());
//	    convertView.setOnClickListener(new OnClickListener()
//	    {
//		@Override
//		public void onClick(View v)
//		{
//		    // TODO Auto-generated method stub
//		    if (stag.getName().equals("On/Off Time(ms)"))
//		    {
//			Intent intent = new Intent(context,
//				OnOffTimeActivity.class);
//			intent.putExtra("value", stag.getValue());
//			context.startActivity(intent);
//		    }
//		    else if (stag.getName().equals("Ouput Power"))
//		    {
//			Intent intent = new Intent(context,
//				OutPutPowerActivity.class);
//
//			context.startActivity(intent);
//		    }
//		    else if (stag.getName().equals("Stop Conditions"))
//		    {
//			Intent intent = new Intent(context,
//				StopCondisionsActivity.class);
//			context.startActivity(intent);
//		    }
//		}
//	    });
	}
	
	return convertView;
    }
}