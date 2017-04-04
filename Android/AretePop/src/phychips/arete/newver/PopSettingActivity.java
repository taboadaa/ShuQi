package phychips.arete.newver;

import phychips.arete.newver.R;
import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.WeakRefHandler;
import com.phychips.rcp.RcpApi2;
import com.phychips.rcp.RcpFhLbtParam;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent2;
import com.phychips.utility.EpcConverter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PopSettingActivity extends Activity implements iRcpEvent2,
	IOnHandlerMessage
{
    private Button back;
    private TextView tvEncodingType;
    private TextView tvOutputPower;
    private TextView tvOnOffTime;
    private TextView tvStopCondition;
    //private TextView tvSession;
    private String beepState;

    private int on_time, off_time, sense_time, fh_enable, lbt_enable,
	    cw_enable;
    static int tx_minpower, tx_maxpower, powerLevel;

    private ToggleButton beepSet;

    private Handler m_Handler;

    static RcpFhLbtParam param;

    private int mTimerError = 0;
    private boolean mGetInfo = false;
    private SharedPreferences mPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_pop_setting);

	mPrefs = PreferenceManager
		.getDefaultSharedPreferences(getBaseContext());

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

	

	param = new RcpFhLbtParam();

	beepSet = (ToggleButton) findViewById(R.id.popsetting_toggle);
	beepSet.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		if (isChecked)
		{
		    RcpApi2.getInstance().setBeep(true);
		}
		else
		{
		    RcpApi2.getInstance().setBeep(false);
		}
	    }
	});

	ToggleButton tglSpeakerBeep = (ToggleButton) findViewById(R.id.popsetting_toggle_speaker);
	tglSpeakerBeep.setChecked(mPrefs.getBoolean("SPEAKER_BEEP", false));
	tglSpeakerBeep.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{
	    boolean speakerBeep = false;

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		if (isChecked)
		{
		    speakerBeep = true;
		}
		else
		{
		    speakerBeep = false;
		}
		SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("SPEAKER_BEEP", speakerBeep);
		editor.commit();
	    }
	});

	ToggleButton rssiSet = (ToggleButton) findViewById(R.id.appsetting_rssi);
	rssiSet.setChecked(mPrefs.getBoolean("DISPLAY_RSSI", false));
	rssiSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
	{
	    boolean rssi = false;

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		if (isChecked)
		{
		    rssi = true;
		}
		else
		{
		    rssi = false;
		}

		System.out.println("rssi = " + rssi);

		SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(PopSettingActivity.this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("DISPLAY_RSSI", rssi);
		editor.commit();
	    }
	});

	ToggleButton tglReadAfterStartup = (ToggleButton) findViewById(R.id.appsetting_read_after_plugging);
	tglReadAfterStartup.setChecked(mPrefs.getBoolean("READ_AFTER_PLUGGING",
		false));
	tglReadAfterStartup
		.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    boolean readAfterPlugging = false;

		    @Override
		    public void onCheckedChanged(CompoundButton buttonView,
			    boolean isChecked)
		    {
			if (isChecked)
			{
			    readAfterPlugging = true;
			}
			else
			{
			    readAfterPlugging = false;
			}
			SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("READ_AFTER_PLUGGING",
				readAfterPlugging);
			editor.commit();
		    }
		});

	tvOutputPower = (TextView) findViewById(R.id.output_power);
	//tvOutputPower.setText("detail");
	final RelativeLayout outputPowerLayout 
		= (RelativeLayout) findViewById(R.id.output_power_layout);	
	outputPowerLayout.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Intent intent = new Intent(getBaseContext(),
			OutPutPowerActivity.class);
		startActivity(intent);
	    }   
	});
	
	tvOnOffTime = (TextView) findViewById(R.id.on_off_time);	
	final RelativeLayout onOffLayout 
		= (RelativeLayout) findViewById(R.id.on_off_time_layout);
	onOffLayout.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Intent intent = new Intent(getBaseContext(),
			OnOffTimeActivity.class);
		intent.putExtra("value", tvOnOffTime.getText());
		startActivity(intent);
	    }
	});
	
	tvStopCondition = (TextView) findViewById(R.id.stop_contidion);	
	final RelativeLayout stopConditionLayout 
		= (RelativeLayout) findViewById(R.id.stop_contidion_layout);
	stopConditionLayout.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		    Intent intent = new Intent(getBaseContext(),
			    StopCondisionsActivity.class);
		    startActivity(intent);
	    }
	});
	
	tvEncodingType = (TextView) findViewById(R.id.encoding_type);	
	final RelativeLayout encodingLayout = (RelativeLayout) findViewById(R.id.encoding_type_layout);
	encodingLayout.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Intent intent = new Intent(getBaseContext(),
			    EncodingTypeActivity.class);
		    startActivity(intent);
	    }
	});
	
	//tvSession = (TextView) findViewById(R.id.session);	
	final RelativeLayout sessionLayout = (RelativeLayout) findViewById(R.id.session_layout);
	sessionLayout.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		Intent intent = new Intent(getBaseContext(),
			    SessionActivity.class);
		    startActivity(intent);
	    }
	});
	
	final RelativeLayout supervisorLayout = (RelativeLayout) findViewById(R.id.supervisor_layout);
	supervisorLayout.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
	        AlertDialog.Builder builder = new AlertDialog.Builder(PopSettingActivity.this);
	        builder.setTitle("Password Required");
	        builder.setMessage("Please enter the supervisor password.");
	            
		final EditText input = new EditText(PopSettingActivity.this);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		input.setTransformationMethod(PasswordTransformationMethod.getInstance());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                        LinearLayout.LayoutParams.MATCH_PARENT,
		                        LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		builder.setView(input);
		builder.setPositiveButton("OK",
			new DialogInterface.OnClickListener() 
		{	                
		    public void onClick(DialogInterface dialog,int which) 
		    {			
			if(RcpApi2.getInstance().submitPassword(input.getText().toString()))
			{
			    Intent intent = new Intent(getBaseContext(),
				RfSettingsActivity.class);
			    	startActivity(intent);			    
			}
			else
			{
			    dialog.cancel();
			    AlertDialog.Builder subbuilder = new AlertDialog.Builder(PopSettingActivity.this);
			    subbuilder.setTitle("Error");
			    subbuilder.setMessage("Invalid password!");
			    subbuilder.setNegativeButton("OK", null);
			    AlertDialog subdialog = subbuilder.create();
			    subdialog.show();
			}			
	            }
	        });		
		AlertDialog dialog = builder.create();
		dialog.show();
	    }
	    
	});
	
	m_Handler = new WeakRefHandler(this);

	RcpApi2.getInstance().getReaderInfo(0xB0);
	mTimerError = 0;
	m_Handler.sendEmptyMessageDelayed(-1, 1000);
    }

    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	RcpApi2.getInstance().setOnRcpEventListener(this);
	
	tvStopCondition.setText(mPrefs.getInt("MAX_TAG", 0) + "/"
		    + mPrefs.getInt("MAX_TIME", 0) + "/"
		    + mPrefs.getInt("REPEAT_CYCLE", 0));
	
	tvEncodingType.setText(EpcConverter.toTypeString(mPrefs.getInt("ENCODING_TYPE", 0)));
	
	mGetInfo = false;
	mTimerError = 0;
	RcpApi2.getInstance().getReaderInfo(0xB0);	
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
    public void onReaderInfoReceived(final int[] data)
    {
	System.out.println("onReaderInfoReceived..");

	mGetInfo = true;
	 
	if (data.length == 20 && ((data[0] & 0xff) == 0xB0))
	{
	    String temp = RcpLib.int2str(data);

	    beepState = temp.substring(2, 4);

	    on_time = Integer.parseInt(temp.substring(6, 10), 16);

	    off_time = Integer.parseInt(temp.substring(10, 14), 16);

	    sense_time = Integer.parseInt(temp.substring(14, 18), 16);

	    // lbt_rf_level = Integer.parseInt(temp.substring(18, 22), 16);

	    fh_enable = Integer.parseInt(temp.substring(22, 24), 16);

	    lbt_enable = Integer.parseInt(temp.substring(24, 26), 16);

	    cw_enable = Integer.parseInt(temp.substring(26, 28), 16);

	    powerLevel = Integer.parseInt(temp.substring(28, 32), 16);

	    tx_minpower = Integer.parseInt(temp.substring(32, 36), 16);

	    tx_maxpower = Integer.parseInt(temp.substring(36, 40), 16);

	    param.readtime = on_time;
	    param.idletime = off_time;
	    param.sensetime = sense_time;
	    param.lbtlevel = powerLevel;
	    param.fhmode = fh_enable;
	    param.lbtmode = lbt_enable;
	    param.cwmode = cw_enable;

	    runOnUiThread(new Runnable()
	    {
		public void run()
		{
//		    listRegulatoryParam.clear();
//		    listReadingParam.clear();

		    tvOutputPower.setText(Integer
			    .toString(powerLevel).substring(0, 2)
			    + "."
			    + Integer.toString(powerLevel).substring(2));
		    tvOnOffTime.setText(Integer.toString(on_time) + "/"
			    + Integer.toString(off_time));		    
		    
		    if (beepState.equals("01"))
		    {
			beepSet.setChecked(true);
		    }
		    else
		    {
			beepSet.setChecked(false);
		    }	    
		    
//		    listRegulatoryParam.add(OutputPower);
//		    listRegulatoryParam.add(OnOffTime);
//		    listReadingParam.add(StopConditions);
//		    adapterRegulatoryParam.notifyDataSetChanged();
//		    adapterReadingParam.notifyDataSetChanged();

		}
	    });
	}
	else
	{

	    // m_Handler.sendEmptyMessage(1);
	    runOnUiThread(new Runnable()
	    {
		public void run()
		{
		    Toast.makeText(PopSettingActivity.this, "Update needed",
			    Toast.LENGTH_SHORT).show();
		}
	    });

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

    }

    @Override
    public void onSuccessReceived(final int[] data, final int commandCode)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		Toast.makeText(PopSettingActivity.this, "Success",
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
		Toast.makeText(PopSettingActivity.this,
			"Error: Error Code = " + data[0], Toast.LENGTH_LONG)
			.show();
	    }
	});
    }

    @Override
    public void handlerMessage(Message msg)
    {
	switch (msg.what)
	{
	case -1:
	    // System.out.println("timer - 1");	    
	    if (!mGetInfo && mTimerError < 5)
	    {
		RcpApi2.getInstance().getReaderInfo(0xB0);
		m_Handler.sendEmptyMessageDelayed(-1, 1000);
		mTimerError++;
	    }
	}
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
