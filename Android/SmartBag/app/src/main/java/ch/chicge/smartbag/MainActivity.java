package ch.chicge.smartbag;

import android.app.Activity;
import android.os.Bundle;

import ch.chicge.smartbag.application.base;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		base b = new base();
        b.setBaseView();
	}


}
