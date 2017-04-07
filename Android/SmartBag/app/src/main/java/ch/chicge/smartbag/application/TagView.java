package ch.chicge.smartbag.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.chicge.smartbag.MainActivity;
import ch.chicge.smartbag.R;
import ch.chicge.smartbag.interfacage.tag;
import ch.chicge.smartbag.interfacage.utilitaire;


public class TagView extends Activity {
    tag t;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);
        final Button retour = (Button) findViewById(R.id.retourTag);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagView.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final EditText e = (EditText) findViewById(R.id.editName);
        final Button valider = (Button) findViewById(R.id.ValiderName);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setCustomName(e.getText().toString());
                addCustomName();
                setCalendrier(t);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id =this.getIntent().getStringExtra("myTag");
        this.t = utilitaire.getTagbyID(id);
        addID();
        addCalendrier();
        addCustomName();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void onRestart(){
        super.onRestart();
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
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
        calendrier cal = t.cal;

        if(cal.Lundi) {
            final CheckBox Lundi = (CheckBox) findViewById(R.id.Lundi);
            Lundi.setChecked(true);
        }
        if(cal.Mardi) {
            final CheckBox Mardi = (CheckBox) findViewById(R.id.Mardi);
            Mardi.setChecked(true);
        }
        if(cal.Mercredi) {
            final CheckBox Mercredi = (CheckBox) findViewById(R.id.Mercredi);
            Mercredi.setChecked(true);
        }
        if(cal.Jeudi) {
            final CheckBox Jeudi = (CheckBox) findViewById(R.id.Jeudi);
            Jeudi.setChecked(true);
        }
        if(cal.Vendredi) {
            final CheckBox Vendredi = (CheckBox) findViewById(R.id.Vendredi);
            Vendredi.setChecked(true);
        }
        if(cal.Samedi) {
            final CheckBox Samedi = (CheckBox) findViewById(R.id.Samedi);
            Samedi.setChecked(true);
        }
        if(cal.Dimanche) {
            final CheckBox Dimanche = (CheckBox) findViewById(R.id.Dimanche);
            Dimanche.setChecked(true);
        }
    }

    public void setCalendrier(tag t){
        final CheckBox Lundi = (CheckBox) findViewById(R.id.Lundi);
        final CheckBox Mardi = (CheckBox) findViewById(R.id.Mardi);
        final CheckBox Mercredi = (CheckBox) findViewById(R.id.Mercredi);
        final CheckBox Jeudi = (CheckBox) findViewById(R.id.Jeudi);
        final CheckBox Vendredi = (CheckBox) findViewById(R.id.Vendredi);
        final CheckBox Samedi = (CheckBox) findViewById(R.id.Samedi);
        final CheckBox Dimanche = (CheckBox) findViewById(R.id.Dimanche);

        if(Lundi.isChecked()){
            t.cal.Lundi = true;
        }
        if(Mardi.isChecked()){
            t.cal.Mardi = true;
        }
        if(Mercredi.isChecked()){
            t.cal.Mercredi = true;
        }
        if(Jeudi.isChecked()){
            t.cal.Jeudi = true;
        }
        if(Vendredi.isChecked()){
            t.cal.Vendredi = true;
        }
        if(Samedi.isChecked()){
            t.cal.Samedi = true;
        }
        if(Dimanche.isChecked()){
            t.cal.Dimanche = true;
        }
    }
}
