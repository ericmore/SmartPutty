package Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//import Control.Configuration;
import Model.ConstantValue;
import Model.SmartSession;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBManager {
	private final static Logger logger = Logger.getLogger(DBManager.class);
	private static DBManager manager = null;
	private Connection conn = null;
	private Base64 base64 = new Base64();
	private Session session ;

	public boolean existCSessionTable = false, existIntranetTable = false;

	private DBManager(){
		try {
			// (4a)
//			Server server = Server.createTcpServer("-tcpAllowOthers","-tcpPort","9092").start();    // (4b)
			if(ConstantValue.MODE.equals("DEV")){
//				this.enableWebServer();
			}


			SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
					.buildSessionFactory();
			this.session = sessionFactory.openSession();


		} catch (Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
//			System.exit(-1);
		}
	}

	public static DBManager getDBManagerInstance(){
		if(manager == null){
			manager = new DBManager();
		}
		return manager;
	}

	/**
	 * enable browser to view H2 database with
	 * http://localhost:8082/
	 * Generic H2 (Embedded)
	 * jdbc:h2:file:database\smartdb
	 */
	public void enableWebServer(){
		try {
			Server webServer = Server.createWebServer("-webAllowOthers","-webPort","8082").start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}




	public void insertSmartSession(SmartSession smartSession){
		session.beginTransaction();
		session.saveOrUpdate(smartSession);
		session.getTransaction().commit();
	}

	public void deleteSmartSession(SmartSession smartSession){
		session.beginTransaction();
		session.delete(smartSession);
		session.getTransaction().commit();
	}

	public List<SmartSession> findAllSmartSession(){
		session.beginTransaction();
		List<SmartSession> result = session.createQuery("from SmartSession ").list();
		session.getTransaction().commit();
		return result;
	}

	public SmartSession findById(int id){
		session.beginTransaction();
		SmartSession smartSession = session.find(SmartSession.class,id);
		session.getTransaction().commit();
		return smartSession;
	}

//	public void insertCSession(ConfigSession csession){
//		String host = csession.getHost();
//		String port = csession.getPort();
//		String user = csession.getUser();
//		String protocol = csession.getProtocol().name();
//		String key = csession.getKey();
//		String password = new String(base64.encode((csession.getPassword()).getBytes()));
//		if (host.isEmpty() || port.isEmpty() || user.isEmpty() || protocol.isEmpty()){
//			MessageDialog.openWarning(MainFrame.shell, "Warning", "host,port,user,protocol should not be set to null");
//			return;
//		}
//		if (isCSessionExist(csession)){
//			deleteCSession(csession);
//		}
//
//		String sql = "INSERT INTO CSession VALUES('"
//			+ host + "','"
//			+ port + "','"
//			+ user + "','"
//			+ protocol + "','"
//			+ key + "','"
//			+ password + "')";
//
//		try {
//			Statement state = conn.createStatement();
//			state.execute(sql);
//			state.close();
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//	}

//	public void deleteCSession(ConfigSession csession){
//		if (!isCSessionExist(csession)){
//			return;
//		}
//
//		try {
//			Statement state = conn.createStatement();
//			String sql = "DELETE FROM CSession WHERE host='"
//				+ csession.getHost() + "' AND port='"
//				+ csession.getPort() + "' AND user='"
//				+ csession.getUser() + "' AND protocol='"
//				+ csession.getProtocol() + "'";
//			state.execute(sql);
//			state.close();
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//	}

//	public ArrayList<ConfigSession> getAllCSessions(){
//		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
//		String sql = "SELECT *  FROM CSession";
//
//		try {
//			Statement state = conn.createStatement();
//			ResultSet rs = state.executeQuery(sql);
//			while (rs.next()){
//				ConfigSession confSession = new ConfigSession(
//					rs.getString("Host"),
//					rs.getString("Port"),
//					rs.getString("User"),
//					Protocol.valueOf(rs.getString("Protocol")),
//					rs.getString("Key"),
//					new String(base64.decode(rs.getString("Password"))));
//				result.add(confSession);
//			}
//
//			rs.close();
//			state.close();
//
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//		return result;
//	}

//	public ArrayList<ConfigSession> queryCSessionByHost(String host){
//		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
//		String sql = "SELECT *  FROM CSession WHERE host='" + host + "'";
//
//		try {
//			Statement state = conn.createStatement();
//			ResultSet rs = state.executeQuery(sql);
//			while (rs.next()){
//				ConfigSession confSession = new ConfigSession(
//					rs.getString("Host"),
//					rs.getString("Port"),
//					rs.getString("User"),
//					Protocol.valueOf(rs.getString("Protocol")),
//					rs.getString("Key"),
//					new String(base64.decode(rs.getString("Password"))));
//				result.add(confSession);
//			}
//			rs.close();
//			state.close();
//
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//		return result;
//	}

//	public ArrayList<ConfigSession> queryCSessionByHostUser(String host, String user){
//		ArrayList<ConfigSession> result = new ArrayList<ConfigSession>();
//		String sql = "SELECT *  FROM CSession WHERE HOST='" + host + "' AND USER='" + user + "'";
//
//		try {
//			Statement state = conn.createStatement();
//			ResultSet rs = state.executeQuery(sql);
//			while (rs.next()){
//				ConfigSession confSession = new ConfigSession(
//					rs.getString("Host"),
//					rs.getString("Port"),
//					rs.getString("User"),
//					Protocol.valueOf(rs.getString("Protocol")),
//					rs.getString("Key"),
//					new String(base64.decode(rs.getString("Password"))));
//				result.add(confSession);
//			}
//			rs.close();
//			state.close();
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//		return result;
//	}

//	public ConfigSession queryCSessionByHostUserProtocol(String host, String user, String protocol){
//		ConfigSession result = null;
//		String sql = "SELECT *  FROM CSession WHERE host='"
//			+ host + "' AND user='"
//			+ user + "' AND protocol='"
//			+ protocol + "'";
//
//		try {
//			Statement state = conn.createStatement();
//			ResultSet rs = state.executeQuery(sql);
//			while (rs.next()){
//				ConfigSession confSession = new ConfigSession(
//					rs.getString("Host"),
//					rs.getString("Port"),
//					rs.getString("User"),
//					Protocol.valueOf(rs.getString("Protocol")),
//					rs.getString("Key"),
//					new String(base64.decode(rs.getString("Password"))));
//				result = confSession;
//			}
//			rs.close();
//			state.close();
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//		return result;
//	}

//	public ConfigSession queryCSessionBySession(ConfigSession session){
//		String host = session.getHost();
//		String user = session.getUser();
//		String protocol = session.getProtocol().name();
////		String password = Base64Util.encodeBASE64(session.getPassword());
//		ConfigSession result = null;
//		String sql = String.format("SELECT * FROM CSession WHERE host='%s' AND user='%s' AND protocol='%s'", host, user, protocol);
//
//
//		try {
//			Statement state = conn.createStatement();
//			ResultSet rs = state.executeQuery(sql);
//			while (rs.next()){
//				ConfigSession confSession = new ConfigSession(
//					rs.getString("Host"),
//					rs.getString("Port"),
//					rs.getString("User"),
//					Protocol.valueOf(rs.getString("Protocol")),
//					rs.getString("Key"),
//					new String(base64.decode(rs.getString("Password"))));
//				result = confSession;
//			}
//			rs.close();
//			state.close();
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//
//		return result;
//	}

//	private boolean isCSessionExist(ConfigSession csession){
//		boolean isExist = false;
//		String sql = "SELECT * FROM CSession WHERE host='"
//			+ csession.getHost() + "' AND port='"
//			+ csession.getPort() + "' AND user='"
//			+ csession.getUser() + "' AND protocol='"
//			+ csession.getProtocol() + "'";
//
//		try {
//			Statement state = conn.createStatement();
//			ResultSet rs = state.executeQuery(sql);
//			while (rs.next()){
//				isExist = true;
//				break;
//			}
//			rs.close();
//			state.close();
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
//		return isExist;
//	}


}
