package ch.chicge.smartbag.application;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;

import ch.chicge.smartbag.R;
import ch.chicge.smartbag.interfacage.tag;

/**
 * Created by Vince on 20.03.2017.
 */

public class liste extends Activity {

    private ArrayList<tag> listTag = new ArrayList<tag>();

    public void setBaseView(){
        setContentView(R.layout.liste);
        tag t = new tag("fsedvs");
        listTag.add(t);
    }

    public liste(){

    }

    public void PrintListe(){
        for (tag t : listTag) {
            if(t.hasCustomName()){

            }
            else{

            }
        }
    }
}
