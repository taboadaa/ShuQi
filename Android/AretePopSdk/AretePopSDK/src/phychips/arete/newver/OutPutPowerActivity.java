package phychips.arete.newver;

import java.util.ArrayList;

import phychips.arete.newver.R;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class OutPutPowerActivity extends Activity
{
    private Button back;

    private ListView outList;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> array_list;

    private int max = 0, min = 0;

    private int powerlevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_out_put_power);

	array_list = new ArrayList<String>();

	min = PopSettingActivity.tx_minpower;
	max = PopSettingActivity.tx_maxpower;

	powerlevel = PopSettingActivity.powerLevel;


	for (int i = min; i <= max; i += 5)
	{
	    array_list.add(Integer.toString(i).substring(0,2)+"."+Integer.toString(i).substring(2));
	}

	adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_checked,array_list);

	back = (Button) findViewById(R.id.outputpower_navigation_back_button);
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

	outList = (ListView) findViewById(R.id.outpout_listview1);
	outList.setAdapter(adapter);

	
	
	outList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	outList.setOnItemClickListener(new OnItemClickListener()
	{

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		try
		{
		    RcpApi.setOutputPowerLevel((byte) ((byte) 0xC8 + (arg2 * 5)));
		}
		catch (RcpException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	
	outList.setItemChecked((powerlevel/5)-40, true);
	adapter.notifyDataSetChanged();
    }
}
