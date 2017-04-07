package ch.chicge.smartbag.interfacage;

import java.util.ArrayList;

/**
 * Created by Vince on 21.03.2017.
 */

public class utilitaire {
    private static boolean created1 = false;
    private static boolean created2 = false;
    public static ArrayList<tag> allTags = new ArrayList<tag>();
    public static ArrayList<String> proxyTags = new ArrayList<String>();

    public static void setAllTags(){
        if(!created1) {
            allTags = new ArrayList<tag>();
            tag t = new tag("0141-5551-5508-2308-7357-4523");
            t.setCustomName("mon nom custom");
            allTags.add(t);
            t = new tag("0141-5551-5508-2308-7357-4529");
            t.setCustomName("le truc que j'oublie tout le temps");
            allTags.add(t);
            created1 = true;
        }
    }

    public static void setProxyTags(){
        if(!created2) {
            proxyTags = new ArrayList<String>();
            proxyTags.add("0141-5551-5508-2308-7357-4523");
            //listTag.add("0141-5551-5508-2308-7357-4525");
            proxyTags.add("0141-5551-5508-2308-7357-4527");
            created2 = true;
        }
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

    public static void saveTags(){

    }

    public static void saveAgenda(){

    }

}
