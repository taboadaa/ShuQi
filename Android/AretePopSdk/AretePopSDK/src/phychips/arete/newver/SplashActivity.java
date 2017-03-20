package phychips.arete.newver;

import phychips.arete.newver.R;

import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.WeakRefHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;

public class SplashActivity extends Activity implements IOnHandlerMessage
{

	Boolean flag = true;
	Handler m_Handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		m_Handler = new WeakRefHandler(this);
		
		m_Handler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	public void handlerMessage(Message msg) 
	{
		// TODO Auto-generated method stub		
		finish();	
	}
}
