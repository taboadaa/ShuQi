package phychips.arete.newver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import phychips.arete.newver.R;

import com.arete.custom.*;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public class MainActivity extends Activity implements iRcpEvent,
	IOnHandlerMessage
{
    public static final String KEY_ENCODING = "my_encoding";
    public static final String KEY_SAVELOG = "my_saveLog";
    public static final String KEY_flag = "my_message";

    public static boolean saveLog = false;
    public static int max_tag = 0, max_time = 0, repeat_cycle = 0,
	    encoding_type = 0;
    private String temp, test_msg;

    private int battery;

    private Handler m_Handler;
    private ToggleButton open;
    private Button stopAutoRead, startAutoRead, clearScreen, option;

    private ListView epclist;
    private TextView total, textView_headset, textView_battery;;

    private ArrayList<String> tagArray = new ArrayList<String>();
    private ArrayList<seqTag> seqArray = new ArrayList<seqTag>();

    private CustomListAdapter seqAdapter;

    @SuppressWarnings("unused")
    private boolean beepState, headsetConnected = false,
	    bUpdateRequired = false;

    @SuppressWarnings("unused")
    private int m_VolumeBackup;

    // private SharedPreferences pref;
    static private boolean m_bOnCreate = false;
    static boolean flag;

    HeadsetConnectionReceiver m_Headset = null;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);

	SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
	@SuppressWarnings("unused")
	SharedPreferences.Editor editor = prefs.edit();

	encoding_type = prefs.getInt(KEY_ENCODING, 0);
	saveLog = prefs.getBoolean(KEY_SAVELOG, false);
	flag = prefs.getBoolean(KEY_flag, false);

	if (!m_bOnCreate)
	{
	    startActivity(new Intent(this, SplashActivity.class));
	    m_bOnCreate = true;
	    m_Headset = new HeadsetConnectionReceiver();
	    registerReceiver(m_Headset, new IntentFilter(
		    Intent.ACTION_HEADSET_PLUG));
	}

	epclist = (ListView) findViewById(R.id.tag_list);

	total = (TextView) findViewById(R.id.name);
	textView_headset = (TextView) findViewById(R.id.aboutvalue);
	textView_battery = (TextView) findViewById(R.id.textView3);

	seqAdapter = new CustomListAdapter(this, R.layout.customlistview,
		seqArray);

	epclist.setAdapter(seqAdapter);

	// Open button
	open = (ToggleButton) findViewById(R.id.toggle_on);
	open.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		if (isChecked)
		{
		    try
		    {
			setVolumeMax();
			RcpApi.open();
			m_Handler.sendEmptyMessage(8);
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}

		else
		{
		    try
		    {
			RcpApi.close();
			if (headsetConnected)
			{
			    textView_headset.setText("Plugged");
			}
			else
			{
			    textView_headset.setText("Unplugged");
			}
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	});

	// On(toggle button) Clickalbe set
	if (headsetConnected)
	    open.setClickable(true);
	else
	    open.setClickable(false);

	// Read button
	startAutoRead = (Button) findViewById(R.id.btn_read);
	startAutoRead.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (RcpApi.isOpen)
		{
		    try
		    {
			RcpApi.startReadTags(max_tag, max_time, repeat_cycle);
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		else
		{
		    Toast.makeText(MainActivity.this, "Reader  not Opened",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	// Stop button
	stopAutoRead = (Button) findViewById(R.id.btn_stop);
	stopAutoRead.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (RcpApi.isOpen)
		{
		    try
		    {
			RcpApi.stopReadTags();
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    if (saveLog)
		    {
			startCapture();
		    }
		}
		else
		{
		    Toast.makeText(MainActivity.this, "Reader  not Opened",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	// Clear button
	clearScreen = (Button) findViewById(R.id.btn_clear);
	clearScreen.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		ListClear();
	    }
	});

	option = (Button) findViewById(R.id.option);
	option.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (RcpApi.isOpen)
		{
		    Intent intent = new Intent(MainActivity.this,
			    OptionActivity.class);
		    startActivity(intent);
		}
	    }
	});

	// test = (Button) findViewById(R.id.btn_test);
	// test.setOnClickListener(new OnClickListener()
	// {
	//
	// @Override
	// public void onClick(View v)
	// {
	// // TODO Auto-generated method stub
	// if (!RcpApi.isOpen)
	// {
	// if (getRequestedOrientation() ==
	// ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
	// {
	// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	// }
	// else if (getRequestedOrientation() ==
	// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
	// {
	// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
	// }
	// else if (getRequestedOrientation() ==
	// ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
	// {
	// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
	// }
	// else if (getRequestedOrientation() ==
	// ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
	// {
	// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	// }
	// }
	// }
	// });

	m_Handler = new WeakRefHandler(this);
    }

    // -------------------------------------------------------------end
    // MainActivity onCreate

    // Headset Connection
    public class HeadsetConnectionReceiver extends BroadcastReceiver
    {
	@Override
	public void onReceive(Context arg0, Intent intent)
	{
	    // TODO Auto-generated method stub
	    if (intent.hasExtra("state"))
	    {
		if (intent.getIntExtra("state", 0) == 0)
		{
		    m_Handler.sendEmptyMessage(5);
		}
		else if (intent.getIntExtra("state", 0) == 1)
		{
		    m_Handler.sendEmptyMessage(6);
		}
	    }
	}
    }

    public void startCapture()
    {
	long time = System.currentTimeMillis();
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(time);

	String fileName;

	fileName = "POPLog_"
		+ Build.MODEL
		+ "_"
		+ Integer.toString(c.get(Calendar.YEAR))
		+ Integer.toString(c.get(Calendar.MONTH) + 101).substring(1)
		+ Integer.toString(c.get(Calendar.DATE) + 100).substring(1)
		+ Integer.toString(c.get(Calendar.HOUR_OF_DAY) + 100)
			.substring(1)
		+ Integer.toString(c.get(Calendar.MINUTE) + 100).substring(1)
		+ Integer.toString(c.get(Calendar.SECOND) + 100).substring(1)
		+ ".csv";

	this.writeFile(fileName);
    }

    private FileOutputStream m_fos = null;

    private void writeFile(String filename)
    {

	try
	{
	    File file = new File(Environment.getExternalStorageDirectory()
		    .getPath() + "/ARETE/", filename);
	    m_fos = new FileOutputStream(file);
	    DataOutputStream ostream = new DataOutputStream(m_fos);
	    for (int i = 0; i < seqArray.size(); i++)
	    {
		String temp_tag = seqArray.get(i).getTag();
		String temp_count = seqArray.get(i).getCount();

		try
		{
		    ostream.writeBytes(temp_tag + ",");
		    ostream.writeBytes(temp_count + "\n");
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    m_fos.flush();
	    ostream.flush();
	    m_fos.close();
	    ostream.close();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public static Intent createIntent(Context context)
    {
	Intent i = new Intent(context, MainActivity.class);
	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	return i;
    }

    // private void createShareIntent()
    // {
    //
    // if (RcpApi.isOpen)
    // {
    //
    // Intent intent = new Intent(MainActivity.this, OptionActivity.class);
    // startActivity(intent);
    // }
    // else
    // {
    // Toast.makeText(MainActivity.this, "Not Opend", Toast.LENGTH_SHORT)
    // .show();
    // }
    // }
    // list clear - adapter clear and array clear
    
    private void ListClear()
    {
	seqArray.clear();
	total.setText(" 0  tags");
	tagArray.clear();

	epclist.setAdapter(seqAdapter);
	bUpdateRequired = false;
    }

    // list refresh - 0(seqAdapter // tag,count,num custom list ),messagedelay
    // 60
    // - 4(totaltag), message delay 1000
    private void ListRefresh(String str)
    {
	// EPC delete(4)
	// str = str.substring(4);

	if (!tagArray.contains(str))
	{
	    seqTag ttag = new seqTag();
	    tagArray.add(str);
	    
	    if (encoding_type == 0)
	    {
		ttag.setTag(str);
	    }
	    
	    else
	    {
		ttag.setTag(new String(RcpLib.convertStringToByteArray(str)));
	    }

	    ttag.setCount("1");
	    seqArray.add(ttag);

	    if (!bUpdateRequired)
	    {
		m_Handler.sendEmptyMessageDelayed(0, 80);
		bUpdateRequired = true;
	    }
	}

	else
	{
	    int currCount = Integer.parseInt((seqArray.get(tagArray
		    .indexOf(str)).getCount()));
	    seqArray.get(tagArray.indexOf(str)).setCount(
		    Integer.toString(currCount + 1));

	    if (!bUpdateRequired)
	    {
		m_Handler.sendEmptyMessageDelayed(0, 80);
		bUpdateRequired = true;
	    }
	}
	
	m_Handler.sendEmptyMessageDelayed(4, 1000);
    }

    // Resume setRcpEvent- mainActivity
    // RcpOpen check

    @Override
    protected void onResume()
    {
	super.onResume();
	RcpApi.setRcpEvent(this);
	if (RcpApi.isOpen)
	{
	    m_Handler.sendEmptyMessage(8);
	}
    }

    // Stop - not need
    @Override
    protected void onStop()
    {
	super.onStop();
	overridePendingTransition(R.anim.slide_out_left1,
		R.anim.slide_out_left1);
	SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
	SharedPreferences.Editor editor = prefs.edit();
	editor.putInt(KEY_ENCODING, encoding_type);
	editor.putBoolean(KEY_SAVELOG, saveLog);
	editor.putBoolean(KEY_flag, flag);
	editor.commit();

    }

    @Override
    protected void onPause()
    {
	super.onPause();

    }

    @Override
    public void setRequestedOrientation(int requestedOrientation)
    {
	// TODO Auto-generated method stub
	super.setRequestedOrientation(requestedOrientation);
    }

    // Destroy - clearCache
    @Override
    protected void onDestroy()
    {

	if (isFinishing())
	{
	    try
	    {
		if (RcpApi.isOpen == true)
		{
		    RcpApi.close();
		}

		if (m_Headset != null)
		{
		    unregisterReceiver(m_Headset);
		    m_Headset = null;
		}

		m_bOnCreate = false;

	    }
	    catch (Exception e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	// pref.edit().putBoolean("flag", flag);
	// pref.edit().commit();
	super.onDestroy();
    }

    // volumax now Voulume backup and volume Max
    private void setVolumeMax()
    {

	AudioManager AudioManager = (AudioManager) getApplicationContext()
		.getSystemService(Context.AUDIO_SERVICE);

	m_VolumeBackup = AudioManager
		.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);

	AudioManager
		.setStreamVolume(
			android.media.AudioManager.STREAM_MUSIC,
			AudioManager
				.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC),
			1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	switch (keyCode)
	{
	case KeyEvent.KEYCODE_VOLUME_DOWN:
	case KeyEvent.KEYCODE_VOLUME_UP:
	    break;
	default:
	    break;
	}
	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTagReceived(int[] data)
    {
	ListRefresh(RcpLib.int2str(data));
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
    public void onSuccessReceived(int[] data)
    {
	// TODO Auto-generated method stub
	m_Handler.sendEmptyMessage(1);
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	// TODO Auto-generated method stub
	temp = RcpLib.int2str(data);

	if (temp.substring(2, 4).equals("01"))
	{
	    beepState = true;
	}
	else
	{
	    beepState = false;
	}

	// powerLevel = (Integer.parseInt(temp.substring(4, 6), 16) % 100) / 10;
	// // tx

	// if(powerLevel == 20){
	// spiner.setSelection(5);
	// }
	// else{
	//
	// spiner.setSelection(5-(powerLevel) / 5);
	// }
	//
	// on_time = Integer.parseInt(temp.substring(6, 10),16); // tx_on_time M
	// + tx_on_time L
	//
	// off_time = Integer.parseInt(temp.substring(10, 14),16); //
	// tx_off_time M + tx_off_time L
    }

    @Override
    public void onFailureReceived(int[] data)
    {
	// TODO Auto-generated method stub
	test_msg = RcpLib.int2str(data);
	m_Handler.sendEmptyMessage(2);
    }

    @Override
    public void onResetReceived(int[] data)
    {
	// TODO Auto-generated method stub
	m_Handler.sendEmptyMessage(3);
    }

    @Override
    public void onAuthenticat(int[] arg0)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onBeepStateReceived(int[] arg0)
    {
	// TODO Auto-generated method stub
    }

    //
    @Override
    public void handlerMessage(Message msg)
    {
	// TODO Auto-generated method stub
	switch (msg.what)
	{
	case 0:
	    // epclist.setVisibility(1);
	    seqAdapter.notifyDataSetChanged();
	    // epclist.setVisibility(0);
	    bUpdateRequired = false;
	    break;

	case 1:
	    if (flag)
	    {
		Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT)
			.show();
	    }
	    break;

	case 2:
	    Toast.makeText(MainActivity.this, "Error code  :  " + test_msg,
		    Toast.LENGTH_SHORT).show();
	    if (flag)
	    {
		Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT)
			.show();
	    }
	    break;

	case 3:
	    if (flag)
	    {
		Toast.makeText(MainActivity.this, "Reader Opened",
			Toast.LENGTH_SHORT).show();
	    }
	    break;

	case 4:
	    total.setText(tagArray.size() + " tags");
	    bUpdateRequired = false;
	    break;

	case 5:
	    headsetConnected = false;
	    open.setChecked(false);
	    open.setClickable(false);
	    textView_headset.setText("Unplugged");

	    Toast.makeText(MainActivity.this, "Headset plug out",
		    Toast.LENGTH_SHORT).show();
	    break;

	case 6:
	    headsetConnected = true;
	    open.setClickable(true);
	    textView_headset.setText("Plugged");

	    Toast.makeText(MainActivity.this, "Headset plug in",
		    Toast.LENGTH_SHORT).show();
	    break;

	case 7:
	    textView_battery.setText(Integer.toString(battery) + "%");
	    textView_headset.setText("Connected");
	    break;

	case 8:
	    try
	    {
		RcpApi.getReaderInfo((byte) 0xB0);
	    }
	    catch (RcpException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    break;

	case 9:
	    break;

	default:
	    break;
	} // End switch

    }

    @Override
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	temp = RcpLib.int2str(dest);

	String now = temp.substring(0, 2);
	String min = temp.substring(2, 4);
	String max = temp.substring(4, 6);

	if ((Integer.parseInt(max, 16) - Integer.parseInt(min, 16)) != 0)
	{
	    battery = (Integer.parseInt(now, 16) - Integer.parseInt(min, 16))
		    * 100
		    / (Integer.parseInt(max, 16) - Integer.parseInt(min, 16));
	}
	else
	{
	    battery = 0;
	}

	// clamping 0 <= battery <= 100
	if (battery > 100)
	{
	    battery = 100;
	}

	if (battery < 0)
	{
	    battery = 0;
	}
	m_Handler.sendEmptyMessage(7);
    }

    @Override
    public void onTestFerPacketReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

}
