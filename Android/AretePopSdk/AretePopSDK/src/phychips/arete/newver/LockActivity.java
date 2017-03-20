package phychips.arete.newver;

import com.makeramen.segmented.SegmentedRadioGroup;
import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.RcpTypeCTag;
import phychips.arete.newver.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends Activity implements OnCheckedChangeListener
{
    private Button back,done;

    private SegmentedRadioGroup segment_TagMemory;
    private SegmentedRadioGroup segment_Action;
    
    private TextView lock_targetTag,lock_accessPass;
    
    private int action = 0, lockdata = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_lock);
	
	segment_TagMemory = (SegmentedRadioGroup) findViewById(R.id.seg_group_three);
	segment_TagMemory.setOnCheckedChangeListener(this);
	segment_Action = (SegmentedRadioGroup) findViewById(R.id.seg_group_four);
	segment_Action.setOnCheckedChangeListener(this);
	
	lock_targetTag = (TextView) findViewById(R.id.lock_targetTag);
	lock_targetTag.setText(TagAccessActivity.nowTag);
	
	lock_accessPass = (TextView) findViewById(R.id.lock_accessPass);
	

	back = (Button) findViewById(R.id.lock_navigation_back_button);
	back.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		moveTaskToBack(false);
		finish();
	    }
	});
	
	done = (Button) findViewById(R.id.lock_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (RcpApi.isOpen) {
			try {
				String epc = lock_targetTag.getText().toString();		
				
				RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2, 1);

				tag.password = Long.parseLong(lock_accessPass.getText()
						.toString(), 16);
		
				tag.epc = RcpLib.convertStringToByteArray(epc);

				tag.lock_mask = lockdata;
				
				RcpApi.lockTagMemory(tag);
			} catch (Exception e) {
				
			}
		}else{
		    Toast.makeText(LockActivity.this, "lock Tag successs",
				Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
	if (group == segment_TagMemory)
	{
	    if (checkedId == R.id.segment_kill)
	    {
		lockdata  = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_acs)
	    {
		lockdata  = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_epc)
	    {
		lockdata  = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_tid)
	    {
		lockdata  = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_user)
	    {
		lockdata  = (action << 8) | (3 << 18);
	    }
	}
	else if (group == segment_Action)
	{
	    if (checkedId == R.id.segment_unlock)
	    {
		action = 0;
	    }
	    else if (checkedId == R.id.segment_punlock)
	    {
		action = 1;
	    }
	    else if (checkedId == R.id.segment_lock)
	    {
		action = 2;
	    }
	    else if (checkedId == R.id.segment_plock)
	    {
		action = 3;
	    }
	}
    }
}
