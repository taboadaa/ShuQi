package phychips.arete.newver;

import java.util.ArrayList;

import phychips.arete.newver.R;

import com.phychips.rcp.RcpApi2;
import com.phychips.rcp.iRcpEvent2;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SessionActivity extends Activity implements iRcpEvent2
{
    private Button back;
    private Button done;
    private ListView outList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> array_list;
    private int mSession = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_session);

	array_list = new ArrayList<String>();

	for (int i = 0; i < 4; i++)
	{
	    array_list.add("S"+Integer.toString(i));
	}

	adapter = new ArrayAdapter<String>(this, R.layout.listview_cell1_checked,array_list);

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

	outList = (ListView) findViewById(R.id.type_listview1);
	outList.setAdapter(adapter);
	outList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	outList.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		mSession = arg2;		
		//RcpApi.setOutputPowerLevel((byte) ((byte) 0xC8 + (arg2 * 5)));
		//RcpApi.setOutputPowerLevel((byte) ((200 + (arg2 * 5)) & 0xff) );
	    }
	});	
	//outList.setItemChecked(mSession, true);
	adapter.notifyDataSetChanged();
	
	done = (Button) findViewById(R.id.power_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {		
		RcpApi2.getInstance().setSession(mSession);
		Toast.makeText(SessionActivity.this, "Reset Your Hardware.",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	RcpApi2.getInstance().setOnRcpEventListener(this);
	RcpApi2.getInstance().getSession();	
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
		Toast.makeText(SessionActivity.this, "Reset Your Hardware.",
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
		Toast.makeText(SessionActivity.this, "Error: Error Code = " + data[0],
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
    public void onTagMemoryReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onSessionReceived(final int session)
    {	
//	System.out.println("session = " + session);
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
	
		outList.setItemChecked(session, true);
	    }
	});
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
