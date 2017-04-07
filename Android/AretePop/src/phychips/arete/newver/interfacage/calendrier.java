package phychips.arete.newver.interfacage;


/**
 * Created by Vince on 03.04.2017.
 */

public class calendrier {
    public boolean Lundi = false;
    public boolean Mardi = false;
    public boolean Mercredi = false;
    public boolean Jeudi = false;
    public boolean Vendredi = false;
    public boolean Samedi = false;
    public boolean Dimanche = false;

    public calendrier(boolean Lundi,boolean Mardi,boolean Mercredi,boolean Jeudi,boolean Vendredi,boolean Samedi,boolean Dimanche){
        this.Lundi = Lundi;
        this.Mardi = Mardi;
        this.Mercredi = Mercredi;
        this.Jeudi = Jeudi;
        this.Vendredi = Vendredi;
        this.Samedi = Samedi;
        this.Dimanche = Dimanche;
    }

    public String toString(){
        String tmp = "";
        if(Lundi)
            tmp += "Lundi";
        if(Mardi)
            tmp += " - Mardi";
        if(Mercredi)
            tmp += " - Mercredi";
        if(Jeudi)
            tmp += " - Jeudi";
        if(Vendredi)
            tmp += " - Vendredi";
        if(Samedi)
            tmp += " - Samedi";
        if(Dimanche)
            tmp += " - Dimanche";
        return tmp;
    }
}
