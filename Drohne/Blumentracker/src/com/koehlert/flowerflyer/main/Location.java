package com.koehlert.flowerflyer.main;

public class Location {
	public float x;
	public float y;
	public float dist;
	
	public Location(){
		x=0;y=0;dist=0;
	}
	public Location(float x, float y, int dist){
		this.x = x; this.y = y; this.dist = dist;
	}
	
	@Override
	public String toString(){
		return "("+x+"|"+y+") d:"+dist;
	}
	
	
}
