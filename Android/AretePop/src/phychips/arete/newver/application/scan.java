package phychips.arete.newver.application;


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

import phychips.arete.newver.MainActivity;
import phychips.arete.newver.R;
import phychips.arete.newver.interfacage.tag;
import phychips.arete.newver.interfacage.utilitaire;

/**
 * Created by Vince on 20.03.2017.
 */

public class scan extends Activity {

    private ArrayList<tag> listTag;
    private LinearLayout layout;
    private boolean unknowntag = false;
    private boolean alert = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        layout = (LinearLayout) findViewById(R.id.scanPage);
        //layout = (LinearLayout) R.layout.scan;
    }

    private void create(){
        layout.removeAllViews();
        layout = null;
        layout = new LinearLayout(this);
        final Button retour = new Button(this);
        retour.setText("Retour");
        retour.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(scan.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        layout.addView(retour);
        setContentView(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        create();
        printKnowntag();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void onRestart(){
        super.onRestart();
        delete();
    }

    protected void onPause(){
        super.onPause();
        delete();
    }

    protected void onStop(){
        super.onStop();
        delete();
    }

    protected void onDestroy(){
        super.onDestroy();
        delete();
    }

    private void delete(){
        layout.removeAllViewsInLayout();
        layout.removeAllViews();
    }

    public void addAlert(){
        TextView alert = new TextView(this);
        alert.setTextColor(Color.RED);
        alert.setText("2 appareil inconnu, veulliez éloigner un appareil!");
        alert.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(alert);
    }

    public void printKnowntag(){
        listTag = utilitaire.getAllKnownTags();
        ArrayList<String> listNearTags = utilitaire.getProxiTags();
        unknowntag = false;
        alert = false;
        for (tag t : listTag) {
            TextView tag = new TextView(this);
            tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if(listNearTags.contains(t.ID)){tag.setTextColor(Color.GREEN);}
            else {tag.setTextColor(Color.RED);}

            if(t.hasCustomName()){
                tag.setText(t.CustomName);
            }
            else {
                tag.setText(t.ID);
            }

            layout.addView(tag);
        }
        //get the other tags
        String tmp = "";
        for (String t : listNearTags) {
            if(!isKnown(t)) {
                if (!unknowntag) {
                    tmp = t;
                    unknowntag = true;
                }
                else{alert = true;}
            }
        }
        if(unknowntag){
            if(!alert){
                TextView tag = new TextView(this);
                tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tag.setTextColor(Color.RED);
                tag.setText(tmp);
                addUnknownTag(tmp);
            }
            else{
                addAlert();
            }
        }
    }

    public boolean isKnown(String ID){
        for (tag t : listTag) {
            if(t.ID.equals(ID)){
                return true;
            }
        }
        return false;
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
                finish();
            }
        });

        layout.addView(b);
        unknowntag = true;
    }
}
