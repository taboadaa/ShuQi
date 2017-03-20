package phychips.arete.newver;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.RcpFhLbtParam;
import com.phychips.rcp.RcpLib;

import phychips.arete.newver.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OnOffTimeActivity extends Activity
{
    Button back, done;
    TextView TextView_OnTime, TextView_OffTime;
    EditText edit_text1, edit_text2;
    RcpFhLbtParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_on_off_time);

	TextView_OnTime = (TextView) findViewById(R.id.onofftimetext);
	TextView_OffTime = (TextView) findViewById(R.id.onofftimetext2);

	String value = getIntent().getStringExtra("value");

	TextView_OnTime.setText(value.substring(0, value.indexOf("/")));
	TextView_OffTime.setText(value.substring(value.indexOf("/") + 1));

	edit_text1 = (EditText) findViewById(R.id.onofftimetext);
	edit_text2 = (EditText) findViewById(R.id.onofftimetext2);

	this.param = PopSettingActivity.param;

	back = (Button) findViewById(R.id.onofftime_navigation_back_button);
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

	done = (Button) findViewById(R.id.onoff_done_btn);
	done.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {

		param.readtime = Integer.parseInt(edit_text1.getText()
			.toString());
		param.idletime = Integer.parseInt(edit_text2.getText()
			.toString());
		try
		{
		    RcpApi.setFhLbtParam(param);
		}
		catch (RcpException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		Toast.makeText(OnOffTimeActivity.this, "Success",
			Toast.LENGTH_SHORT).show();
	    }
	});
    }
    
    

}
