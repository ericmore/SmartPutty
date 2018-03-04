package Model;

import javax.persistence.*;

@Entity
@Table(name="SmartSession")
public class SmartSession {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String host;
    private String port;
    private String user;
    private String password;
    private String protocol;
    private String key;
    private String description;
    private String alias;
    private String type;
    private String puttySession;

    public SmartSession(){}
    public SmartSession(String host, String port, String user, String password, String protocol, String key, String description, String alias, String type, String puttySession) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.protocol = protocol;
        this.key = key;
        this.description = description;
        this.alias = alias;
        this.type = type;
        this.puttySession = puttySession;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPuttySession() {
        return puttySession;
    }

    public void setPuttySession(String puttySession) {
        this.puttySession = puttySession;
    }
}
