package ch.chicge.smartbag.interfacage;

import java.util.ArrayList;

/**
 * Created by Vince on 21.03.2017.
 */

public class utilitaire {
    public static ArrayList<tag> allTags;
    public static ArrayList<String> proxyTags;

    public static void setAllTags(){
        ArrayList<tag> listTag = new ArrayList<tag>();
        tag t = new tag("0141-5551-5508-2308-7357-4523");
        t.setCustomName("mon nom custom");
        listTag.add(t);
        t = new tag("0141-5551-5508-2308-7357-4529");
        t.setCustomName("le truc que j'oublie tout le temps");
        listTag.add(t);
        allTags = listTag;
    }

    public static void setProxyTags(){
        ArrayList<String> listTag = new ArrayList<String>();
        listTag.add("0141-5551-5508-2308-7357-4523");
        //listTag.add("0141-5551-5508-2308-7357-4525");
        listTag.add("0141-5551-5508-2308-7357-4527");
        proxyTags = listTag;
    }

    public static ArrayList<String> getProxiTags(){
        return proxyTags;
    }

    public static tag getTagbyID(String id){
        for (tag t: allTags) {
            if(t.ID.equals(id)){
                return t;
            }
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
