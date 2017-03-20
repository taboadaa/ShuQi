package phychips.arete.newver;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import phychips.arete.newver.R;

public class StopCondisionsActivity extends Activity
{
    private Button back, done;

    private EditText text1, text2, text3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_stop_condisions);

	text1 = (EditText) findViewById(R.id.stop_editText1);
	text2 = (EditText) findViewById(R.id.stop_editText2);
	text3 = (EditText) findViewById(R.id.stop_editText3);

	back = (Button) findViewById(R.id.stopconditions_navigation_back_button);
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

	done = (Button) findViewById(R.id.stop_done_btn);
	done.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {

		// TODO Auto-generated method stub
		if (isNumber(text1.getText().toString()))
		{

		    MainActivity.max_tag = Integer.parseInt(text1.getText()
			    .toString());

		    if (isNumber(text2.getText().toString()))
		    {
			MainActivity.max_time = Integer.parseInt(text2
				.getText().toString());
			if (isNumber(text3.getText().toString()))
			{
			    MainActivity.repeat_cycle = Integer.parseInt(text3
				    .getText().toString());
			    Toast.makeText(StopCondisionsActivity.this,
				    "success", Toast.LENGTH_SHORT).show();
			}
			else
			{
			    Toast.makeText(StopCondisionsActivity.this,
				    "Error: Only integers allowed", Toast.LENGTH_SHORT).show();
			}
		    }
		    else
		    {
			Toast.makeText(StopCondisionsActivity.this, "Error: Only integers allowed",
				Toast.LENGTH_SHORT).show();
		    }

		}
		else
		{
		    Toast.makeText(StopCondisionsActivity.this, "Error: Only integers allowed",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    public static boolean isNumber(String str)
    {
	boolean check = true;
	
	if (!str.isEmpty())
	{
	    for (int i = 0; i < str.length(); i++)
	    {
		if (!Character.isDigit(str.charAt(i)))
		{
		    check = false;
		    break;
		}
	    }
	    return check;
	}
	else
	{
	    check = false;
	    return check;
	}
    }

}
