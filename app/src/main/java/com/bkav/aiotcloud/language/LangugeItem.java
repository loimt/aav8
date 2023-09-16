package com.bkav.aiotcloud.language;


import com.bkav.aiotcloud.util.Xml;

public class LangugeItem {
	public LangugeItem(Xml xml) {
		this.key = xml.getAttrib("key");
		this.vaule = xml.getAttrib("value");
	}
	
	public String getKey() {
		return key;
	}
	
	public String getVaule() {
		return vaule;
	}
	
	private final String key;
	private final String vaule;
}