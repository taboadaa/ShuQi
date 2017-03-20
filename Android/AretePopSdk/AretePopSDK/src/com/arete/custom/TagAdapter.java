package com.arete.custom;

import java.util.ArrayList;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class TagAdapter extends ArrayAdapter<String> {

	Activity context;
	ArrayList<String> data;

	public TagAdapter(Activity context, ArrayList<String> data) {
		super(context, android.R.layout.simple_list_item_1, data);
		this.data = data;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// ViewHolder holder;
//	TextView tagitem;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);

			rowView.setOnLongClickListener(new View.OnLongClickListener() {

				public boolean onLongClick(View v) {
	//				TextView tag;
	//				String selected;
	//				String[] split;

					try
					{
					    RcpApi.stopReadTags();
					}
					catch (RcpException e)
					{
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}

//					tag = (TextView) v.getTag();
//					selected = (String) tag.getText();
//					split = selected.split(" ");

					// Toast.makeText(MainActivity.this, split[1] + " selected",
					// Toast.LENGTH_LONG).show();

//					Intent intent = new Intent(context,
//							TagmemoryActivity.class);
//					intent.putExtra("tagitem", split[0]);
//					startActivity(intent);

					return false;
				}
			});

//			tagitem = (TextView) rowView.findViewById(R.id.tagitem);
//			rowView.setTag(tagitem);
		} else {
//			tagitem = (TextView) rowView.getTag();
		}

//		String s = data.get(position);
//		tagitem.setText(s);

		return rowView;
	}
}