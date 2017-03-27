package ch.chicge.smartbag.application;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.chicge.smartbag.R;
import ch.chicge.smartbag.MainActivity;
import ch.chicge.smartbag.interfacage.tag;
import ch.chicge.smartbag.interfacage.utilitaire;

/**
 * Created by Vince on 20.03.2017.
 */

public class scan extends Activity {

    private ArrayList<tag> listTag = new ArrayList<tag>();
    private LinearLayout layout;
    private MainActivity t;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
       /* listTag = utilitaire.getProxiTags();
        PrintListe(); */



    public scan(LinearLayout layout, MainActivity t){
        //layout = new LinearLayout(this);
        this.layout = layout;
        this.t = t;
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        addAlert();
        addUnknownTag("535-4534-3214-5214-3132");

        //listTag = utilitaire.getProxiTags();
        //PrintListe();

        //setContentView(R.layout.liste);
        //setContentView(layout);
    }

    public LinearLayout getLayout(){
        return layout;
    }

    public void PrintListe(){
        for (tag t : listTag) {
            if(t.hasCustomName())
                addLayout(t.CustomName);
            else
                addLayout(t.ID);
        }
    }

    public void addLayout(String CustomName){
        //LinearLayout placeHolder = (LinearLayout) findViewById(R.id.layout);
        //getLayoutInflater().inflate(R.layout.second_layout, placeHolder);
    }

    public void addAlert(){
        LinearLayout row = new LinearLayout(t);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView alert = new TextView(t);
        alert.setText("2 appareil inconnu, veulliez éloigner un appareil!");
        alert.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.addView(alert);
        layout.addView(row);
    }

    public void addUnknownTag(String id){
        LinearLayout row = new LinearLayout(t);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button b = new Button(t);

        TextView alert = new TextView(t);
        b.setText("enregistrer: " + id);
        b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.addView(b);
        layout.addView(row);
    }
}
