package phychips.arete.newver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import com.arete.custom.*;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.phychips.rcp.*;
import com.phychips.utility.EpcConverter;

import phychips.arete.newver.application.TagView;

public class MainActivity extends Activity implements iRcpEvent2,
	OnCompletionListener, IOnHandlerMessage
{
    static private boolean m_bOnCreate = false;

    public int encoding_type = EpcConverter.HEX_STRING;
    private boolean displayRssi = false;
    private boolean speakerBeep = false;
    private boolean readAfterPlugging = false;
    public int max_tag = 0;
    public int max_time = 0;
    public int repeat_cycle = 0;
    
    private Handler m_Handler;
    private String test_msg;
    private int battery;
    private ToggleButton open;
    private Button stopAutoRead, startAutoRead, clearScreen, option;
    public ListView epclist;
    public ListView epclist_empty;
    private TextView tvTagCount, tvHeadsetStatus, tvBattery;
    public ArrayList<customCell> tagCellList = new ArrayList<customCell>();
	public ArrayList<customCell> tagCellListBed = new ArrayList<customCell>();
	public ArrayList<String> tagStringList = new ArrayList<String>();
    public ArrayList<int[]> tagDataList = new ArrayList<int[]>();
    public ArrayList<customCell> tagArrayEmpty = new ArrayList<customCell>();
    private CustomListViewAdapter tagAdaterEmpty;
    private CustomListViewAdapter tagAdapter;

    private MediaPlayer mp;
    private IntentBroadcastReceiver mIntentReceiver = null;
    private AudioManager mAudioManager = null;
    private SharedPreferences mPrefs = null;
    private Vibrator vibe;

	//Création fichier clé-valeur
	Map<String, ?> weekTags;
	SharedPreferences sharedPref;
    Calendar rightNow = Calendar.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	if (!m_bOnCreate)
	{
	    m_bOnCreate = true;
	    startActivity(new Intent(this, SplashActivity.class));
	    usrRegisterIntent();
	}

	Intent intent = getIntent();// check if this intent is started via
				    // custom scheme link
	if (Intent.ACTION_VIEW.equals(intent.getAction()))
	{
	    Uri uri = intent.getData();
	    // may be some test here with your custom uriString
	    String cbUrl = uri.getQueryParameter("cburl");
	    String cbParam = uri.getQueryParameter("cbparam");

	    System.out.println("0. cbUrl = " + cbUrl);
	    System.out.println("0. cbParam = " + cbParam);
	}

	epclist = (ListView) findViewById(R.id.tag_list);

	tvTagCount = (TextView) findViewById(R.id.name);
	tvHeadsetStatus = (TextView) findViewById(R.id.aboutvalue);
	tvBattery = (TextView) findViewById(R.id.textView3);
	tagAdapter = new CustomListViewAdapter(this,
		R.layout.listview_cell2_disclosure_taglist, tagCellList);
	epclist.setAdapter(tagAdapter);
	epclist.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		RcpApi2.getInstance().stopReadTags();

		if (encoding_type == EpcConverter.HEX_STRING)
		{
		    Intent intent = new Intent(getBaseContext(),
			    TagView.class);
		    intent.putExtra("myTag",
			    ((customCell)(tagCellList.toArray()[arg2])).getId());
		    startActivity(intent);
		}
		else
		{
		    Toast.makeText(MainActivity.this,
			    "Not possible when encoding type is not HEX.",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	epclist_empty = (ListView) findViewById(R.id.tag_list_empty);
	tagAdaterEmpty = new CustomListViewAdapter(this,
		R.layout.listview_cell2_disclosure_taglist_empty, tagArrayEmpty);
	for (int i = 0; i < 20; i++)
	{
	    tagAdaterEmpty.add(new customCell());
	}
	epclist_empty.setAdapter(tagAdaterEmpty);
	epclist_empty.setEnabled(false);

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
			RcpApi2.getInstance().open();
			setVolumeMax();
			// m_Handler.sendEmptyMessage(8);
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    startAutoRead.setEnabled(true);
		    clearScreen.setEnabled(true);
		    stopAutoRead.setEnabled(true);
		    option.setEnabled(true);
		}
		else
		{
		    try
		    {
			RcpApi2.getInstance().close();
			if (mAudioManager.isWiredHeadsetOn())
			{
			    tvHeadsetStatus.setText("Plugged");
			}
			else
			{
			    tvHeadsetStatus.setText("Unplugged");
			}
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    startAutoRead.setEnabled(false);
		    clearScreen.setEnabled(false);
		    stopAutoRead.setEnabled(false);
		    option.setEnabled(false);
		}
	    }
	});

	// On(toggle button) Clickalbe set
	if (mAudioManager.isWiredHeadsetOn())
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
		if (RcpApi2.getInstance().isOpen())
		{
		    if (speakerBeep)
		    {
			max_tag = 1;
		    }
		    else
		    {
			max_tag = mPrefs.getInt("MAX_TAG", 0);
		    }

		    if (displayRssi)
		    {
			RcpApi2.getInstance().startReadTagsWithRssi(max_tag,
				max_time, repeat_cycle);
		    }
		    else
		    {
//			RcpApi2.getInstance().startReadTagsWithTid(max_tag, max_time,repeat_cycle);
			RcpApi2.getInstance().startReadTags(max_tag, max_time,repeat_cycle);
		    }
		}
		else
		{
		    Toast.makeText(MainActivity.this, "Not opened yet",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
	startAutoRead.setEnabled(false);

	// Stop button
	stopAutoRead = (Button) findViewById(R.id.btn_stop);
	stopAutoRead.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (RcpApi2.getInstance().isOpen())
		{
		    RcpApi2.getInstance().stopReadTags();
		}
		else
		{
		    Toast.makeText(MainActivity.this,
			    "Failure to initialize audio port",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
	stopAutoRead.setEnabled(false);

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
	clearScreen.setEnabled(false);

	option = (Button) findViewById(R.id.option);
	option.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (RcpApi2.getInstance().isOpen())
		{
		    RcpApi2.getInstance().stopReadTags();

		    Intent intent = new Intent(getBaseContext(),
			    OptionActivity.class);
		    intent.putExtra("TAG_LIST", tagStringList);
		    startActivity(intent);
		}
	    }
	});
	option.setEnabled(false);

	TelephonyManager mTelMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	if (mTelMgr != null && mTelMgr.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE)
	{
	    mTelMgr.listen(new PhoneStateListener()
	    {
		public void onCallStateChanged(int state, String incomingNumber)
		{
		    switch (state)
		    {
		    case TelephonyManager.CALL_STATE_OFFHOOK:
		    case TelephonyManager.CALL_STATE_RINGING:
			try
			{
			    RcpApi2.getInstance().close();
			    open.setChecked(false);
			    System.out.println("calling stop............");
			}
			catch (Exception e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			break;
		    }
		}
	    }, PhoneStateListener.LISTEN_CALL_STATE);
	}

	mp = MediaPlayer.create(getApplicationContext(), R.raw.read);
	mp.setOnCompletionListener(this);

	m_Handler = new WeakRefHandler(this);
	
	vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
	super.onNewIntent(intent);
	// custom scheme link
	if (Intent.ACTION_VIEW.equals(intent.getAction()))
	{
	    // Uri uri = intent.getData();
	    // may be some test here with your custom uriString
	    // String cbUrl = uri.getQueryParameter("cburl");
	    // String cbParam = uri.getQueryParameter("cbparam");
	    //
	    // System.out.println("1. cbUrl = " + cbUrl);
	    // System.out.println("1. cbParam = " + cbParam);
	}
    }

    private void usrRegisterIntent()
    {
	usrUnRegisterIntent();

	mIntentReceiver = new IntentBroadcastReceiver();
	IntentFilter filter = new IntentFilter();
	filter.addAction(Intent.ACTION_SCREEN_OFF);
	filter.addAction(Intent.ACTION_HEADSET_PLUG);
	registerReceiver(mIntentReceiver, filter);
    }

    private void usrUnRegisterIntent()
    {
	if (mIntentReceiver != null)
	{
	    unregisterReceiver(mIntentReceiver);
	    mIntentReceiver = null;
	}
    }

    // -------------------------------------------------------------end
    // MainActivity onCreate

    // Headset Connection
    public class IntentBroadcastReceiver extends BroadcastReceiver
    {
	@Override
	public void onReceive(Context arg0, Intent intent)
	{
	    System.out.println(intent.toString());

	    if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF))
	    {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
			PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, "POWER_ON");
		wl.setReferenceCounted(true);
		wl.acquire();
		wl.release();
	    }
	    else if (intent.getAction().equalsIgnoreCase(
		    Intent.ACTION_HEADSET_PLUG))
	    {
		if (intent.hasExtra("state"))
		{
		    if (intent.getIntExtra("state", 0) == 0)
		    {
			runOnUiThread(new Runnable()
			{
			    public void run()
			    {
				// m_Handler.sendEmptyMessage(5);
				// headsetConnected = false;
				open.setChecked(false);
				open.setClickable(false);
				tvHeadsetStatus.setText("Unplugged");
			    }
			});
		    }
		    else if (intent.getIntExtra("state", 0) == 1)
		    {
			runOnUiThread(new Runnable()
			{
			    public void run()
			    {
				// m_Handler.sendEmptyMessage(6);
				// headsetConnected = true;
				open.setClickable(true);
				tvHeadsetStatus.setText("Plugged");

				if (readAfterPlugging
					&& !RcpApi2.getInstance().isOpen())
				{
				    runOnUiThread(new Runnable()
				    {
					public void run()
					{
					    open.performClick();
					}
				    });

				    m_Handler.sendEmptyMessageDelayed(-1, 2000);
				}

			    }
			});

		    }
		}
	    }
	}
    }

    public static Intent createIntent(Context context)
    {
	Intent i = new Intent(context, MainActivity.class);
	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	return i;
    }

    synchronized private void ListClear()
    {
	tagCellList.clear();
	tagStringList.clear();
	tagDataList.clear();
	tagAdapter.notifyDataSetChanged();
	tvTagCount.setText("0  tags");

    }

    private void initWeekTags(){
        weekTags = sharedPref.getAll();
        String currDay = rightNow.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        for (String key : weekTags.keySet()) {
            if(key.length() < 30){
                if(sharedPref.getString(key+currDay, "F") == "T")
                    listRefresh(Integer.parseInt(key), "rouge");
            }
        }
    }

    synchronized private void ListRefreshWithRssi(final int[] data,
	    final int rssi)
    {

	final String str = EpcConverter.toString(encoding_type, data);

	boolean newTagReceived = true;
	int index;

	for (index = 0; index < tagDataList.size(); index++)
	{	    	    
	    if (Arrays.equals(tagDataList.get(index), data))
	    //if (tagCellList.get(index).getName().equals(str))
	    {
		// System.out.println(seqArray.get(index).getName() +
		// " -- " + str);
		newTagReceived = false;
		break;
	    }
	}

	if (newTagReceived)
	{
	    tagDataList.add(data);
	    tagStringList.add(str);
	    
	    runOnUiThread(new Runnable()
	    {
		synchronized public void run()
		{
		    final customCell ttag = new customCell();
		    //ttag.setName(str);
			ttag.setName("cador");

			//TODO Changer str check clé valeur, remplacer id par nom si existe

		    ttag.setValue(Integer.toString(rssi));		    
		    tagCellList.add(ttag);		    
		    tagAdapter.notifyDataSetChanged();
		    tvTagCount.setText(tagCellList.size() + " tags");
		}
	    });
	    
	}
	else
	{
	    final int indexLock = index;

	    runOnUiThread(new Runnable()
	    {
		synchronized public void run()
		{
		    tagCellList.get(indexLock).setValue(Integer.toString(rssi));
		    tagAdapter.notifyDataSetChanged();
		}
	    });

	}

    }

    // TODO Fonction copie de LIstRefresh à appeler avant
    synchronized private void ListRefresh(final int[] data, String color)
    {

	    runOnUiThread(new Runnable()
	    {
		public void run()
		{   
	
	final String str = EpcConverter.toString(encoding_type, data);
	boolean newTagReceived = true;
	boolean newItemReceived = true;
	int index;

	for (index = 0; index < tagDataList.size(); index++)
	{
	    if (Arrays.equals(tagDataList.get(index), data))
	    {		
		newTagReceived = false;
		break;
	    }
	}
		
	if(encoding_type == EpcConverter.EAN13)
	{
	    if(newTagReceived)
	    {
		for (index = 0; index < tagCellList.size(); index++)
		{
    			if (tagCellList.get(index).getName().equals(str))
    			{
    			    newItemReceived = false;    		
    			    break;
    			}
    	    	}
	    }
	    else
	    {
		newItemReceived = false;
	    }
	}
	
	if(newTagReceived)
	{
	    tagDataList.add(data);
	}

	if ( (encoding_type != EpcConverter.EAN13 && newTagReceived)
		|| (encoding_type == EpcConverter.EAN13 && newTagReceived && newItemReceived) )
	{
//	    runOnUiThread(new Runnable()
//	    {
//		public void run()
//		{
		    final customCell ttag = new customCell();
			ttag.setName(str);
		//todo modifier

		    tagCellList.add(ttag);	    
		    tagStringList.add(str);


			SharedPreferences.Editor editor = sharedPref.edit();
			String cador = sharedPref.getString(str, null);
		System.out.println("null");
			if (cador == null){

				editor.putString(str, "inconnu");
				editor.commit();
				cador = sharedPref.getString(str, "key not found");
			}

			ttag.setName(cador);
			ttag.setValue("1");
			ttag.setId(str);
		    tagAdapter.notifyDataSetChanged();
		    tvTagCount.setText(tagCellList.size() + " tags");
//		}
//	    });
	}
	else if( (encoding_type != EpcConverter.EAN13 && !newTagReceived) ||
		(encoding_type == EpcConverter.EAN13 && newTagReceived && !newItemReceived) )
	{
	    final int indexLock = index;
	    
//	    runOnUiThread(new Runnable()
//	    {
//		public void run()
//		{   
		    if(tagCellList.size() > 0)
		    {
			final int currCount = Integer.parseInt((tagCellList.get(indexLock)
				    .getValue()));
			
			tagCellList.get(indexLock).setValue(
				Integer.toString(currCount + 1));
			tagAdapter.notifyDataSetChanged();
		    }
//		}
//	    });
	}	
	
 	}
	    });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        this.max_tag = mPrefs.getInt("MAX_TAG", 0);
        this.max_time = mPrefs.getInt("MAX_TIME", 0);
        this.repeat_cycle = mPrefs.getInt("REPEAT_CYCLE", 0);
        this.speakerBeep = mPrefs.getBoolean("SPEAKER_BEEP", false);
        this.readAfterPlugging = mPrefs
            .getBoolean("READ_AFTER_PLUGGING", false);

        boolean newDisplayRssi = mPrefs.getBoolean("DISPLAY_RSSI", false);
        int new_encoding_type = mPrefs.getInt("ENCODING_TYPE", 0);

        if (this.displayRssi != newDisplayRssi || this.encoding_type != new_encoding_type)
        {
            ListClear();
            this.displayRssi = newDisplayRssi;
            this.encoding_type = new_encoding_type;
        }

        RcpApi2.getInstance().setOnRcpEventListener(this);
        if (RcpApi2.getInstance().isOpen())
        {
            runOnUiThread(new Runnable()
            {
            public void run()
            {
                RcpApi2.getInstance().getReaderInfo((byte) 0xB0);
            }
            });
        }
        ListClear();
    }

    // Stop - not need
    @Override
    protected void onStop()
    {
	super.onStop();
	overridePendingTransition(R.anim.slide_out_left1,
		R.anim.slide_out_left1);
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
		if (RcpApi2.getInstance().isOpen())
		{
		    RcpApi2.getInstance().close();
		}
		usrUnRegisterIntent();
		m_bOnCreate = false;
	    }
	    catch (Exception e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	super.onDestroy();
    }

    // volumax now Voulume backup and volume Max
    private void setVolumeMax()
    {
	if (mAudioManager
		.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC) != mAudioManager
		.getStreamVolume(android.media.AudioManager.STREAM_MUSIC))
	{

	    // System.out.println("Volume = "+ audioManager
	    // .getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC));

	    mAudioManager
		    .setStreamVolume(
			    android.media.AudioManager.STREAM_MUSIC,
			    mAudioManager
				    .getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC),
			    1);
	}
    }

    int mAudioModeBackup;
    boolean playingSound = false;

    @Override
    public void onTagReceived(int[] data)
    {
	ListRefresh(data, "white");
	vibe.vibrate(100);
	
	if (this.speakerBeep)
	{
	    playSound();
	}
    }

    @Override
    public void onTagWithRssiReceived(int[] data, int rssi)
    {
	ListRefreshWithRssi(data, rssi);
	vibe.vibrate(100);
	
	if (this.speakerBeep)
	{
	    playSound();
	}
    }

    synchronized public void playSound()
    {
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		if (RcpApi2.getInstance().isOpen())
		    RcpApi2.getInstance().close();

		// System.out.println("check - playingSound");
		if (!playingSound)
		{
		    System.out.println("playingSound");

		    playingSound = true;

		    AudioManager audioManager = (AudioManager) getApplicationContext()
			    .getSystemService(Context.AUDIO_SERVICE);

		    mAudioModeBackup = audioManager.getMode();
		    audioManager.setMode(AudioManager.MODE_IN_CALL);
		    audioManager.setSpeakerphoneOn(true);

		    try
		    {
			Thread.sleep(500);
		    }
		    catch (InterruptedException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    setVolumeMax();
		    mp.start();
		}
	    }
	});
    }

    @Override
    synchronized public void onCompletion(MediaPlayer mp)
    {
	if (playingSound)
	{
	    // System.out.println("playingSound - onCompletion");
	    AudioManager audioManager = (AudioManager) getApplicationContext()
		    .getSystemService(Context.AUDIO_SERVICE);

	    audioManager.setMode(mAudioModeBackup);
	    audioManager.setSpeakerphoneOn(false);

	    RcpApi2.getInstance().open();
	    setVolumeMax();
	    playingSound = false;
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
    public void onSuccessReceived(int[] data, int code)
    {
	// TODO Auto-generated method stub
	displayConnected();
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		test_msg = RcpLib.int2str(data);
		Toast.makeText(MainActivity.this, "Error code  :  " + test_msg,
			Toast.LENGTH_SHORT).show();
	    }
	});
    }

    @Override
    public void onResetReceived()
    {
	// TODO Auto-generated method stub
	displayConnected();
    }

    private void displayConnected()
    {
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvHeadsetStatus.setText("Connected");
	    }
	});
    }

    @Override
    public void onBatteryStateReceived(final int[] dest)
    {
	if (dest.length < 3)
	    return;

	int adc = dest[0] & 0xff;
	int adcMin = dest[1] & 0xff;
	int adcMax = dest[2] & 0xff;

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

	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvBattery.setText(Integer.toString(battery) + "%");
		tvHeadsetStatus.setText("Connected");
	    }
	});
    }

    @Override
    public void onTagMemoryLongReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void handlerMessage(Message msg)
    {
	switch (msg.what)
	{
	case -1:
	    // System.out.println("timer - 1");
	    startAutoRead.performClick();
	    break;
	}
    }

    @Override
    public void onSessionReceived(int channel)
    {
	
    }

    @Override
    public void onTagWithTidReceived(final int[] pcEpc, final int[] tid)
    {
	ListRefresh(pcEpc);
//	ListRefresh(tid);	
	vibe.vibrate(100);
	
	if (this.speakerBeep)
	{
	    playSound();
	}
    }

    @Override
    public void onGenericTransportReceived(int arg0, int[] arg1)
    {
	// TODO Auto-generated method stub
	
    }
}
