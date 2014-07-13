package com.zach.wilson.magic.app.models;

import java.io.Serializable;

public class Format implements Serializable {

	String standard;
	String modern;
	String commander;
	String legacy;
	String vintage;
	
	
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	
	public String getModern() {
		return modern;
	}
	public void setModern(String modern) {
		this.modern = modern;
	}
	public String getCommander() {
		return commander;
	}
	public void setCommander(String commander) {
		this.commander = commander;
	}
	public String getLegacy() {
		return legacy;
	}
	public void setLegacy(String legacy) {
		this.legacy = legacy;
	}
	public String getVintage() {
		return vintage;
	}
	public void setVintage(String vintage) {
		this.vintage = vintage;
	}
	
	@Override
	public String toString(){
		return this.modern + " " + this.vintage + " " + this.legacy;
		
	}
	
	
}
