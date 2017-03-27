package ch.chicge.smartbag;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.chicge.smartbag.application.liste;
import ch.chicge.smartbag.application.scan;
import ch.chicge.smartbag.application.base;
import ch.chicge.smartbag.application.scan;

public class MainActivity extends Activity {


    private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        layout = new LinearLayout(this);
        scan s = new scan(layout, this);
        setContentView(s.getLayout());


		//layout = new LinearLayout(this);
        //layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        //LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /*Button b = new Button(this);
        b.setText("mon boutton");
        //b.setLayoutParams(lparams);
        Button b2 = new Button(this);
        b2.setLeft(800);
        b2.setTop(800);
        b2.setText("mon deuxième boutton");
        //b2.setLayoutParams(lparams);
        layout.addView(b);
        layout.addView(b2);*/

        /*TextView alert = new TextView(this);
        alert.setText("2 appareil inconnu, veulliez éloigner un appareil!");
        alert.setTextColor(Color.red(20));
        alert.setHeight(200);
        alert.se
        layout.addView(alert);*/




        /*for (int i = 0; i < 2; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 2; j++) {
                Button btnTag = new Button(this);
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btnTag.setText("Button " + (j + 1 + (i * 4)));
                btnTag.setId(j + 1 + (i * 4));
                row.addView(btnTag);
            }

            layout.addView(row);
        }*/

        //
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
