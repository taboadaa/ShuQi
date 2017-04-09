package com.arete.custom;

import android.graphics.Color;

public class customCell
{
    String id;
    String name;
    String value;
    int color = Color.RED;
    
    public customCell()
    {
	
    }
    
    public customCell(String name,String value)
    {
	this.name = name;
	this.value = value;
    }
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    public void setId(String id){this.id = id;}
    public String getId(){return this.id;}
    public void setColor(int color){this.color = color;}
    public int getColor(){return this.color;}

}
