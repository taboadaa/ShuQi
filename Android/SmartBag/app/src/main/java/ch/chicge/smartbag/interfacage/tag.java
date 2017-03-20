package ch.chicge.smartbag.interfacage;

/**
 * Created by Vince on 20.03.2017.
 */

public class tag {
    public String ID = "";
    public String CustomName = "";

    public tag(String ID){
        this.ID = ID;
    }

    public void setCustomName(String CustomName){
        this.CustomName = CustomName;
    }

    public boolean hasCustomName(){
        if(CustomName.equals(""))
            return false;
        else
            return true;
    }
}
