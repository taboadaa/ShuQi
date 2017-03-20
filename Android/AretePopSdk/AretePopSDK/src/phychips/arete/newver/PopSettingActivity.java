package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.PopSettingAdapter;
import com.arete.custom.WeakRefHandler;
import com.arete.custom.inform;
import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.RcpFhLbtParam;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.ToggleButton;

public class PopSettingActivity extends Activity implements iRcpEvent,
	IOnHandlerMessage
{
    private ListView listView1;
    private ListView listView2;
    

    private Button back;

    private PopSettingAdapter adapter1;
    private PopSettingAdapter adapter2;
  

    private ArrayList<inform> list1;
    private ArrayList<inform> list2;
   

    private String beepState;
    @SuppressWarnings("unused")
    private int on_time, off_time, sense_time, lbt_rf_level, fh_enable,
	    lbt_enable, cw_enable;

    static int tx_minpower, tx_maxpower, powerLevel;

    private inform OutputPower, OnOffTime, StopConditions;
    private ToggleButton beepSet;

    @SuppressWarnings("unused")
    private int powerMin, powerMax;

    private Handler handler;

    static RcpFhLbtParam param;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_pop_setting);

	back = (Button) findViewById(R.id.popsetting_navigation_back_button);
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

	RcpApi.setRcpEvent(this);

	param = new RcpFhLbtParam();

	try
	{
	    RcpApi.getReaderInfo(0xB0);
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	listView1 = (ListView) findViewById(R.id.popsetting_listview1);
	listView2 = (ListView) findViewById(R.id.popsetting_listview2);
	

	list1 = new ArrayList<inform>();
	list2 = new ArrayList<inform>();
	

	OutputPower = new inform("Ouput Power", "detail");
	OnOffTime = new inform("On/Off Time(ms)", "detail");
	StopConditions = new inform("Stop Conditions", MainActivity.max_tag
		+ "/" + MainActivity.max_time + "/" + MainActivity.repeat_cycle);

	

	beepSet = (ToggleButton) findViewById(R.id.popsetting_toggle);

	beepSet.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		// TODO Auto-generated method stub
		if (isChecked)
		{
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
		else
		{
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
	    }
	});

	if (beepState == null)
	{
	    Toast.makeText(PopSettingActivity.this, "Need firmware update...",
		    Toast.LENGTH_SHORT);
	}
	// else if (beepState.equals("01"))
	// {
	// beepSet.setState(true);
	// }
	// else
	// {
	// beepSet.setState(false);
	// }

	adapter1 = new PopSettingAdapter(this, R.layout.popsettinglist, list1);
	adapter2 = new PopSettingAdapter(this, R.layout.popsettinglist, list2);
	

	listView1.setAdapter(adapter1);
	listView2.setAdapter(adapter2);
	

	list1.add(OutputPower);
	list1.add(OnOffTime);
	list2.add(StopConditions);
	

	adapter1.notifyDataSetChanged();
	adapter2.notifyDataSetChanged();
	

	handler = new WeakRefHandler(this);

    }

    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	try
	{
	    RcpApi.getReaderInfo(0xB0);
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    protected void onStart()
    {
	// TODO Auto-generated method stub
	super.onRestart();
	overridePendingTransition(R.anim.slide_out_right1,
		R.anim.slide_out_right2);
    }

    @Override
    public void onTagReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	try
	{
	    RcpApi.getCurrChannel();
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// TODO Auto-generated method stub
	String temp = RcpLib.int2str(data);
	// TODO Auto-generated method stub

	if (temp.length() == 40)
	{
	    beepState = temp.substring(2, 4);

	    on_time = Integer.parseInt(temp.substring(6, 10), 16);

	    off_time = Integer.parseInt(temp.substring(10, 14), 16);

	    sense_time = Integer.parseInt(temp.substring(14, 18), 16);

	    lbt_rf_level = Integer.parseInt(temp.substring(18, 22), 16);

	    fh_enable = Integer.parseInt(temp.substring(22, 24), 16);

	    lbt_enable = Integer.parseInt(temp.substring(24, 26), 16);

	    cw_enable = Integer.parseInt(temp.substring(26, 28), 16);

	    powerLevel = Integer.parseInt(temp.substring(28, 32), 16);

	    tx_minpower = Integer.parseInt(temp.substring(32, 36), 16);

	    tx_maxpower = Integer.parseInt(temp.substring(36, 40), 16);

	    handler.sendEmptyMessage(0);
	}
	else
	{

	    handler.sendEmptyMessage(1);

	}

	// powerMin = (Integer.parseInt(temp.substring(34, 38), 16) % 100) / 10;
	// powerMax = (Integer.parseInt(temp.substring(36, 38), 16) % 100) / 10;

	// L
	// tx_off_time L

	// temp.substring(14, 16); // tx_sense_time M
	// temp.substring(16, 18); // tx_sense_time L
	// temp.substring(18, 20); // rf_level M
	// temp.substring(20, 22); // rf_level L
	// temp.substring(22, 24); // fh_en
	// temp.substring(24, 26); // lbt_en
	// temp.substring(26, 28); // cw_en
    }

    @Override
    public void onRegionReceived(int[] data)
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
    public void onChannelReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onFhLbtReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTxPowerLevelReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTagMemoryReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onHoppingTableReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onModulationParamReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onAnticolParamReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTempReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onRssiReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onRegistryItemReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onResetReceived(int[] data)
    {

    }

    @Override
    public void onSuccessReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFailureReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticat(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onBeepStateReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTestFerPacketReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void handlerMessage(Message msg)
    {
	// TODO Auto-generated method stub
	switch (msg.what)
	{
	case 0:

	    param.readtime = on_time;
	    param.idletime = off_time;
	    param.sensetime = sense_time;
	    param.powerlevel = powerLevel;
	    param.fhmode = fh_enable;
	    param.lbtmode = lbt_enable;
	    param.cwmode = cw_enable;

	    list1.clear();
	    list2.clear();

	    OutputPower = new inform("Ouput Power", Integer
		    .toString(powerLevel).substring(0, 2)
		    + "."
		    + Integer.toString(powerLevel).substring(2));
	    OnOffTime = new inform("On/Off Time(ms)", Integer.toString(on_time)
		    + "/" + Integer.toString(off_time));
	    StopConditions = new inform("Stop Conditions", MainActivity.max_tag
			+ "/" + MainActivity.max_time + "/" + MainActivity.repeat_cycle);
	   

	    if (beepState.equals("01"))
	    {
		beepSet.setChecked(true);
	    }
	    else
	    {
		beepSet.setChecked(false);
	    }

	    list1.add(OutputPower);
	    list1.add(OnOffTime);
	    list2.add(StopConditions);
	    adapter1.notifyDataSetChanged();
	    adapter2.notifyDataSetChanged();

	    break;
	case 1:

	    Toast.makeText(PopSettingActivity.this, "Need firm ware Update",
		    Toast.LENGTH_SHORT).show();
	    break;

	case 2:

	    break;
	
	}

    }

    @Override
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

}
