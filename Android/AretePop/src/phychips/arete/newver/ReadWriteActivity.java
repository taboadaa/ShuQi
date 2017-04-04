package phychips.arete.newver;


import phychips.arete.newver.R;

import com.makeramen.segmented.SegmentedRadioGroup;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.phychips.rcp.*;


@SuppressLint("DefaultLocale")
public class ReadWriteActivity extends Activity implements
	OnCheckedChangeListener, iRcpEvent2
{
    public Byte resultCode = 0x00;
    
    private Button back;
    private Button done;
    private SegmentedRadioGroup segment_ReadWrite;
    private SegmentedRadioGroup segment_option;

    private TextView tvTargetTag;
    private TextView tvLength;
    private TextView tvPassword;
    private TextView tvAddress;
    private TextView tvData;
    private TextView tvTitle;
    // private TextView tvDataText;

    private boolean state = true;

    private int option = 0;

    private String dataText;

    private Integer dataAddr;
    private StringBuffer dataBuffer;

    // private Handler m_Handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_readwrite);
	RcpApi2.getInstance().setOnRcpEventListener(this);

	segment_ReadWrite = (SegmentedRadioGroup) findViewById(R.id.seg_group_one);
	segment_ReadWrite.setOnCheckedChangeListener(this);
	segment_option = (SegmentedRadioGroup) findViewById(R.id.seg_group_two);
	segment_option.setOnCheckedChangeListener(this);

	tvTargetTag = (TextView) findViewById(R.id.read_targettag);
	tvTargetTag.setText(TagAccessActivity.nowTag);

	tvLength = (TextView) findViewById(R.id.read_length);

	tvPassword = (TextView) findViewById(R.id.read_accesspass);

	tvAddress = (TextView) findViewById(R.id.read_startAddress);

	tvData = (TextView) findViewById(R.id.read_data);

	tvTitle = (TextView) findViewById(R.id.titlebar_text);

	back = (Button) findViewById(R.id.read_navigation_back_button);

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

	done = (Button) findViewById(R.id.read_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (state)
		{	// Read Memory
		    tvData.setText(null);
		    
		    try
		    {
			String epc = tvTargetTag.getText().toString();

			int data_length = Integer.parseInt(tvLength.getText()
				.toString());

			if (data_length <= 64)
			{
			    RcpApi2.getInstance().readFromTagMemory(
				Long.parseLong(tvPassword
					.getText().toString(), 16),
				RcpLib.convertStringToByteArray(epc),
				option,
				Integer.parseInt(tvAddress
					.getText().toString(), 16),
				data_length);
			}
			else
			{
			    // Sets the maxLength of "tvData"
			    InputFilter[] FilterArray = new InputFilter[1];
			    FilterArray[0] = new InputFilter.LengthFilter((data_length>>2)*40);
			    tvData.setFilters(FilterArray);

			    dataAddr = 0;
			    dataBuffer = new StringBuffer();
			    RcpApi2.getInstance().readFromTagMemoryLong(
				Long.parseLong(tvPassword
					.getText().toString(), 16),
				RcpLib.convertStringToByteArray(epc),
				option,
				Integer.parseInt(tvAddress
					.getText().toString(), 16),
				data_length);
			}
		    }
		    catch (Exception e)
		    {
			Toast.makeText(ReadWriteActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
		    }
		}
		else
		{	// Write Memory
		    String epc = tvTargetTag.getText().toString();
		    String data_temp = tvData.getText().toString();

		    if(data_temp.length()%4 != 0)
			Toast.makeText(ReadWriteActivity.this, "Data length is invalid.", Toast.LENGTH_SHORT).show();
		    else
		    {
			try
			{
			    RcpApi2.getInstance().writeToTagMemory(
				    Long.parseLong(tvPassword.getText()
					    .toString(), 16),
				    RcpLib.convertStringToByteArray(epc),
				    option,
				    Integer.parseInt(tvAddress.getText()
					    .toString(), 16),
				    RcpLib.convertStringToByteArray(data_temp));
			    
			    tvLength.setText(String.valueOf(data_temp.length()/4));
			}
			catch (Exception e)
			{
			    Toast.makeText(ReadWriteActivity.this,
				    e.toString(), Toast.LENGTH_SHORT).show();
			}
		    }
		}
	    }
	});
    }

    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
	if (group == segment_ReadWrite)
	{
	    if (checkedId == R.id.segment_read)
	    {
		state = true;
		tvTitle.setText("Read");
		tvLength.setEnabled(true);
	    }
	    else if (checkedId == R.id.segment_write)
	    {
		state = false;
		tvTitle.setText("Write");
		tvLength.setEnabled(false);
	    }
	}
	else if (group == segment_option)
	{
	    if (checkedId == R.id.segment_rfu)
	    {
		option = 0x00;
	    }
	    else if (checkedId == R.id.segment_epc)
	    {
		option = 0x01;
	    }
	    else if (checkedId == R.id.segment_tid)
	    {
		option = 0x02;
	    }
	    else if (checkedId == R.id.segment_user)
	    {
		option = 0x03;
	    }
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
    public void onTagMemoryReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	resultCode = 0x00;
	
	dataText = RcpLib.int2str(data);
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvData.setText(dataText);
		// tvDataText.setText("Data(HEX) " + data.length + " byte");
		// System.out.println(tvData.length() / 2);
	    }
	});
	// m_Handler.sendEmptyMessage(0);
    }

    @Override
    public void onResetReceived()
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onSuccessReceived(final int[] data, final int commandCode)
    {
	// TODO Auto-generated method stub
	resultCode = 0x00;
	
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		Toast.makeText(ReadWriteActivity.this, "Success",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	System.out.println("onFailureReceived");

	resultCode = (byte)(data[0] & 0xFF);
	
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		tvData.setText(null);

		Toast.makeText(ReadWriteActivity.this, "Error: Error Code = 0x" + RcpLib.byte2string((byte)(data[0]&0xff)),
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
	
	if (dest.length > 3)
	{
	    dataAddr = Integer.parseInt(tvAddress.getText().toString(), 16);
	    dataBuffer.append(RcpLib.int2str(dest).substring(6));

	    runOnUiThread(new Runnable()
	    {
		public void run()
		{
		    tvData.setText("Read operation is in progress..");
		}
	    });
	}
	else if((dest.length == 1) & (dest[0] == 0x1F))	// 0x1F = Complete Read Memory
	{
	    runOnUiThread(new Runnable()
	    {
		public void run()
		{
		    tvData.setText(editAddressString(dataAddr, dataBuffer));
		}
	    });
	}
    }

    public String editAddressString(int dataAddr, StringBuffer dataString)
    {
	StringBuffer sb = new StringBuffer();

	for(int i=0; i<dataString.length(); i++)
	{
	    if(i%16 == 0)
		sb.append(String.format("0x%04X    ", dataAddr+(i/4)));

	    sb.append(dataString.charAt(i));  

	    if(i%2 == 1)
		sb.append(" ");

	    if(i%16 == 15 )
		sb.append("\n");		                  
	}
	
	return sb.toString();
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
