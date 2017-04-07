package phychips.arete.newver.application;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import phychips.arete.newver.MainActivity;
import phychips.arete.newver.R;
import phychips.arete.newver.interfacage.calendrier;
import phychips.arete.newver.interfacage.tag;
import phychips.arete.newver.interfacage.utilitaire;


public class TagView extends Activity {
    tag t;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;

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
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setCustomName(e.getText().toString());
                addCustomName();
                setCalendrier(t);
                editor.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id =this.getIntent().getStringExtra("myTag");
        this.t = utilitaire.getTagbyID(id);
        final EditText e = (EditText) findViewById(R.id.editName);
        String tmp = sharedPref.getString(t.ID, "inconnu");
        t.setCustomName(tmp);
        if(t.hasCustomName())
            e.setText(t.CustomName);
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
            editor.putString(t.ID, t.CustomName);
        }
        else{
            text.setText("Custom Name : " + "pas de custom name");
        }
    }

    public void addCalendrier(){
        String Lundis = sharedPref.getString(t.ID+"MONDAY", "F");
        String Mardis = sharedPref.getString(t.ID+"TUESDAY", "F");
        String Mercredis = sharedPref.getString(t.ID+"WEDNESDAY", "F");
        String Jeudis = sharedPref.getString(t.ID+"THURSDAY", "F");
        String Vendredis = sharedPref.getString(t.ID+"FRIDAY", "F");
        String Samedis = sharedPref.getString(t.ID+"SATURDAY", "F");
        String Dimanches = sharedPref.getString(t.ID+"SUNDAY", "F");

        //calendrier cal = t.cal;

        if(Lundis.equals("T")) {
            final CheckBox Lundi = (CheckBox) findViewById(R.id.Lundi);
            Lundi.setChecked(true);
        }
        if(Mardis.equals("T")) {
            final CheckBox Mardi = (CheckBox) findViewById(R.id.Mardi);
            Mardi.setChecked(true);
        }
        if(Mercredis.equals("T")) {
            final CheckBox Mercredi = (CheckBox) findViewById(R.id.Mercredi);
            Mercredi.setChecked(true);
        }
        if(Jeudis.equals("T")) {
            final CheckBox Jeudi = (CheckBox) findViewById(R.id.Jeudi);
            Jeudi.setChecked(true);
        }
        if(Vendredis.equals("T")) {
            final CheckBox Vendredi = (CheckBox) findViewById(R.id.Vendredi);
            Vendredi.setChecked(true);
        }
        if(Samedis.equals("T")) {
            final CheckBox Samedi = (CheckBox) findViewById(R.id.Samedi);
            Samedi.setChecked(true);
        }
        if(Dimanches.equals("T")) {
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
            editor.putString(t.ID+"Lundi", "T");
        }
        else{
            t.cal.Lundi = false;
            editor.putString(t.ID+"Lundi", "F");
        }
        if(Mardi.isChecked()){
            t.cal.Mardi = true;
            editor.putString(t.ID+"Mardi", "T");
        }
        else{
            t.cal.Mardi = false;
            editor.putString(t.ID+"Mardi", "F");
        }
        if(Mercredi.isChecked()){
            t.cal.Mercredi = true;
            editor.putString(t.ID+"Mercredi", "T");
        }
        else{
            t.cal.Mercredi = false;
            editor.putString(t.ID+"Mercredi", "F");
        }
        if(Jeudi.isChecked()){
            t.cal.Jeudi = true;
            editor.putString(t.ID+"Jeudi", "T");
        }
        else{
            t.cal.Jeudi = false;
            editor.putString(t.ID+"Jeudi", "F");
        }
        if(Vendredi.isChecked()){
            t.cal.Vendredi = true;
            editor.putString(t.ID+"Vendredi", "T");
        }
        else{
            t.cal.Vendredi = false;
            editor.putString(t.ID+"Vendredi", "F");
        }
        if(Samedi.isChecked()){
            t.cal.Samedi = true;
            editor.putString(t.ID+"Samedi", "T");
        }
        else{
            t.cal.Samedi = false;
            editor.putString(t.ID+"Samedi", "F");
        }
        if(Dimanche.isChecked()){
            t.cal.Dimanche = true;
            editor.putString(t.ID+"Dimanche", "T");
        }
        else{
            t.cal.Dimanche = false;
            editor.putString(t.ID+"Dimanche", "F");
        }
    }
}