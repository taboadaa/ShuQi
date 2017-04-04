package com.arete.custom;

public class seqTag 
{
	
	String tag;
	String count;
	
	public seqTag(String tag,String count)
	{
	    this.tag = tag;
	    this.count = count;
	}
	
	public seqTag(){
	    
	}
	
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}

}
