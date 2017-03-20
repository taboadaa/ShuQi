package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.app.Activity;

public class AppSettingActivity extends Activity
{

    private RadioGroup logGroup, messageGroup;

    private Spinner spinner_encoding;
    private ArrayList<String> spinnerEncodingArray;
    private ArrayAdapter<String> spinnerEncodingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_app_setting);
		
	logGroup = (RadioGroup) findViewById(R.id.LogGroup);
	logGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{

	    @Override
	    public void onCheckedChanged(RadioGroup group, int checkedId)
	    {
		// TODO Auto-generated method stub
		if (checkedId == group.getChildAt(0).getId())
		{
		    MainActivity.saveLog = true;

		}
		else
		{
		    MainActivity.saveLog = false;
		}
	    }
	});
	
	if (MainActivity.saveLog)
	{
	    ((RadioButton) logGroup.getChildAt(0)).setChecked(true);
	}
	else
	{
	    ((RadioButton) logGroup.getChildAt(1)).setChecked(true);
	}

	messageGroup = (RadioGroup) findViewById(R.id.messageGroup);
	messageGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{
	    @Override
	    public void onCheckedChanged(RadioGroup group, int checkedId)
	    {
		// TODO Auto-generated method stub
		if (checkedId == group.getChildAt(0).getId())
		{
		    MainActivity.flag = true;
		}
		else
		{
		    MainActivity.flag = false;
		}
	    }
	});

	if (MainActivity.flag)
	{
	    ((RadioButton) messageGroup.getChildAt(0)).setChecked(true);
	}
	
	else
	{
	    ((RadioButton) messageGroup.getChildAt(1)).setChecked(true);
	}

	spinner_encoding = (Spinner) findViewById(R.id.encoding);

	spinner_encoding.setOnItemSelectedListener(new OnItemSelectedListener()
	{
	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1,
		    int arg2, long arg3)
	    {
		// TODO Auto-generated method stub
		MainActivity.encoding_type = arg2;
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0)
	    {
		// TODO Auto-generated method stub
	    }
	});

	spinnerEncodingArray = new ArrayList<String>();
	spinnerEncodingArray.add("Basic");
	spinnerEncodingArray.add("ASC");

	spinnerEncodingAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_dropdown_item_1line,
		spinnerEncodingArray);
	spinner_encoding.setAdapter(spinnerEncodingAdapter);
	
	spinner_encoding.setSelection(MainActivity.encoding_type);
    }
    
    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onRestart();
        overridePendingTransition( R.anim.slide_out_right1, R.anim.slide_out_right2);
    }
}
