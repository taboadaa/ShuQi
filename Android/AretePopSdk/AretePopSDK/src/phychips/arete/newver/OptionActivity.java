package phychips.arete.newver;


import java.util.ArrayList;

import com.arete.custom.SimpleListAdapter;

import phychips.arete.newver.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import android.app.Activity;


public class OptionActivity extends Activity
{
    private ListView listView1;
   // private ListView listView2;
    private ListView listView3;
    private ListView listView4;
    
    private Button back;

    private SimpleListAdapter adapter1;
    //private SimpleListAdapter adapter2;
    private SimpleListAdapter adapter3;  
    
    private ArrayList<String> string_a;
    private ArrayList<String> string_b;
    private ArrayList<String> string_c;
    
 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_option);
	
	string_a = new ArrayList<String>();
	string_a.add("POP Setting");
	
	string_b = new ArrayList<String>();
	string_b.add("App Setting");
	
	string_c = new ArrayList<String>();
	string_c.add("About");
	
	
	
	adapter1 = new SimpleListAdapter(this, R.layout.simple_list,string_a);
	
	listView1 = (ListView) findViewById(R.id.optionList_pop);
	
	listView1.setAdapter(adapter1);
	
	//App setting
	
	//adapter2 = new SimpleListAdapter(this, R.layout.simple_list,string_b);
	
	//listView2 = (ListView) findViewById(R.id.optionList_app);
	
	//listView2.setAdapter(adapter2);
	
	
	adapter3 = new SimpleListAdapter(this, R.layout.simple_list,string_c);
	
	listView3 = (ListView) findViewById(R.id.optionList_about);
	
	listView3.setAdapter(adapter3);
	
	back =(Button) findViewById(R.id.option_navigation_back_button);
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
    }
    
    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onRestart();
        overridePendingTransition( R.anim.slide_out_right1, R.anim.slide_out_right2);
    }
}
