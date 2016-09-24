package Dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import Model.ConfigSession;
import Model.Intranet;
import Model.Protocol;
import UI.MainFrame;

public class DBManager {
	private final static Logger logger = Logger.getLogger(DBManager.class);
	private final static String DATABASE_PATH = "database\\sessiondb";
	private static DBManager manager = null;
	private Connection conn = null;
	private Base64 base64 = new Base64();

	public boolean existCSessionTable = false, existIntranetTable = false;

	private DBManager(){
		try {
			String driver = "org.hsqldb.jdbcDriver";
			String protocol = "jdbc:hsqldb:";
			Class.forName(driver).newInstance();
			String user = "sa";
			String password = "";
			Properties props = new Properties();
			props.put("user", user);
			props.put("password", password);
			props.put("jdbc.strict_md", "false");
			props.put("jdbc.get_column_name", "false");
			props.put("shutdown", "true");
			conn = DriverManager.getConnection(protocol + DATABASE_PATH, props);
			logger.debug("Connect to Database");
			Statement state = conn.createStatement();
			String[] str = {"TABLE"};
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet result = meta.getTables("", null, null, str);
			while (result.next()){
				 logger.debug(
					        "Database Meta:   "+result.getString("TABLE_CAT") 
					       + ", "+result.getString("TABLE_SCHEM")
					       + ", "+result.getString("TABLE_NAME")
					       + ", "+result.getString("TABLE_TYPE")
					       + ", "+result.getString("REMARKS")); 
				if (result.getString("TABLE_NAME").equals("CSESSION")){
					existCSessionTable = true;
				} else if (result.getString("TABLE_NAME").equals("INTRANET")){
					existIntranetTable = true;
				}

			}
			result.close();
			if (!existCSessionTable){
				String sql = "CREATE TABLE CSession(Host varchar(50), Port varchar(10), User varchar(50), Protocol varchar(10), Key varchar(100), Password varchar(50), PRIMARY KEY (Host,Port,User,Protocol))";
				state.execute(sql);
			}
			if (!existIntranetTable){
				String sql = "CREATE TABLE Intranet(ID varchar(50),Password varchar(50), Desthost varchar(50), PRIMARY KEY (ID,Desthost))";
				state.execute(sql);

			}

			state.close();

		} catch (Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
//			System.exit(-1);
		}
	}


	public static DBManager getDBManagerInstance(){
		if (manager == null){
			manager = new DBManager();
		}
		return manager;
	}

	public void insertCSession(ConfigSession csession){
		String host = csession.getHost();
		String port = csession.getPort();
		String user = csession.getUser();
		String protocol = csession.getProtocol().name();
		String key = csession.getKey();
		String password = new String(base64.encode((csession.getPassword()).getBytes()));
		if (host.isEmpty() || port.isEmpty() || user.isEmpty() || protocol.isEmpty()){
			MessageDialog.openWarning(MainFrame.shell, "Warning", "host,port,user,protocol should not be set to null");
			return;
		}
		if (isCSessionExist(csession)){
			deleteCSession(csession);
		}

		String sql = "INSERT INTO CSession VALUES('"
			+ host + "','"
			+ port + "','"
			+ user + "','"
			+ protocol + "','"
			+ key + "','"
			+ password + "')";

		try {
			Statement state = conn.createStatement();
			state.execute(sql);
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void deleteCSession(ConfigSession csession){
		if (!isCSessionExist(csession)){
			return;
		}

		try {
			Statement state = conn.createStatement();
			String sql = "DELETE FROM CSession WHERE host='"
				+ csession.getHost() + "' AND port='"
				+ csession.getPort() + "' AND user='"
				+ csession.getUser() + "' AND protocol='"
				+ csession.getProtocol() + "'";
			state.execute(sql);
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public ArrayList<ConfigSession> getAllCSessions(){
		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
		String sql = "SELECT *  FROM CSession";

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				ConfigSession confSession = new ConfigSession(
					rs.getString("Host"),
					rs.getString("Port"),
					rs.getString("User"),
					Protocol.valueOf(rs.getString("Protocol")),
					rs.getString("Key"),
					new String(base64.decode(rs.getString("Password"))));
				result.add(confSession);
			}

			rs.close();
			state.close();

		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ConfigSession> queryCSessionByHost(String host){
		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
		String sql = "SELECT *  FROM CSession WHERE host='" + host + "'";

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				ConfigSession confSession = new ConfigSession(
					rs.getString("Host"),
					rs.getString("Port"),
					rs.getString("User"),
					Protocol.valueOf(rs.getString("Protocol")),
					rs.getString("Key"),
					new String(base64.decode(rs.getString("Password"))));
				result.add(confSession);
			}
			rs.close();
			state.close();

		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ConfigSession> queryCSessionByHostUser(String host, String user){
		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
		String sql = "SELECT *  FROM CSession WHERE HOST='" + host + "' AND USER='" + user + "'";

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				ConfigSession confSession = new ConfigSession(
					rs.getString("Host"),
					rs.getString("Port"),
					rs.getString("User"),
					Protocol.valueOf(rs.getString("Protocol")),
					rs.getString("Key"),
					new String(base64.decode(rs.getString("Password"))));
				result.add(confSession);
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public ConfigSession queryCSessionByHostUserProtocol(String host, String user, String protocol){
		ConfigSession result = null;
		String sql = "SELECT *  FROM CSession WHERE host='"
			+ host + "' AND user='"
			+ user + "' AND protocol='"
			+ protocol + "'";

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				ConfigSession confSession = new ConfigSession(
					rs.getString("Host"),
					rs.getString("Port"),
					rs.getString("User"),
					Protocol.valueOf(rs.getString("Protocol")),
					rs.getString("Key"),
					new String(base64.decode(rs.getString("Password"))));
				result = confSession;
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public ConfigSession queryCSessionBySession(ConfigSession session){
		String host = session.getHost();
		String user = session.getUser();
		String protocol = session.getProtocol().name();
//		String password = Base64Util.encodeBASE64(session.getPassword());
		ConfigSession result = null;
		String sql = String.format("SELECT * FROM CSession WHERE host='%s' AND user='%s' AND protocol='%s'", host, user, protocol);
		

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				ConfigSession confSession = new ConfigSession(
					rs.getString("Host"),
					rs.getString("Port"),
					rs.getString("User"),
					Protocol.valueOf(rs.getString("Protocol")),
					rs.getString("Key"),
					new String(base64.decode(rs.getString("Password"))));
				result = confSession;
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}

		return result;
	}

	private boolean isCSessionExist(ConfigSession csession){
		boolean isExist = false;
		String sql = "SELECT * FROM CSession WHERE host='"
			+ csession.getHost() + "' AND port='"
			+ csession.getPort() + "' AND user='"
			+ csession.getUser() + "' AND protocol='"
			+ csession.getProtocol() + "'";

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				isExist = true;
				break;
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return isExist;
	}

	

	
	

	public void closeDB(){
		try {
			conn.close();
			logger.debug("Committed transaction and closed connection");
		} catch (SQLException e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}
}
