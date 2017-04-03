package ch.chicge.smartbag.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.chicge.smartbag.MainActivity;
import ch.chicge.smartbag.R;
import ch.chicge.smartbag.interfacage.tag;
import ch.chicge.smartbag.interfacage.utilitaire;

/**
 * Created by Vince on 03.04.2017.
 */

public class TagView extends Activity {
    tag t;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);
        LinearLayout layout = (LinearLayout) findViewById(R.id.tagView);

        final Button retour = (Button) findViewById(R.id.retourTag);
        String id =this.getIntent().getStringExtra("myTag");
        this.t = utilitaire.getTagbyID(id);
        addID();
        addCalendrier();
        addCustomName();

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TagView.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addID(){
        TextView text = (TextView) findViewById(R.id.IDTag);
        text.setText("id : " + t.ID);
    }

    public void addCustomName(){
        TextView text = (TextView) findViewById(R.id.CustomNameTag);
        if(t.hasCustomName()){
            text.setText("Custom Name : " + t.CustomName);
        }
        else{
            text.setText("Custom Name : " + "pas de custom name");
        }
    }

    public void addCalendrier(){
        TextView text = (TextView) findViewById(R.id.jours);
        text.setText(t.cal.toString());
    }
}
