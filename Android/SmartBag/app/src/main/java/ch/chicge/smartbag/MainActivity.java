package ch.chicge.smartbag;

import android.app.Activity;
import android.os.Bundle;

<<<<<<< HEAD
import com.phychips.audio.*;
import com.phychips.rcp.*;
import com.phychips.utility.*;
import android.R;
=======
public class MainActivity extends Activity {
>>>>>>> parent of dd0a216... Ajout lib a AndroidStudio

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
<<<<<<< HEAD
		setContentView(R.layout.base);

		aretePop = new RcpApi();
	}

	void areteConnect() {
		try {
			aretePop.open();
		} catch (Exception e) {
			e.printStackTrace();
		}


=======
		setContentView(R.layout.activity_main);
>>>>>>> parent of dd0a216... Ajout lib a AndroidStudio
	}
}
