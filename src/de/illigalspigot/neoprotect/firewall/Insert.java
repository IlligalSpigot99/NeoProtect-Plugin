package de.illigalspigot.neoprotect.firewall;

import java.util.regex.Pattern;

public class Insert {
	
	private int id;
	private Type type;
	private String ip;
	private String date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Insert(int id, Type type, String ip, String date) {
		this.id = id;
		this.type = type;
		this.ip = ip;
		this.date = date;
		this.date = date.split(Pattern.quote("T"))[1].split(Pattern.quote("."))[0] + " " + date.split(Pattern.quote("-"))[2] + "." + date.split(Pattern.quote("-"))[1] + "." + date.split(Pattern.quote("-"))[0];
	}
	

}
