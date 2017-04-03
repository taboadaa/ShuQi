package ch.chicge.smartbag.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private boolean unknowntag = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        layout = (LinearLayout) findViewById(R.id.scanPage);
        printKnowntag();
        //addAlert();
        //addUnknownTag("325235-325523-4325325");

        final Button retour = (Button) findViewById(R.id.retourScan);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(scan.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addAlert(){
        TextView alert = new TextView(this);
        alert.setTextColor(Color.RED);
        alert.setText("2 appareil inconnu, veulliez éloigner un appareil!");
        alert.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(alert);
    }

    public void printKnowntag(){
        ArrayList<tag> listTag = utilitaire.getAllKnownTags();
        ArrayList<String> listNearTags = utilitaire.getProxiTags();
        unknowntag = false;
        for (tag t : listTag) {
            TextView tag = new TextView(this);
            tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if(listNearTags.contains(t.ID)){
                tag.setTextColor(Color.GREEN);
                if(t.hasCustomName()){
                    tag.setText(t.CustomName);
                }
                else {
                    tag.setText(t.ID);
                }
            }
            else {
                if(unknowntag){
                    addAlert();
                }
                else {
                    tag.setTextColor(Color.RED);
                    tag.setText(t.ID);
                    addUnknownTag(t.ID);
                }
            }
            layout.addView(tag);
        }
    }

    public void addUnknownTag(String id){
        final Button b = new Button(this);
        b.setText("enregistrer: " + id);
        b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(scan.this, TagView.class);
                intent.putExtra("myTag", b.getText().toString().substring(14));
                startActivity(intent);
            }
        });

        layout.addView(b);
        unknowntag = true;
    }
}
