package phychips.arete.newver;

import phychips.arete.newver.R;

import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.WeakRefHandler;
import com.makeramen.segmented.SegmentedRadioGroup;
import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.RcpTypeCTag;
import com.phychips.rcp.iRcpEvent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ReadActivity extends Activity implements OnCheckedChangeListener,
	iRcpEvent, IOnHandlerMessage
{
    private Button back, done;
    private SegmentedRadioGroup segment_ReadWrite;
    private SegmentedRadioGroup segment_option;

    private TextView read_TargetTag, length, password, address, data,
	    read_legth;

    private boolean state = true;

    private int option = 0;

    private String data_return;

    private Handler m_Handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_read);

	RcpApi.setRcpEvent(this);

	m_Handler = new WeakRefHandler(this);

	segment_ReadWrite = (SegmentedRadioGroup) findViewById(R.id.seg_group_one);
	segment_ReadWrite.setOnCheckedChangeListener(this);
	segment_option = (SegmentedRadioGroup) findViewById(R.id.seg_group_two);
	segment_option.setOnCheckedChangeListener(this);

	read_TargetTag = (TextView) findViewById(R.id.read_targettag);
	read_TargetTag.setText(TagAccessActivity.nowTag);

	length = (TextView) findViewById(R.id.read_length);

	password = (TextView) findViewById(R.id.read_accesspass);

	address = (TextView) findViewById(R.id.read_startAddress);

	data = (TextView) findViewById(R.id.read_data);

	read_legth = (TextView) findViewById(R.id.read_text);

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
		{
		    try
		    {
			String epc = read_TargetTag.getText().toString();

			int data_length = Integer.parseInt(length.getText()
				.toString(), 16);

			RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2,
				data_length);

			tag.password = Long.parseLong(password.getText()
				.toString(), 16);

			tag.epc = RcpLib.convertStringToByteArray(epc);

			tag.start_address = Integer.parseInt(address.getText()
				.toString(), 16);
			tag.memory_bank = option;

			RcpApi.readFromTagMemory(tag);
		    }
		    catch (Exception e)
		    {
			data.setText(e.toString());
			Toast.makeText(ReadActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
		    }
		}

		else
		{
		    try
		    {
			String epc = read_TargetTag.getText().toString();
			String data_temp = data.getText().toString();

			int data_length;

			if (length.getText().toString().equals("0"))
			{
			    data_length = epc.length() / 4 + 2;
			}
			else
			{
			    data_length = Integer.parseInt(length.getText()
				    .toString(), 16);
			}

			RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2,
				data_length);

			tag.password = Long.parseLong(password.getText()
				.toString(), 16);

			tag.epc = RcpLib.convertStringToByteArray(epc);

			tag.start_address = Integer.parseInt(address.getText()
				.toString(), 16);
			tag.data = RcpLib.convertStringToByteArray(data_temp);

			tag.memory_bank = option;

			RcpApi.writeToTagMemory(tag);
		    }
		    catch (Exception e)
		    {
			// data.setText(e.toString());
			Toast.makeText(ReadActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
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
	    }
	    else if (checkedId == R.id.segment_write)
	    {
		state = false;
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
	data_return = RcpLib.int2str(data);
	m_Handler.sendEmptyMessage(0);
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
	    data.setText(data_return);
	    read_legth.setText("Data(HEX) " + data.length() / 2 + " byte");
	    System.out.println(data.length() / 2);
	    break;
	}
    }

    @Override
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

}
