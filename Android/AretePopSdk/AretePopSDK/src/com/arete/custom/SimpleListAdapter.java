package com.arete.custom;

import java.util.ArrayList;

import phychips.arete.newver.AppSettingActivity;
import phychips.arete.newver.KillActivity;
import phychips.arete.newver.LockActivity;
import phychips.arete.newver.PopInformationActivity;
import phychips.arete.newver.PopSettingActivity;
import phychips.arete.newver.R;
import phychips.arete.newver.ReadActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleListAdapter extends ArrayAdapter<String>
{

    private Context context;
    private int mResource;
    private ArrayList<String> mList;
    private LayoutInflater mInflater;

    public SimpleListAdapter(Context context, int layoutResource,
	    ArrayList<String> objects)
    {
	super(context, layoutResource, objects);
	this.context = context;
	this.mResource = layoutResource;
	this.mList = objects;
	this.mInflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
	final String name = mList.get(position);

	if (convertView == null)
	{
	    convertView = mInflater.inflate(mResource, null);
	}

	if (name != null)
	{

	    TextView textView = (TextView) convertView.findViewById(R.id.name);
	    textView.setText(name);

	    // tTextView.setOnLongClickListener(new OnLongClickListener() {
	    //
	    // @Override
	    // public boolean onLongClick(View v) {
	    // // TODO Auto-generated method stub
	    // Intent intent = new Intent(context, TagMemoryActivity.class);
	    // System.out.println(stag.getTag());
	    // intent.putExtra("tagitem", stag.getTag());
	    // context.startActivity(intent);
	    // return false;
	    // }
	    // });
	    //
	    @SuppressWarnings("unused")
	    ImageView cImageView = (ImageView) convertView
		    .findViewById(R.id.nextImage);
	    convertView.setOnClickListener(new OnClickListener()
	    {

		@Override
		public void onClick(View v)
		{
		    // TODO Auto-generated method stub
		    if (name.equals("POP Setting"))
		    {
			Intent intent = new Intent(context,
				PopSettingActivity.class);
			context.startActivity(intent);
		    }
		    else if(name.equals("App Setting")){

			Intent intent = new Intent(context,
				AppSettingActivity.class);
			context.startActivity(intent);
		    }else if(name.equals("About")){
			Intent intent = new Intent(context,
				PopInformationActivity.class);
			context.startActivity(intent);
		    }else if(name.equals("Output Power")){
			Intent intent = new Intent(context,
				PopInformationActivity.class);
			context.startActivity(intent);
		    }else if(name.equals("On/Off Time(ms)")){
			Intent intent = new Intent(context,
				PopInformationActivity.class);
			context.startActivity(intent);
		    }else if(name.equals("Read / Write")){
			Intent intent = new Intent(context,
				ReadActivity.class);
			context.startActivity(intent);
		    }else if(name.equals("Lock")){
			Intent intent = new Intent(context,
				LockActivity.class);
			context.startActivity(intent);
		    }else if(name.equals("Kill")){
			Intent intent = new Intent(context,
				KillActivity.class);
			context.startActivity(intent);
		    }else{// Stop Conditions
			
		    }
		}
	    });
	}

	return convertView;

    }
}