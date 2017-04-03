package ch.chicge.smartbag.interfacage;

import java.util.ArrayList;

/**
 * Created by Vince on 21.03.2017.
 */

public class utilitaire {
    public static ArrayList<tag> allTags;
    public static ArrayList<tag> proxyTags;

    public static void setAllTags(){
        ArrayList<tag> listTag = new ArrayList<tag>();
        tag t = new tag("0141-5551-5508-2308-7357-4523");
        listTag.add(t);
        t.setCustomName("mon nom custom");
        listTag.add(t);
        allTags = listTag;
    }

    public static void setProxyTags(){
        ArrayList<tag> listTag = new ArrayList<tag>();
        tag t = new tag("0141-5551-5508-2308-7357-4523");
        listTag.add(t);
        t.setCustomName("mon nom custom");
        listTag.add(t);
        proxyTags = listTag;
    }

    public static ArrayList<tag> getProxiTags(){
        return proxyTags;
    }

    public static tag getTagbyID(String id){
        for (tag t: allTags) {
            if(t.ID.equals(id)){
                return t;
            }
        }
        return new tag(id);
    }

    public static ArrayList<tag> getAllKnownTags(){
        return allTags;
    }

    public static void saveTags(){

    }

    public static void saveAgenda(){

    }

}
