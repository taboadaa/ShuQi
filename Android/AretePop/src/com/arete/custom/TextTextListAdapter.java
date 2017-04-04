package com.arete.custom;

import java.util.ArrayList;

import phychips.arete.newver.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TextTextListAdapter extends ArrayAdapter<customCell> {

	@SuppressWarnings("unused")
	private Context context;
	private int mResource;
	private ArrayList<customCell> mList;
	private LayoutInflater mInflater;
	
	
	public TextTextListAdapter(Context context,int layoutResource,ArrayList<customCell> objects){
		super(context,layoutResource, objects);
		this.context = context;
		this.mResource = layoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		final customCell stag = mList.get(position);
		
		if(convertView == null){
			convertView = mInflater.inflate(mResource, null);
		}
		
		if(stag != null){
			
			TextView tTextView = (TextView) convertView.findViewById(R.id.aboutname);
			TextView cTextView = (TextView) convertView.findViewById(R.id.aboutvalue);
			
			tTextView.setText(stag.getName());
			cTextView.setText(stag.getValue());
			
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
//			cImageView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent	intent = new Intent(context, TagMemoryActivity.class);
//					intent.putExtra("tagitem", stag.getTag());
//					context.startActivity(intent);
//					
//				}
//			});
		}
		
		return convertView;
		
	}
}