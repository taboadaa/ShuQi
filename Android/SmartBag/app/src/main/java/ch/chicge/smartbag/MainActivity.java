package ch.chicge.smartbag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.chicge.smartbag.application.liste;
import ch.chicge.smartbag.application.scan;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*base b = new base();
        b.setBaseView(); */
		final Button scanButt = (Button) findViewById(R.id.button10);
		final Button listButt = (Button) findViewById(R.id.button9);

		scanButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, scan.class);
				startActivity(intent);
			}
		});

		listButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, liste.class);
				startActivity(intent);
			}
		});
	}


}
