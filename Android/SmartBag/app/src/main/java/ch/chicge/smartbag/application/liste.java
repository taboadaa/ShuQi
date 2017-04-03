package ch.chicge.smartbag.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

//import ch.chicge.smartbag.R;
import ch.chicge.smartbag.MainActivity;
import ch.chicge.smartbag.R;
import ch.chicge.smartbag.interfacage.tag;
import ch.chicge.smartbag.interfacage.utilitaire;

/**
 * Created by Vince on 20.03.2017.
 */

public class liste extends Activity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listetag);
        for(tag t : utilitaire.getAllKnownTags()){
            listItems.add(t.CustomName);
        }
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView listView = (ListView) findViewById(R.id.taglist);
        listView.setAdapter(adapter);

        final Button retour = (Button) findViewById(R.id.retourListe);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(liste.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void onRestart(){
        super.onRestart();
    }

    protected void onResume(){
        super.onResume();
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

}
