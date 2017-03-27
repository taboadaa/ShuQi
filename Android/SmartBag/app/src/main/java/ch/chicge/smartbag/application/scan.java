package ch.chicge.smartbag.application;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ch.chicge.smartbag.R;
import ch.chicge.smartbag.interfacage.tag;
import ch.chicge.smartbag.interfacage.utilitaire;

/**
 * Created by Vince on 20.03.2017.
 */

public class scan extends Activity {

    private ArrayList<tag> listTag = new ArrayList<tag>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
       /* listTag = utilitaire.getProxiTags();
        PrintListe(); */



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
