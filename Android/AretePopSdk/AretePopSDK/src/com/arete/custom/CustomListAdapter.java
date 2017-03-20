package com.arete.custom;

import java.util.ArrayList;

import phychips.arete.newver.R;
import phychips.arete.newver.TagAccessActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<seqTag> {

	private Context context;
	private int mResource;
	private ArrayList<seqTag> mList;
	private LayoutInflater mInflater;
	
	
	public CustomListAdapter(Context context,int layoutResource,ArrayList<seqTag> objects){
		super(context,layoutResource, objects);
		this.context = context;
		this.mResource = layoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		final seqTag stag = mList.get(position);
		
		if(convertView == null){
			convertView = mInflater.inflate(mResource, null);
		}
		
		if(stag != null){
			
			TextView tTextView = (TextView) convertView.findViewById(R.id.list_tag);
			TextView cTextView = (TextView) convertView.findViewById(R.id.list_count);
			@SuppressWarnings("unused")
			ImageView cImageView = (ImageView) convertView.findViewById(R.id.listSelect);	
			
			tTextView.setText(stag.getTag());
			cTextView.setText(stag.getCount());
//			tTextView.setOnLongClickListener(new OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View v) {
//					// TODO Auto-generated method stub
//					Intent	intent = new Intent(context, TagMemoryActivity.class);
//					System.out.println(stag.getTag());
//					intent.putExtra("tagitem", stag.getTag());
//					context.startActivity(intent);
//					return false;
//				}
//			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent	intent = new Intent(context, TagAccessActivity .class);
					intent.putExtra("tagitem", stag.getTag());
					context.startActivity(intent);
					
				}
			});
		}
		
		return convertView;
		
	}
}
