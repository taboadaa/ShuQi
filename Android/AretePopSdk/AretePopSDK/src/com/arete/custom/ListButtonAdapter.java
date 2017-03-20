package com.arete.custom;

import java.util.ArrayList;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;

import phychips.arete.newver.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ListButtonAdapter extends ArrayAdapter<ToggleButtonAndText>
{

    @SuppressWarnings("unused")
    private Context context;
    private int mResource;
    private ArrayList<ToggleButtonAndText> mList;
    private LayoutInflater mInflater;
    private ToggleButton tbtn;

    public ListButtonAdapter(Context context, int layoutResource,
	    ArrayList<ToggleButtonAndText> objects)
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
	final ToggleButtonAndText stag = mList.get(position);

	if (convertView == null)
	{
	    convertView = mInflater.inflate(mResource, null);
	}

	if (stag != null)
	{

	    TextView tTextView = (TextView) convertView.findViewById(R.id.PopSettingToggleText);
	   
	    tTextView.setText(stag.getName());
	     
	    tbtn = (ToggleButton) convertView.findViewById(R.id.toggleButton1);
	    
	   

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
	    // cImageView.setOnClickListener(new OnClickListener() {
	    //
	    // @Override
	    // public void onClick(View v) {
	    // // TODO Auto-generated method stub
	    // Intent intent = new Intent(context, TagMemoryActivity.class);
	    // intent.putExtra("tagitem", stag.getTag());
	    // context.startActivity(intent);
	    //
	    // }
	    // });
	    tbtn.setOnCheckedChangeListener(new OnCheckedChangeListener()
	    {
	        
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	        {
	    	// TODO Auto-generated method stub
	            if(isChecked){
	        	try
			{
			    RcpApi.setBeep(false);
			}
			catch (RcpException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
	            }
	            else{
	        	try
			{
			    RcpApi.setBeep(true);
			}
			catch (RcpException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
	            }
	        }
	    });
	    System.out.println("beeep sssstate "+stag.isState());
	    tbtn .setChecked(stag.isState());
	    
	   
	}

	return convertView;

    }
}