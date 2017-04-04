package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import com.arete.custom.CustomListView2Adapter;
import com.arete.custom.customCell;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TagAccessActivity extends Activity
{
    public ListView listView1;
    private Button back;
    private CustomListView2Adapter adapter1;
    private ArrayList<customCell> string_a;    
    public static String nowTag;    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_tag_access);
	
	string_a = new ArrayList<customCell>();
	string_a.add(new customCell("Read / Write",""));
	string_a.add(new customCell("Lock",""));
	string_a.add(new customCell("Kill",""));
	
	nowTag = splitEpc(getIntent().getStringExtra("tagitem"));
	
	back = (Button)findViewById(R.id.tagaccess_navigation_back_button);
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
	
	adapter1 = new CustomListView2Adapter(this, R.layout.listview_cell2_disclosure,string_a);		
	listView1 = (ListView) findViewById(R.id.tagacess_listView);		
	listView1.setAdapter(adapter1);	
	listView1.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		switch(arg2)
		{
		case 0:
		{
		    Intent intent = new Intent(getBaseContext(),
			    ReadWriteActivity.class);
		    startActivity(intent);
		}
		    break;
		case 1:
		{
		    Intent intent = new Intent(getBaseContext(),
			LockActivity.class);
		    startActivity(intent);
		}
		    break;
		case 2:
		{
		    Intent intent = new Intent(getBaseContext(),
			KillActivity.class);
		    startActivity(intent);
		}
		    break;
		}		
	    }	    
	});
    }
    
    public String splitEpc(String pcXpcEpc)
    {
	if((Byte.parseByte(pcXpcEpc.substring(0, 2), 16) & 0x02) == 0x02)	// check the XI bit on the PC
	    if((Byte.parseByte(pcXpcEpc.substring(4, 6), 16) & 0x80) == 0x80)	// check the XEB bit on the XPC_W1
		    return pcXpcEpc.substring(12);
	    else
		    return pcXpcEpc.substring(8);
	else
	    return pcXpcEpc.substring(4);
    }
}
