package ch.chicge.smartbag;

import android.app.Activity;
import android.os.Bundle;

import com.phychips.audio.*;
import com.phychips.rcp.*;
import com.phychips.utility.*;
import android.R;

public abstract class MainActivity extends Activity implements iRcpEvent {
	RcpApi aretePop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base);

		aretePop = new RcpApi();
	}

	void areteConnect() {
		try {
			aretePop.open();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
