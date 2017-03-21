package ch.chicge.smartbag.interfacage;

import java.util.ArrayList;

/**
 * Created by Vince on 21.03.2017.
 */

public class utilitaire {
    public utilitaire(){

    }

    public static ArrayList getProxiTags(){
        ArrayList<tag> listTag = new ArrayList<tag>();
        tag t = new tag("0141-5551-5508-2308-7357-4523");
        listTag.add(t);
        t.setCustomName("mon nom custom");
        listTag.add(t);
        t.setCustomName("mon nom custom2");
        listTag.add(t);
        return listTag;
    }

    public static ArrayList getAllKnowTags(){
        ArrayList<tag> listTag = new ArrayList<tag>();
        tag t = new tag("0141-5551-5508-2308-7357-4523");
        listTag.add(t);
        t.setCustomName("mon nom custom");
        listTag.add(t);
        t.setCustomName("mon nom custom2");
        listTag.add(t);
        return listTag;
    }

    public static void saveTags(){

    }

    public static void saveAgenda(){

    }

}
