package com.arete.custom;

import phychips.arete.newver.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonGroup extends LinearLayout {
	private static final String TAG = "ButtonGroup";
	private Context context;
	private ButtonGroup group;
	private int id = 0;
	private int currentItem = -1;
	
	public ButtonGroup(Context context) {
		super(context);
		this.context = context;
		setOrientation(LinearLayout.HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setWeightSum(1);
		group = this;
	}

	public void addView(String name) {

		ToggleButton button = new ToggleButton(context);
		button.setId(id++);
		button.setText(name);
		button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_fill));
		button.setWidth(200);
		button.setHeight(400);
	
		super.addView(button);
	}
	
	public int getCurrentItem(){
		return currentItem;
	}
	public class ToggleButton extends Button implements OnClickListener{
		private boolean toggle = false;
		private int childID = -1;
		public ToggleButton(Context context) {
			super(context);
			setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Log.i(TAG , "onClick");
			setToggle();
			childID = this.getId();
			if(toggle){
				Log.i(TAG , "toggle = true");
				View[] btnGroup = getChildViews(group);
				for(View btn : btnGroup){
					ToggleButton toggleBtn = (ToggleButton) btn;
					if(toggleBtn.getStateToggle() && toggleBtn.getId() != childID){
						Log.i(TAG , "getID : " + toggleBtn.getId());
						toggleBtn.setToggle();
						
					}
				}
			}
			setCurrentItem();
		}
		
		public boolean getStateToggle(){
			return toggle;
		}
		
		private void setToggle(){
			toggle = !toggle;
			if(toggle){
				setBackgroundResource(R.drawable.blue_fill);
			}else{
				setBackgroundResource(R.drawable.white_fill);
			}
		}
		
		private void setCurrentItem(){
			int childCount = group.getChildCount();
			for (int index = 0; index < childCount; index++) {
				if(((ToggleButton)group.getChildAt(index)).getStateToggle()){
					currentItem = index;
				}
			}
		}

		private View[] getChildViews(ViewGroup group) {
			int childCount = group.getChildCount();
			final View[] childViews = new View[childCount];
			for (int index = 0; index < childCount; index++) {
				childViews[index] = group.getChildAt(index);
			}
			return childViews;
		}

	}
}
