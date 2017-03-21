package ch.chicge.smartbag.application;

import android.app.Activity;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

//import ch.chicge.smartbag.R;
import ch.chicge.smartbag.interfacage.tag;
import ch.chicge.smartbag.interfacage.utilitaire;

/**
 * Created by Vince on 20.03.2017.
 */

public class liste extends Activity {

    private ArrayList<tag> listTag = new ArrayList<tag>();

    public void setBaseView(){
        //setContentView(R.layout.liste);
        listTag = utilitaire.getAllKnowTags();
    }

    public liste(){

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
}
