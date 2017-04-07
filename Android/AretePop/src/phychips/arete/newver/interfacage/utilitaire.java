package phychips.arete.newver.interfacage;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import phychips.arete.newver.R;

/**
 * Created by Vince on 21.03.2017.
 */

public class utilitaire {
    public static ArrayList<tag> allTags = new ArrayList<tag>();
    public static ArrayList<String> proxyTags = new ArrayList<String>();
    public static SharedPreferences sharedPref;

    public static void setAllTags(){
        tag t = new tag("0141-5551-5508-2308-7357-4523");
        t.setCustomName("mon nom custom");
        allTags.add(t);
        t = new tag("0141-5551-5508-2308-7357-4529");
        t.setCustomName("le truc que j'oublie tout le temps");
        allTags.add(t);
    }

    public static void setProxyTags(){
        proxyTags.add("0141-5551-5508-2308-7357-4523");
        //listTag.add("0141-5551-5508-2308-7357-4525");
        proxyTags.add("0141-5551-5508-2308-7357-4527");
    }

    public static ArrayList<String> getProxiTags(){
        return proxyTags;
    }

    public static tag getTagbyID(String id){
        for (tag t: allTags) {
            if(t.ID.equals(id)){return t;}
        }
        tag t = new tag(id);
        allTags.add(t);
        return t;
    }

    public static ArrayList<tag> getAllKnownTags(){
        return allTags;
    }

    public static void saveTags(Context context){
    }

    public static void saveAgenda(){

    }

}
