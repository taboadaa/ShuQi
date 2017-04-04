package phychips.arete.newver;

import com.phychips.rcp.RcpApi2;
import com.phychips.rcp.RcpFhLbtParam;
import com.phychips.rcp.iRcpEvent2;

import phychips.arete.newver.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

public class RfSettingsActivity extends Activity implements iRcpEvent2
{
    Button back, done;
    EditText etChannel;
    RcpFhLbtParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_rf_setting);
	
	etChannel = (EditText) findViewById(R.id.channel);
	
	back = (Button) findViewById(R.id.navigation_back_button);
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

	done = (Button) findViewById(R.id.done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		int channel = Integer.parseInt(etChannel.getText()
	                			.toString());
		
		RcpApi2.getInstance().setChannel(channel, 0);
	    }
	});
    }
    
    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	RcpApi2.getInstance().setOnRcpEventListener(this);
	RcpApi2.getInstance().getChannel();
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
    public void onChannelReceived(final int channel, final int channelOffset)
    {
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {
		etChannel.setText(Integer.toString(channel));
	    }
	});
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
    public void onSuccessReceived(int[] data, int code)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		Toast.makeText(RfSettingsActivity.this, "Success",
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
		Toast.makeText(RfSettingsActivity.this, "Error: Error Code = " + data[0],
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
