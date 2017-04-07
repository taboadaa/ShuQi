package phychips.arete.newver.interfacage;


/**
 * Created by Vince on 20.03.2017.
 */

public class tag {
    public String ID = "";
    public String CustomName = "";
    public calendrier cal;

    public tag(String ID){
        this.ID = ID;
        this.cal = new calendrier(false,false,false,false,false,false,false);
    }

    public void setCustomName(String CustomName){
        this.CustomName = CustomName;
    }

    public void setCal(boolean Lundi,boolean Mardi,boolean Mercredi,boolean Jeudi,boolean Vendredi,boolean Samedi,boolean Dimanche){
        cal.Lundi = Lundi;
        cal.Mardi = Mardi;
        cal.Mercredi = Mercredi;
        cal.Jeudi = Jeudi;
        cal.Vendredi = Vendredi;
        cal.Samedi = Samedi;
        cal.Dimanche = Dimanche;
    }

    public boolean hasCustomName(){
        if(CustomName.equals(""))
            return false;
        else
            return true;
    }
}
