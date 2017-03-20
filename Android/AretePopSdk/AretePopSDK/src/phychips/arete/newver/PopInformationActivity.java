package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.TestThread;
import com.arete.custom.TextTextListAdapter;
import com.arete.custom.WeakRefHandler;
import com.arete.custom.inform;
import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class PopInformationActivity extends Activity implements iRcpEvent,
	IOnHandlerMessage
{
    private String temp, region, now, min, max, model, serial, firmware;
    private StringBuffer sb;

    private Handler m_Handler;
    private TestThread th;

    private Button diagnostic, back;
    private TextView percent;
    private ProgressBar progress;

    private int battery_per;

    private TextTextListAdapter adapter;
    private ListView listView;
    private ArrayList<inform> informList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_pop_information);

	percent = (TextView) findViewById(R.id.percent);
	progress = (ProgressBar) findViewById(R.id.progressBar1);

	RcpApi.setRcpEvent(PopInformationActivity.this);

	sb = new StringBuffer();

	listView = (ListView) findViewById(R.id.about_list);
	informList = new ArrayList<inform>();
	adapter = new TextTextListAdapter(this, R.layout.aboutlist, informList);
	listView.setAdapter(adapter);

	try
	{
	    RcpApi.getReaderInfo(0xB1);
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	back = (Button) findViewById(R.id.about_navigation_back_button);
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

	diagnostic = (Button) findViewById(R.id.Diagnostic);
	diagnostic.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		th = new TestThread(m_Handler, sb);
		th.start();
		progress.setVisibility(View.VISIBLE);
	    }
	});
	m_Handler = new WeakRefHandler(this);
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
	// TODO Auto-generated method stub
	temp = RcpLib.int2str(data);
	System.out.println("data  >>>>>>>" + temp);
	if (temp.substring(0, 2).equalsIgnoreCase("B1"))
	{
	    m_Handler.sendEmptyMessage(1);
	}
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
	// TODO Auto-generated method stub

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
	    progress.setVisibility(View.INVISIBLE);
	    break;
	case 1:
	    region = temp.substring(2, 4);
	    now = temp.substring(4, 6);
	    min = temp.substring(6, 8);
	    max = temp.substring(8, 10);
	    model = temp.substring(10, 30);
	    serial = temp.substring(30, 50);
	    firmware = temp.substring(50, 58);
	    sb.append(parseRegion(Integer.parseInt(region)) + "\n");

	    if (Integer.parseInt(max, 16) - Integer.parseInt(min, 16) != 0)
	    {
		battery_per = (Integer.parseInt(now, 16) - Integer.parseInt(
			min, 16))
			* 100
			/ (Integer.parseInt(max, 16) - Integer
				.parseInt(min, 16));
	    }
	    else
	    {
		battery_per = 0;
	    }

	    if (battery_per > 100)
		battery_per = 100;

	    if (battery_per < 0)
		battery_per = 0;
	    inform a = new inform();
	    a.setName("Region");
	    a.setValue(parseRegion(Integer.parseInt(region)));

	    inform b = new inform();
	    b.setName("PID");
	    b.setValue(new String(RcpLib.convertStringToByteArray(serial)));

	    inform c = new inform();
	    c.setName("Model");
	    c.setValue(new String(RcpLib.convertStringToByteArray(model)));

	    inform d = new inform();
	    d.setName("Battery");
	    d.setValue(Integer.toString(battery_per) + "%");

	    inform e = new inform();
	    e.setName("FID");
	    e.setValue(firmware);

	    inform f = new inform();
	    f.setName("App Version");
	    String version = "0.0";

	    PackageInfo pInfo;
	    try
	    {
		pInfo = this.getPackageManager().getPackageInfo(
			this.getPackageName(), 0);
		version = pInfo.versionName;
	    }
	    catch (NameNotFoundException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    f.setValue(version);

	    informList.add(f);
	    informList.add(c);
	    informList.add(b);
	    informList.add(e);
	    informList.add(a);
	    informList.add(d);

	    sb.append(new String(RcpLib.convertStringToByteArray(serial))
		    + "\n");
	    sb.append(new String(RcpLib.convertStringToByteArray(model)) + "\n");
	    sb.append(firmware + "\n");
	    adapter.notifyDataSetChanged();
	    break;

	case 7:
	    percent.setText(Integer.toString(msg.arg1) + "%");
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
	case 1:
	case 11:
	    name = "KOREA";
	    break;
	case 2:
	case 21:
	case 22:
	case 32:
	    name = "USA";
	    break;
	case 4:
	case 31:
	    name = "EU";
	    break;
	case 8:
	case 41:
	    name = "JAPAN";
	    break;
	case 10:
	case 16:
	case 51:
	case 52:
	    name = "CHINA";
	    break;
	default:
	    name = "NONE";
	    break;
	}

	return name;
    }

    @Override
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

}
