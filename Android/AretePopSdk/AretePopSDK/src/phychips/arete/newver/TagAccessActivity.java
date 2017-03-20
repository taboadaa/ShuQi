package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import com.arete.custom.SimpleListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TagAccessActivity extends Activity
{
    private ListView listView1;
   
    
    private Button back;

    private SimpleListAdapter adapter1;
  

    private ArrayList<String> string_a;

    
    public static String nowTag;
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_tag_access);
	
	string_a = new ArrayList<String>();
	string_a.add("Read / Write");
	string_a.add("Lock");
	string_a.add("Kill");
	
	
		
	nowTag = getIntent().getStringExtra("tagitem").substring(4);
	
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
	
	adapter1 = new SimpleListAdapter(this, R.layout.simple_list,string_a);
	
	
	listView1 = (ListView) findViewById(R.id.tagacess_listView);
	
	
	listView1.setAdapter(adapter1);
	
    }

   

}
