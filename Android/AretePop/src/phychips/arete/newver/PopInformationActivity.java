package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.ProgressDialogHelper;
import com.arete.custom.TestThread;
import com.arete.custom.TextTextListAdapter;
import com.arete.custom.WeakRefHandler;
import com.arete.custom.customCell;
import com.phychips.rcp.RcpApi2;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class PopInformationActivity extends Activity implements iRcpEvent2,
	IOnHandlerMessage
{
    private String model, serial, firmware;
    private StringBuffer sb;
    private Handler m_Handler;
    private TestThread th;
    private Button diagnostic, back;
    private int battery, region;

    private TextTextListAdapter adapter;
    private ListView listView;
    private ArrayList<customCell> informList;
    private int timerError = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	sb = new StringBuffer();

	setContentView(R.layout.activity_pop_information);
	listView = (ListView) findViewById(R.id.about_list);
	informList = new ArrayList<customCell>();
	adapter = new TextTextListAdapter(this, R.layout.aboutlist, informList);
	listView.setAdapter(adapter);
	listView.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		if(arg2 == 5)
		{
		    // 1. Instantiate an AlertDialog.Builder with its constructor
		    AlertDialog.Builder builder 
		    = new AlertDialog.Builder(PopInformationActivity.this);

		    // 2. Chain together various setter methods to set the dialog characteristics		    
		    builder.setTitle("Battery Calibration")
		           .setMessage("This process will delete battery information"
		        	   + " from your ARETE POP. 1. After click YES, fully"
		        	   + " charge your ARETE POP until the red LED turn off."
		        	   + " 2. Disconnect the charger and use your ARETE POP." 
		        	   + " 3.When your battery gets low, fully charged again.");

		 // Add the buttons
		    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) 
		               {
		                   // User clicked OK button
		        	   RcpApi2.getInstance().calGpAdc(255,0);
		               }
		           });
		    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) 
		               {
		                   // User cancelled the dialog
		               }
		           });
		    
		    // 3. Get the AlertDialog from create()
		    AlertDialog dialog = builder.create();
		    dialog.show();
		}
	    }	    
	});

	RcpApi2.getInstance()
		.setOnRcpEventListener(PopInformationActivity.this);

	back = (Button) findViewById(R.id.about_navigation_back_button);
	back.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		moveTaskToBack(false);
		finish();
	    }
	});

	diagnostic = (Button) findViewById(R.id.Diagnostic);
	diagnostic.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		th = new TestThread(m_Handler, sb);
		th.start();

		ProgressDialogHelper.getInstance().showProgressDialog(
			PopInformationActivity.this, "Wait..", "0%");
	    }
	});
	m_Handler = new WeakRefHandler(this);

	RcpApi2.getInstance().getReaderInfo(0xB1);
	timerError = 0;
	m_Handler.sendEmptyMessageDelayed(-1, 1000);
    }

    @Override
    protected void onStart()
    {
	super.onRestart();
	overridePendingTransition(R.anim.slide_out_right1,
		R.anim.slide_out_right2);
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
	m_Handler.removeMessages(-1);
    }

    @Override
    public void onTagReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	if (data.length > 1 && (data[0] & 0xff) == 0xB1)
	{
	    String temp = RcpLib.int2str(data);

	    region = (int) (data[1] & 0xff);
	    int adc = (int) (data[2] & 0xff);
	    int adcMin = (int) (data[3] & 0xff);
	    int adcMax = (int) (data[4] & 0xff);

	    model = temp.substring(10, 30);
	    serial = temp.substring(30, 50);
	    firmware = temp.substring(50, 58);

	    sb.append(parseRegion(region) + "\n");

	    if (adcMin == adcMax)
	    {
		adcMax += 1;
	    }

	    battery = (adc - adcMin) * 100 / (adcMax - adcMin) + 12;
	    battery /= 25;
	    battery *= 25;

	    if (battery > 100)
		battery = 100;
	    else if (battery < 0)
		battery = 0;

	    PackageInfo pInfo = null;

	    try
	    {
		pInfo = this.getPackageManager().getPackageInfo(
			this.getPackageName(), 0);

	    }
	    catch (NameNotFoundException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    final String version = pInfo.versionName;

	    runOnUiThread(new Runnable()
	    {
		public void run()
		{
		    customCell a = new customCell();
		    a.setName("Region");
		    a.setValue(parseRegion(region));

		    customCell b = new customCell();
		    b.setName("PID");
		    b.setValue(new String(RcpLib
			    .convertStringToByteArray(serial)));

		    customCell c = new customCell();
		    c.setName("Model");
		    c.setValue(new String(RcpLib
			    .convertStringToByteArray(model)));

		    customCell d = new customCell();
		    d.setName("Battery");
		    d.setValue(Integer.toString(battery) + "%");

		    customCell e = new customCell();
		    e.setName("FID");
		    e.setValue(firmware);

		    customCell f = new customCell();
		    f.setName("App Version");

		    f.setValue(version);

		    informList.add(f);
		    informList.add(c);
		    informList.add(b);
		    informList.add(e);
		    informList.add(a);
		    informList.add(d);

		    sb.append(new String(RcpLib
			    .convertStringToByteArray(serial)) + "\n");
		    sb.append(new String(RcpLib.convertStringToByteArray(model))
			    + "\n");
		    sb.append(firmware + "\n");

		    adapter.notifyDataSetChanged();
		}
	    });

	    // break;
	}
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
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		Toast.makeText(PopInformationActivity.this, "Success",
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
		Toast.makeText(PopInformationActivity.this,
			"Error: Error Code = " + data[0], Toast.LENGTH_LONG)
			.show();
	    }
	});
    }

    @Override
    public void handlerMessage(Message msg)
    {
	// TODO Auto-generated method stub
	switch (msg.what)
	{
	case 0:
	    ProgressDialogHelper.getInstance().hideProgressDialog();
	    break;
	case 7:
	    ProgressDialogHelper.getInstance().setMessage(
		    Integer.toString(msg.arg1) + "%");
	    break;
	case -1:
	    // System.out.println("timer - 1");
	    if (this.informList.size() == 0 && timerError < 5)
	    {
		RcpApi2.getInstance().getReaderInfo(0xB1);
		m_Handler.sendEmptyMessageDelayed(-1, 1000);
		timerError++;
	    }
	    break;

	default:
	    break;
	} // End switch
    }

    public String parseRegion(int region)
    {
	String name;
	switch (region)
	{
	case 0x01:
	case 0x11:
	    name = "KOREA";
	    break;
	case 0x02:
	case 0x21:
	case 0x22:
	case 0x32:
	    name = "USA";
	    break;
	case 0x04:
	case 0x31:
	    name = "EU";
	    break;
	case 0x08:
	case 0x41:
	    name = "JAPAN";
	    break;
	case 0x10:
	case 0x16:
	case 0x51:
	case 0x52:
	    name = "CHINA";
	    break;
	case 0x71:
	    name = "ASIA";
	    break;
	default:
	    name = "NONE";
	    break;
	}

	return name;
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
