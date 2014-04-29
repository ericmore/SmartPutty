package Model;

public class ConfigSession {

	public ConfigSession(String host, String user, String protocol, String file,String password){
		this.host = host;
		this.user = user;
		this.protocol = protocol;
		this.file = file;
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getKey() {
    return file;
  }
  public void setFile(String file) {
    this.file = file;
  }



  private String host = "";
	private String user = "";
	private String password = "";
	private String protocol = "";
	private String file = "";
	

}
