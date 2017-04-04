package phychips.arete.newver;


import com.phychips.rcp.iRcpEvent2;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import phychips.arete.newver.R;

public class StopCondisionsActivity extends Activity implements iRcpEvent2
{
    private Button back, done;

    private EditText text1, text2, text3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_stop_condisions);

	final SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(getBaseContext());
	    
	text1 = (EditText) findViewById(R.id.stop_editText1);
	text1.setText(Integer.toString(prefs.getInt("MAX_TAG", 0)));
	text2 = (EditText) findViewById(R.id.stop_editText2);
	text2.setText(Integer.toString(prefs.getInt("MAX_TIME", 0)));
	text3 = (EditText) findViewById(R.id.stop_editText3);
	text3.setText(Integer.toString(prefs.getInt("REPEAT_CYCLE", 0)));

	back = (Button) findViewById(R.id.stopconditions_navigation_back_button);
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

	done = (Button) findViewById(R.id.stop_done_btn);
	done.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {

		int max_tag = 0;
		int max_time = 0;
		int repeat_cycle = 0;

		if (isNumber(text1.getText().toString()))
		{

		    max_tag = Integer.parseInt(text1.getText()
			    .toString());

		    if (isNumber(text2.getText().toString()))
		    {
			max_time = Integer.parseInt(text2
				.getText().toString());
			if (isNumber(text3.getText().toString()))
			{
			    repeat_cycle = Integer.parseInt(text3
				    .getText().toString());
			    
			    //SharedPreferences prefs = PreferenceManager
				//	.getDefaultSharedPreferences(getBaseContext());
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putInt("MAX_TAG", max_tag);
			    editor.putInt("MAX_TIME", max_time);
			    editor.putInt("REPEAT_CYCLE", repeat_cycle);			    
			    editor.commit();
			    
			    Toast.makeText(StopCondisionsActivity.this,
				    "success", Toast.LENGTH_SHORT).show();
			}
			else
			{
			    Toast.makeText(StopCondisionsActivity.this,
				    "Error: Only integers allowed", Toast.LENGTH_SHORT).show();
			}
		    }
		    else
		    {
			Toast.makeText(StopCondisionsActivity.this, "Error: Only integers allowed",
				Toast.LENGTH_SHORT).show();
		    }

		}
		else
		{
		    Toast.makeText(StopCondisionsActivity.this, "Error: Only integers allowed",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    public static boolean isNumber(String str)
    {
	boolean check = true;
	
	if (!str.isEmpty())
	{
	    for (int i = 0; i < str.length(); i++)
	    {
		if (!Character.isDigit(str.charAt(i)))
		{
		    check = false;
		    break;
		}
	    }
	    return check;
	}
	else
	{
	    check = false;
	    return check;
	}
    }

    @Override
    public void onTagReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onRegionReceived(int region)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onSelectParamReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onQueryParamReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onChannelReceived(int channel, int channelOffset)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onFhLbtReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTxPowerLevelReceived(int power)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTagMemoryReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onResetReceived()
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onSuccessReceived(int[] data, final int commandCode)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		Toast.makeText(StopCondisionsActivity.this, "Success",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		Toast.makeText(StopCondisionsActivity.this, "Error: Error Code = " + data[0],
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onBatteryStateReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTagMemoryLongReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTagWithRssiReceived(int[] data, int rssi)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onSessionReceived(int channel)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTagWithTidReceived(final int[] pcEpc, final int[] tid)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onGenericTransportReceived(int arg0, int[] arg1)
    {
	// TODO Auto-generated method stub
	
    }

}
