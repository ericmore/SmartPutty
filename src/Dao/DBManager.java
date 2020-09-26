package Dao;

import java.sql.Connection;
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
import java.io.File;
import java.io.FileInputStream;


public class DBManager {
	private final static Logger logger = Logger.getLogger(DBManager.class);
	private final static String DATABASE_PATH = "database\\";
	private final static String DATABASE_NAME = "sessiondb";
	private static DBManager manager = null;
	private Connection conn = null;
	private Base64 base64 = new Base64();

	public boolean existCSessionTable = false, existIntranetTable = false;

	private DBManager(){
		String driver = "org.hsqldb.jdbcDriver";
		String protocol = "jdbc:hsqldb:";
		String user = "sa";
		String password = "";
		String sql = "";
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// Load JDBC driver:
			Class.forName(driver).newInstance();

			Properties props = new Properties();
			props.put("user", user);
			props.put("password", password);
			props.put("jdbc.strict_md", "false");
			props.put("jdbc.get_column_name", "false");
			props.put("shutdown", "true");

			conn = DriverManager.getConnection(protocol + DATABASE_PATH + DATABASE_NAME, props);
			logger.info("Connected to database");

			stmt = conn.createStatement();

			try {
				sql = "SELECT count(*) FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE table_schem='PUBLIC' AND table_name='CSESSION'";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					if (rs.getInt(1) == 1){ // If one row is returned, table exists
						existCSessionTable = true;
						logger.debug("OK: CSESSION table exists");
					}
				}

				sql = "SELECT count(*) FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE table_schem='PUBLIC' AND table_name='INTRANET'";
				rs = stmt.executeQuery(sql);
				while (rs.next()){
					if (rs.getInt(1) == 1){ // If one row is returned, table exists
						existIntranetTable = true;
						logger.debug("OK: INTRANET table exists");
					}
				}
			} catch (SQLException e){
				logger.error(ExceptionUtils.getStackTrace(e));
			} finally {
				rs.close();
			}

			// Check if needed tables exist:
			if (!existCSessionTable){
				ExecuteScript(stmt, "csession.sql");
			}
			if (!existIntranetTable){
				ExecuteScript(stmt, "intranet.sql");
			}
		} catch (Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
//			System.exit(-1);
		}

		// Release database resources:
		try {
			stmt.close();
		} catch (SQLException e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * To execute a single SQL script.
	 * @param stmt Statement.
	 * @param script SQL script to execute.
	 */
	private void ExecuteScript(Statement stmt, String script){
		try {
			File objBatch=new File(DATABASE_PATH + script);
			if (objBatch.exists()){
				FileInputStream objStream=new FileInputStream(objBatch);
				String strFileContent=new java.util.Scanner(objStream,"UTF-8").useDelimiter("\\A").next();
				String[] arrCommands=strFileContent.split(";");

				for(String strCommand : arrCommands){
					stmt.addBatch(strCommand);
				}
				stmt.executeBatch();
				logger.debug("OK: SQL script " + DATABASE_PATH + script + " executed");
			} else {
				logger.error("Cannot find database SQL script: " + DATABASE_PATH + script);
			}
		} catch (SQLException e){
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
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
