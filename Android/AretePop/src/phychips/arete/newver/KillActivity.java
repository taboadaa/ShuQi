package phychips.arete.newver;

import com.phychips.rcp.RcpApi2;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent2;
import phychips.arete.newver.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KillActivity extends Activity implements iRcpEvent2
{
    public Byte resultCode = 0x00;
    
    private Button back;
    private Button done;
    
    private TextView kill_targetTag, kill_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_kill);
	RcpApi2.getInstance().setOnRcpEventListener(this);

	kill_targetTag = (TextView) findViewById(R.id.kill_targetTag);
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
		if (RcpApi2.getInstance().isOpen())
		{
		    try
		    {
			String epc = kill_targetTag.getText().toString();


			RcpApi2.getInstance().killTag(
				Long.parseLong(kill_password.getText()
					.toString(), 16),
				RcpLib.convertStringToByteArray(epc));
		    }
		    catch (Exception e)
		    {
			Toast.makeText(KillActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
		    }
		}
		else
		{
		    Toast.makeText(KillActivity.this, "Kill Tag successs",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
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
    public void onSuccessReceived(int[] data, int code)
    {
	resultCode = 0x00;
	
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		Toast.makeText(KillActivity.this, "Success",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	resultCode = (byte)(data[0] & 0xFF);
	
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		//Toast.makeText(KillActivity.this, "Error: Error Code = " + data[0],
		//	Toast.LENGTH_LONG).show();
		
		Toast.makeText(KillActivity.this, "Error: Error Code = 0x" + RcpLib.byte2string((byte)(data[0]&0xff)),
			Toast.LENGTH_LONG).show();
	    }
	});
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
    public void onBatteryStateReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onSessionReceived(int channel)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTagWithTidReceived(int[] pcEpc,int[] Tid)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onGenericTransportReceived(int arg0, int[] arg1)
    {
	// TODO Auto-generated method stub
	
    }

}
