package phychips.arete.newver;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.RcpTypeCTag;

import phychips.arete.newver.R;

import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KillActivity extends Activity
{
    private Button back,done;
    private TextView kill_targetTag,kill_password;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_kill);
	
	kill_targetTag = (TextView)findViewById(R.id.kill_targetTag);
	kill_targetTag.setText(TagAccessActivity.nowTag);
	
	kill_password = (TextView) findViewById(R.id.kill_pass);
	
	back = (Button) findViewById(R.id.kill_navigation_back_button);
	back.setOnClickListener(new OnClickListener()
	{
	    
	    @Override
	    public void onClick(View v)
	    {
		moveTaskToBack(false);
		finish();
	    }
	});
	
	done = (Button) findViewById(R.id.kill_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (RcpApi.isOpen) {
			try {
				String epc = kill_targetTag.getText().toString();
				RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2, 1);

				tag.password = Long.parseLong(kill_password.getText()
						.toString(), 16);
				
				tag.epc = RcpLib.convertStringToByteArray(epc);

				tag.recom = 0;

				RcpApi.killTag(tag);
			} catch (Exception e) {
				Toast.makeText(KillActivity.this, e.toString(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
		    	Toast.makeText(KillActivity.this, "Kill Tag successs",
				Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

}
