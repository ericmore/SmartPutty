package Dao;

import Model.ConfigSession;
import Model.Intranet;
import Model.Protocol;
import UI.MainFrame;
import Utils.RegistryUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import org.eclipse.jface.dialogs.MessageDialog;

public class DBManager {
	private final static String DATABASE_PATH = "database\\sessiondb";
	private static DBManager manager = null;
	private Connection conn = null;

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

			Statement state = conn.createStatement();
			String[] str = {"TABLE"};
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet result = meta.getTables("", null, null, str);
			while (result.next()){
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
				// System.out.println(sql);
			}
			if (!existIntranetTable){
				String sql = "CREATE TABLE Intranet(ID varchar(50),Password varchar(50), Desthost varchar(50), PRIMARY KEY (ID,Desthost))";
				//System.out.println(sql);
				state.execute(sql);

			}

			state.close();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void exportDB(){
		// TODO
	}

	public void importDB(){
		// TODO
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
		String password = Base64Util.encodeBASE64(csession.getPassword());
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
			System.out.println("insertCSession() " + sql); //DEBUG
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
			// System.out.println(sql); //DEBUG
			state.execute(sql);
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public ArrayList<ConfigSession> getAllCSessions(){
		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
		String sql = "SELECT *  FROM CSession";
		//	System.out.println(sql);

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
					Base64Util.decodeBASE64(rs.getString("Password")),
					RegistryUtils.SMARTPUTTY_SESSION);
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
		// System.out.println(sql); //DEBUG

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
					Base64Util.decodeBASE64(rs.getString("Password")),
					RegistryUtils.SMARTPUTTY_SESSION);
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
		// System.out.println(sql);

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
					Base64Util.decodeBASE64(rs.getString("Password")),
					RegistryUtils.SMARTPUTTY_SESSION);
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
		// System.out.println(sql); //DEBUG

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
					Base64Util.decodeBASE64(rs.getString("Password")),
					RegistryUtils.SMARTPUTTY_SESSION);
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
		String sql = "SELECT *  FROM CSession WHERE host='"
			+ host + "' AND user='"
			+ user + "' AND protocol='"
			+ protocol + "'";
		// System.out.println("queryCSessionBySession() " + sql); //DEBUG

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				// System.out.println("queryCSessionBySession() " + rs.getString("Host")); //DEBUG
				ConfigSession confSession = new ConfigSession(
					rs.getString("Host"),
					rs.getString("Port"),
					rs.getString("User"),
					Protocol.valueOf(rs.getString("Protocol")),
					rs.getString("Key"),
					Base64Util.decodeBASE64(rs.getString("Password")),
					RegistryUtils.SMARTPUTTY_SESSION);
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
		// System.out.println(sql); //DEBUG

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

	public ArrayList<Intranet> getAllIntranets(){
		ArrayList<Intranet> result = new ArrayList<Intranet>();
		String sql = "SELECT *  FROM Intranet";
		//	System.out.println(sql);

		try {
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				result.add(new Intranet(
					rs.getString("ID"),
					Base64Util.decodeBASE64(rs.getString("Password")),
					rs.getString("Desthost")));
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public Intranet queryIntranet(Intranet intranet){
		try {
			String sql = "SELECT *  FROM Intranet WHERE ID='" + intranet.getIntranetID() + "' AND DestHost='" + intranet.getDesthost() + "'";
			//	System.out.println(sql);
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				return new Intranet(
					rs.getString("ID"),
					Base64Util.decodeBASE64(rs.getString("Password")),
					rs.getString("DestHost"));
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public void insertIntranet(Intranet intranet){
		String intranetID = intranet.getIntranetID();
		String intranetPassword = Base64Util.encodeBASE64(intranet.getIntranetPassword());
		String desthost = intranet.getDesthost();
		if (intranetID.equals("") || desthost.equals("")){
			MessageDialog.openWarning(MainFrame.shell, "warning", "intranetID,DestHost can not be set to null");
			return;
		}
		if (isIntranetExist(intranet)){
			deleteIntranet(intranet);
		}

		String sql = "INSERT INTO Intranet VALUES('" + intranetID + "','"
			+ intranetPassword + "','"
			+ desthost + "')";
		//	System.out.println(sql);

		try {
			Statement state = conn.createStatement();
			state.execute(sql);
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void deleteIntranet(Intranet intranet){
		if (!isIntranetExist(intranet)){
			return;
		}
		try {
			Statement state = conn.createStatement();
			String sql = "DELETE FROM Intranet WHERE ID='"
				+ intranet.getIntranetID() + "' AND Desthost='"
				+ intranet.getDesthost() + "'";
			//System.out.println(sql);
			state.execute(sql);
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	private boolean isIntranetExist(Intranet intranet){
		boolean ret = false;
		try {
			String sql = "SELECT *  FROM Intranet WHERE ID='" + intranet.getIntranetID() + "' AND Desthost='" + intranet.getDesthost() + "'";
			//	System.out.println(sql);
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				ret = true;
				break;
			}
			rs.close();
			state.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return ret;
	}

	public void closeDB(){
		try {
			conn.close();
			System.out.println("Committed transaction and closed connection");
		} catch (SQLException e){
			e.printStackTrace();
		}

	}
}
