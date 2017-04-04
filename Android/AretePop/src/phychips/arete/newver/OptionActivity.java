package phychips.arete.newver;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.arete.custom.CustomListView2Adapter;
import com.arete.custom.customCell;
import com.phychips.utility.Logger;
import phychips.arete.newver.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class OptionActivity extends Activity
{
    private ListView listView1;
//    private ListView listView2;
    private ListView listView3;
    private ListView listView4;
   
    private Button back;

    private CustomListView2Adapter adapter1;
//    private SimpleListAdapter adapter2;
    private CustomListView2Adapter adapter3;  
    private CustomListView2Adapter adapter4;
    
    private ArrayList<customCell> string_a;
//    private ArrayList<String> string_b;
    private ArrayList<customCell> string_c;
    private ArrayList<customCell> string_d;
    private Intent intent;	
    private String[] tagList = null;
 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_option);
	
	string_a = new ArrayList<customCell>();
	string_a.add(new customCell("Settings",""));
	
//	string_b = new ArrayList<String>();
//	string_b.add("App Setting");
	
	string_c = new ArrayList<customCell>();
	string_c.add(new customCell("About",""));
	
	string_d = new ArrayList<customCell>();
	string_d.add(new customCell("Export tag(s)",""));
	
	intent = getIntent();
	if(intent != null)
	{
	    ArrayList<String> tags = intent.getStringArrayListExtra("TAG_LIST");
	    tagList = tags.toArray(new String[0]);
	}
	
	adapter1 = new CustomListView2Adapter(this, R.layout.listview_cell2_disclosure,string_a);	
	listView1 = (ListView) findViewById(R.id.optionList_pop);	
	listView1.setAdapter(adapter1);	
	listView1.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		Intent intent = new Intent(getBaseContext(),
		PopSettingActivity.class);
		startActivity(intent);
	    }
	    
	});
	
	//App setting	
//	adapter2 = new SimpleListAdapter(this, R.layout.simple_list,string_b);	
//	listView2 = (ListView) findViewById(R.id.optionList_app);	
//	listView2.setAdapter(adapter2);
//	listView2.setOnItemClickListener(new OnItemClickListener()
//	{
//	    @Override
//	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		    long arg3)
//	    {
//		Intent intent = new Intent(getBaseContext(),
//		AppSettingActivity.class);
//		startActivity(intent);			
//	    }
//	});
	
	adapter3 = new CustomListView2Adapter(this, R.layout.listview_cell2_disclosure,string_c);	
	listView3 = (ListView) findViewById(R.id.optionList_about);	
	listView3.setAdapter(adapter3);
	listView3.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		Intent intent = new Intent(getBaseContext(),
		PopInformationActivity.class);
		startActivity(intent);			
	    }
	});
	
	adapter4 = new CustomListView2Adapter(this, R.layout.listview_cell2_disclosure,string_d);	
	listView4 = (ListView) findViewById(R.id.optionList_export_tag);	
	listView4.setAdapter(adapter4);	
	listView4.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		if(tagList != null)
		{
		    // file save
		    String fileName = getFilename();	
		    String contents = getTags();
		    Logger tagWriter = new Logger();
		    tagWriter.initFile("RFID", fileName);
		    tagWriter.write(contents);
		    tagWriter.releaseFile();
		    		    
		    File file = new File(Environment.getExternalStorageDirectory()
			    .getPath() + "/RFID/", fileName); 
		    
		    if(file != null && file.exists()&&file.isFile())
		    {
			 // email
			 Intent i = new Intent(android.content.Intent.ACTION_SEND);
			 i.setType("application/csv");
			 i.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		         i.putExtra(android.content.Intent.EXTRA_SUBJECT, fileName);
		         i.putExtra(android.content.Intent.EXTRA_TEXT, contents);
		         Uri u = Uri.fromFile(file);	
		         i.putExtra(Intent.EXTRA_STREAM, u);
		         startActivity(Intent.createChooser(i, "Choose Email Client"));
		    }	    
		   
		}
	    }
	});
	
	back =(Button) findViewById(R.id.option_navigation_back_button);
	back.setOnClickListener(new OnClickListener()
	{	    
	    @Override
	    public void onClick(View v)
	    {
		moveTaskToBack(false);
		finish();
	    }
	});
    }
    
    @Override
    protected void onStart()
    {
        super.onRestart();
        overridePendingTransition( R.anim.slide_out_right1, R.anim.slide_out_right2);
    }   
    
    private String getFilename()
    {
	long time = System.currentTimeMillis();
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(time);

	String fileName;

	fileName = "tag_list_"
		+ "_"
		+ Integer.toString(c.get(Calendar.YEAR))
		+ Integer.toString(c.get(Calendar.MONTH) + 101).substring(1)
		+ Integer.toString(c.get(Calendar.DATE) + 100).substring(1)
		+ "_"
		+ Integer.toString(c.get(Calendar.HOUR_OF_DAY) + 100)
			.substring(1)
		+ Integer.toString(c.get(Calendar.MINUTE) + 100).substring(1)
		+ Integer.toString(c.get(Calendar.SECOND) + 100).substring(1)
		+ ".csv";

	return fileName;
    }

    private String getTags()
    {
	StringBuilder sb = new StringBuilder();
	
	synchronized (tagList)
	{
	    for (int i = 0; i < tagList.length; i++)
	    {
		sb.append(tagList[i]);
		sb.append("\n");
	    }
	}

	return sb.toString();
    }
    

    

    
}
