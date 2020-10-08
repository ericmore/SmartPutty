package com.sp.Model;

public class Intranet {

	private String IntranetID = "";
	private String IntranetPassword = "";
	private String Desthost = "";
	

	public Intranet(String intranetID, String intranetPassword, String destHost) {
		IntranetID = intranetID;
		IntranetPassword = intranetPassword;
		Desthost = destHost;
	}
	public void printObj(){
		System.out.println(getIntranetID());
		System.out.println(getIntranetPassword());
		System.out.println(getDesthost());
	}

	public String getIntranetID() {
		return IntranetID;
	}
	public void setIntranetID(String intranetID) {
		IntranetID = intranetID;
	}
	public String getIntranetPassword() {
		return IntranetPassword;
	}
	public void setIntranetPassword(String intranetPassword) {
		IntranetPassword = intranetPassword;
	}
	public String getDesthost() {
		return Desthost;
	}

	public void setDesthost(String destHost) {
		Desthost = destHost;
	}

}
