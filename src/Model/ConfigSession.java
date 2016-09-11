package Model;

import Model.ConstantValue.ConfigSessionTypeEnum;

/**
 * Session variables to establish a communication with a SSH client.
 * @author SS
 */
public class ConfigSession {
	private ConfigSessionTypeEnum ConfigSessionType = ConfigSessionTypeEnum.SMART_PUTTY_SESSION;
	private String host = "";
	private String port = ""; // Can be a number or a device name (COM1, COM2, ...).
	private String user = "";
	private Protocol protocol;
	private String file = "";
	private String password = "";
	private String puttySession = "";
	
	public ConfigSession(String user,String password, String port,String puttySession){
		this.user = user;
		this.password = password;
		this.port = port;
		this.puttySession = puttySession;
		this.ConfigSessionType = ConfigSessionTypeEnum.PURE_PUTTY_SESSION;
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
		ConfigSessionType = ConfigSessionTypeEnum.SMART_PUTTY_SESSION;
	}
	
	
	public ConfigSessionTypeEnum getConfigSessionType() {
		return ConfigSessionType;
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
	
	public String getKey(){
		return file;
	}

	public void setKey(String file){
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
}
