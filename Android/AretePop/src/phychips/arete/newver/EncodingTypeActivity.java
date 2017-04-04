package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;
import com.phychips.utility.EpcConverter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EncodingTypeActivity extends Activity 
{
    private Button back;
    private Button done;
    private ListView typeList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> array_list;
    private SharedPreferences mPrefs = null;
    private int type = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_encoding_type);
	
	mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	array_list = new ArrayList<String>();
			
	int i = 0;
	while (true)
	{
	    String strType = EpcConverter.toTypeString(i++);
	    if(strType == null)
		break;
	    
	    array_list.add(strType);	    
	}	
		
	adapter = new ArrayAdapter<String>(this, R.layout.listview_cell1_checked,array_list);
		
	back = (Button) findViewById(R.id.navigation_back_button);
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

	typeList = (ListView) findViewById(R.id.type_listview1);
	typeList.setAdapter(adapter);
	typeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);	
	typeList.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		type = arg2;
	    }
	});
	
	typeList.setItemChecked(mPrefs.getInt("ENCODING_TYPE", 0), true);
	adapter.notifyDataSetChanged();
		    
	done = (Button) findViewById(R.id.power_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		SharedPreferences.Editor editor = mPrefs.edit();
		    editor.putInt("ENCODING_TYPE", type);			    
		    editor.commit();		    
		    Toast.makeText(getBaseContext(),
			    "success", Toast.LENGTH_SHORT).show();
	    }
	});
    }

 }
