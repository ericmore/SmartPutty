package com.sp.entity;


import com.sp.model.ConstantValue;
import com.sp.model.Protocol;

import javax.persistence.*;

/**
 * Session variables to establish a communication with a SSH client.
 */
@Entity
public class ConfigSession {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int sessionType = ConstantValue.SMART_PUTTY_SESSION;
	private String host;
	private String port; // Can be a number or a device name (COM1, COM2, ...).
	private String user;
	private Protocol protocol;
	private String file;
	private String password;
	private String puttySession;

	public ConfigSession(String user,String password, String port,String puttySession){
		this.user = user;
		this.password = password;
		this.port = port;
		this.puttySession = puttySession;
		this.sessionType = ConstantValue.PURE_PUTTY_SESSION;
	}

	/**
	 * SmartPutty Session constructor
	 * @param host
	 * @param port
	 * @param user
	 * @param protocol
	 * @param file
	 * @param password
	 */
	public ConfigSession(String host, String port, String user, Protocol protocol, String file, String password){
		this.host = host;
		this.port = port;
		this.user = user;
		this.protocol = protocol;
		this.file = file;
		this.password = password;
		this.sessionType = ConstantValue.SMART_PUTTY_SESSION;
	}

	public ConfigSession() {

	}

	public int getSessionType() {
		return sessionType;
	}

	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}

	public String getHost(){
		return host;
	}

	public void setHost(String host){
		this.host = host;
	}

	public String getPort(){
		return port;
	}

	public void setPort(String port){
		this.port = port;
	}

	public String getUser(){
		return user;
	}

	public void setUser(String user){
		this.user = user;
	}

	public Protocol getProtocol(){
		return protocol;
	}

	public void setProtocol(Protocol protocol){
		this.protocol = protocol;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getSession(){
		return puttySession;
	}

	public void setSession(String session){
		this.puttySession = session;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
